package com.example.feedforward_courier.utils;

import android.util.Log;

import com.example.feedforward_courier.interfacea.ApiCallback;
import com.example.feedforward_courier.interfacea.ApiService;
import com.example.feedforward_courier.models.Order;
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

    public void getAllOrders(String userSuperApp, String userEmail, int size, int page, ApiCallback<List<ObjectBoundary>> callback) {
        apiService.getAllObjctsByType("Order", userSuperApp, userEmail, size, page).enqueue(new Callback<List<ObjectBoundary>>() {
            @Override
            public void onResponse(Call<List<ObjectBoundary>> call, Response<List<ObjectBoundary>> response) {
                if (response.isSuccessful()) {
                    callback.onSuccess(response.body());
                    Log.d("DatabaseRepository", "onResponse: GET " );
                }
            }

            @Override
            public void onFailure(Call<List<ObjectBoundary>> call, Throwable t) {
                // Handle failure
                Log.d("DatabaseRepository", "onFailure: GET " + t.getMessage());
                callback.onError(t.getMessage());
            }
        });
    }

    public void updateUser(UserBoundary user) {
        Call<Void> call = apiService.updateUser(user.getUserId().getSuperapp(), user.getUserId().getEmail(), user);
        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.isSuccessful()) {
                    Log.d(" DatabaseRepository", "onResponse: " + response.body());
                } else {
                    Log.d(" DatabaseRepository", "onError: " + response.code());
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Log.d(" DatabaseRepository", "onFailure: " + t.getMessage());
            }
        });
    }

    public void createUser(String email, String userName, String avatar, final ApiCallback<UserBoundary> callback) {
        NewUserBoundary user = new NewUserBoundary(email, RoleEnum.SUPERAPP_USER, userName, avatar);
        Call<UserBoundary> call = apiService.createUser(user);
        call.enqueue(new Callback<UserBoundary>() {
            @Override
            public void onResponse(Call<UserBoundary> call, Response<UserBoundary> response) {
                if (response.isSuccessful()) {
                    UserBoundary user = response.body();
                    UserSession.getInstance().setUserEmail(email);
                    callback.onSuccess(user);
                    Log.d("DatabaseRepository", "onResponse: " + user);
                } else {
                    callback.onError("Error: " + response.code());
                    Log.e("DatabaseRepository", "onError: " + response.code());
                }
            }

            @Override
            public void onFailure(Call<UserBoundary> call, Throwable t) {
                callback.onError("Failure: " + t.getMessage());
                Log.e("DatabaseRepository", "onFailure: " + t.getMessage());
            }
        });
    }

    public void getUser(String email, final ApiCallback<UserBoundary> callback) {
        Call<UserBoundary> call = apiService.getUser("2024b.gal.said", email);
        call.enqueue(new Callback<UserBoundary>() {
            @Override
            public void onResponse(Call<UserBoundary> call, Response<UserBoundary> response) {
                if (response.isSuccessful()) {
                    UserBoundary user = response.body();
                    callback.onSuccess(user);
                    Log.d(" DatabaseRepository", "onResponse: " + user);
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

    public void getAllOrdersByCommandAndLocation(DistanceUnit unit, Location location, double distance, ApiCallback<List<Order>> callback) {
        CommandBoundary commandBoundary = new CommandBoundary("SBRT");
        Map<String, Object> commandMap = Map.of(
                "type", "Order",
                "lat", location.getLat(),
                "lng",location.getLng(),
                "distance", distance,
                "distanceUnit", unit
                );
        commandBoundary.setCommandAttributes(commandMap);
        Call<List<ObjectBoundary>> call = apiService.command(UserSession.getInstance().getSUPERAPP(), commandBoundary);
        call.enqueue(new Callback<List<ObjectBoundary>>() {
            @Override
            public void onResponse(Call<List<ObjectBoundary>> call, Response<List<ObjectBoundary>> response) {
                if (response.isSuccessful()) {
                    List<Order> orders = Order.convertObjectBoundaryList(response.body());
                    callback.onSuccess(orders);
                    Log.d("DatabaseRepository", "onResponse: GET " + orders);
                }
            }

            @Override
            public void onFailure(Call<List<ObjectBoundary>> call, Throwable t) {
                // Handle failure
                Log.d("DatabaseRepository", "onFailure: GET " + t.getMessage());
            }
        });

    }

    public void updateObject(ObjectBoundary object){
        Call<Void> call = apiService.updateObject(object.getObjectId().getId(), object.getObjectId().getSuperapp(), UserSession.getInstance().getSUPERAPP(), UserSession.getInstance().getUserEmail(), object);
        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.isSuccessful()) {
                    Log.d(" DatabaseRepository", "onResponse: " + response.body());
                } else {
                    Log.d(" DatabaseRepository", "onError: " + response.code());
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Log.d(" DatabaseRepository", "onFailure: " + t.getMessage());
            }
        });
    }
}


