package com.example.feedforward_courier.utils;

import android.util.Log;

import com.example.feedforward_courier.interfacea.ApiCallback;
import com.example.feedforward_courier.interfacea.ApiService;
import com.example.feedforward_courier.models.server.CommandOptions;
import com.example.feedforward_courier.models.server.DistanceUnit;
import com.example.feedforward_courier.models.server.command.CommandBoundary;
import com.example.feedforward_courier.models.server.object.Location;
import com.example.feedforward_courier.models.server.object.ObjectBoundary;
import com.example.feedforward_courier.models.server.user.NewUserBoundary;
import com.example.feedforward_courier.models.server.user.RoleEnum;
import com.example.feedforward_courier.models.server.user.UserBoundary;
import com.example.feedforward_courier.models.server.user.UserSession;

import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Repository {
    private static Repository instance;
    private ApiService apiService;

    private Repository() {
        this.apiService = RetrofitClient.getApiService();
    }

    public static Repository getInstance() {
        if (instance == null) {
            instance = new Repository();
        }
        return instance;
    }

    /**
     * This method is used to update a user in the server
     * @param superapp -SUPERAPP
     * @param userEmail  - user email
     * @param userBoundary - user object
     * @param callback - callback to handle the response
     */
    public void updateUser(String superapp, String userEmail, UserBoundary userBoundary, final ApiCallback<Void> callback) {
        Call<Void> call = apiService.updateUser(superapp, userEmail, userBoundary);
        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.isSuccessful()) {
                    callback.onSuccess(null);
                } else {
                    Log.e("ApiRepository", "Error response code: " + response.code());
                    Log.e("ApiRepository", "Error response message: " + response.message());
                    Log.e("ApiRepository", "Error response body: " + response.errorBody());
                    callback.onError("Error: " + response.code());
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Log.e("ApiRepository", "Failure message: " + t.getMessage(), t);
                callback.onError("Failure: " + t.getMessage());
            }
        });
    }

    /**
     * This method is used to create a user in the server
     * @param newUserBoundary - user object
     * @param callback - callback to handle the response
     */
    public void createUser(NewUserBoundary newUserBoundary, final ApiCallback<UserBoundary> callback) {
        Call<UserBoundary> call = apiService.createUser(newUserBoundary);
        call.enqueue(new Callback<UserBoundary>() {
            @Override
            public void onResponse(Call<UserBoundary> call, Response<UserBoundary> response) {
                if (response.isSuccessful()) {
                    callback.onSuccess(response.body());
                } else {
                    Log.e("ApiRepository", "Error response code: " + response.code());
                    Log.e("ApiRepository", "Error response message: " + response.message());
                    Log.e("ApiRepository", "Error response body: " + response.errorBody());
                    callback.onError("Error: " + response.code());
                }
            }

            @Override
            public void onFailure(Call<UserBoundary> call, Throwable t) {
                Log.e("ApiRepository", "Failure message: " + t.getMessage(), t);
                callback.onError("Failure: " + t.getMessage());
            }
        });
    }

    /**
     * This method is used to get a user from the server
     * @param email - user email
     * @param callback - callback to handle the response
     */
    public void getUser(String email, final ApiCallback<UserBoundary> callback) {
        Call<UserBoundary> call = apiService.getUser(UserSession.getInstance().getSUPERAPP(), email);
        call.enqueue(new Callback<UserBoundary>() {
            @Override
            public void onResponse(Call<UserBoundary> call, Response<UserBoundary> response) {
                if (response.isSuccessful()) {
                    callback.onSuccess( response.body());
                } else {
                    callback.onError("Error: " + response.code());
                    Log.d(" DatabaseRepository", "onError: " + response.code());
                }
            }

            @Override
            public void onFailure(Call<UserBoundary> call, Throwable t) {
                callback.onError("Failure: " + t.getMessage());
                Log.d(" DatabaseRepository", "onFailure: " + t.getMessage());
            }
        });
    }

    /**
     * This method is used to create an object in the server
     * @param object - object to be created
     * @param callback - callback to handle the response
     */
    public void createObject(ObjectBoundary object, final ApiCallback<ObjectBoundary> callback) {
        Call<ObjectBoundary> call = apiService.createObject(object);
        call.enqueue(new Callback<ObjectBoundary>() {
            @Override
            public void onResponse(Call<ObjectBoundary> call, Response<ObjectBoundary> response) {
                if (response.isSuccessful()) {
                    ObjectBoundary object = response.body();
                    callback.onSuccess(object);
                    Log.d(" DatabaseRepository", "onResponse: " + object);
                } else {
                    callback.onError("Error: " + response.code());
                    Log.d(" DatabaseRepository", "onError: " + response.code());
                }
            }

            @Override
            public void onFailure(Call<ObjectBoundary> call, Throwable t) {
                callback.onError("Failure: " + t.getMessage());
                Log.d(" DatabaseRepository", "onFailure: " + t.getMessage());
            }
        });
    }

    /**
     * This method is used to get all orders by location
     * @param unit - distance unit
     * @param location - location
     * @param distance - distance
     * @param callback - callback to handle the response
     */
    public void getAllOrdersByCommandAndLocation(DistanceUnit unit, Location location, double distance, ApiCallback<List<ObjectBoundary>> callback) {
        CommandBoundary commandBoundary = new CommandBoundary(CommandOptions.SBRT.toString());
        Map<String, Object> commandMap = Map.of(
                "type", "Order",
                "lat", location.getLat(),
                "lng", location.getLng(),
                "distance", distance,
                "distanceUnit", unit
        );
        commandBoundary.setCommandAttributes(commandMap);
        Call<List<ObjectBoundary>> call = apiService.command(UserSession.getInstance().getSUPERAPP(), commandBoundary);
        getUser(UserSession.getInstance().getUser().getUserId().getEmail(), new ApiCallback<UserBoundary>() {
            @Override
            public void onSuccess(UserBoundary user) {
                user.setRole(RoleEnum.MINIAPP_USER); // Set the role to MINIAPP_USER
                updateUser(UserSession.getInstance().getSUPERAPP(), UserSession.getInstance().getUser().getUserId().getEmail(), user, new ApiCallback<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        call.enqueue(new Callback<List<ObjectBoundary>>() {
                            @Override
                            public void onResponse(Call<List<ObjectBoundary>> call, Response<List<ObjectBoundary>> response) {
                                if (response.isSuccessful()) {
                                    user.setRole(RoleEnum.SUPERAPP_USER); // Set the role back to SUPERAPP_USER
                                    updateUser(UserSession.getInstance().getSUPERAPP(), UserSession.getInstance().getUser().getUserId().getEmail(), user, new ApiCallback<Void>() {
                                        @Override
                                        public void onSuccess(Void unused) {
                                            callback.onSuccess(response.body());
                                            Log.d("DatabaseRepository", "onResponse: GET " + response.body());
                                        }

                                        @Override
                                        public void onError(String error) {
                                            callback.onError("Failed to update user role: " + error);
                                            Log.d("DatabaseRepository", "onError: Failed to update user role: " + error);
                                        }
                                    });
                                } else {
                                    callback.onError("Error: " + response.code());
                                    Log.d("DatabaseRepository", "onError: " + response.code());
                                }
                            }

                            @Override
                            public void onFailure(Call<List<ObjectBoundary>> call, Throwable t) {
                                callback.onError("Failure: " + t.getMessage());
                                Log.d("DatabaseRepository", "onFailure: GET " + t.getMessage());
                            }
                        });
                    }

                    @Override
                    public void onError(String error) {
                        callback.onError("Failed to update user role: " + error);
                        Log.d("DatabaseRepository", "onError: Failed to update user role: " + error);
                    }
                });
            }

            @Override
            public void onError(String error) {
                callback.onError("Error: " + error);
                Log.d("DatabaseRepository", "onError: " + error);
            }
        });
    }


    public void updateObject(ObjectBoundary object, ApiCallback<Void> callback) {
        Call<Void> call = apiService.updateObject(object.getObjectId().getId(), UserSession.getInstance().getSUPERAPP(), UserSession.getInstance().getSUPERAPP(), UserSession.getInstance().getUser().getUserId().getEmail(), object);
        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.isSuccessful()) {
                    Log.d("DatabaseRepository", "onResponse: " + response.body());
                    callback.onSuccess(null);  // Indicate success to the callback
                } else {
                    Log.d("DatabaseRepository", "onError: " + response.code());
                    callback.onError("Error: " + response.code());
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Log.d("DatabaseRepository", "onFailure: " + t.getMessage());
                callback.onError("Failure: " + t.getMessage());
            }
        });
    }



    public void getSpecificObject(String superapp, String id, String userSuperApp, String userEmail, final ApiCallback<ObjectBoundary> callback) {
        Call<ObjectBoundary> call = apiService.getSpecificObject(superapp, id, userSuperApp, userEmail);
        call.enqueue(new Callback<ObjectBoundary>() {
            @Override
            public void onResponse(Call<ObjectBoundary> call, Response<ObjectBoundary> response) {
                if (response.isSuccessful()) {
                    callback.onSuccess(response.body());
                } else {
                    Log.e("ApiRepository", "Error response code: " + response.code());
                    Log.e("ApiRepository", "Error response message: " + response.message());
                    Log.e("ApiRepository", "Error response body: " + response.errorBody());
                    callback.onError("Error: " + response.code());
                }
            }

            @Override
            public void onFailure(Call<ObjectBoundary> call, Throwable t) {
                Log.e("ApiRepository", "Failure message: " + t.getMessage(), t);
                callback.onError("Failure: " + t.getMessage());
            }
        });
    }




}
