package com.example.feedforward_courier.ui.home;

import android.util.Log;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.feedforward_courier.interfacea.ApiCallback;
import com.example.feedforward_courier.models.Order;
import com.example.feedforward_courier.models.OrderStatus;
import com.example.feedforward_courier.models.server.DistanceUnit;
import com.example.feedforward_courier.models.server.object.Location;
import com.example.feedforward_courier.models.server.object.ObjectBoundary;
import com.example.feedforward_courier.models.server.user.UserSession;
import com.example.feedforward_courier.utils.Repository;

import java.util.List;

public class HomeViewModel extends ViewModel {

    private final MutableLiveData<String> mText;
    private Repository repository;
    private MutableLiveData<Location> currentLocation = new MutableLiveData<>();


    public HomeViewModel() {
        repository = Repository.getInstance();
        mText = new MutableLiveData<>();
        mText.setValue("This is home fragment");


    }

    public void getOrders(ApiCallback<List<Order>> callback){
        repository.getAllOrders(UserSession.getInstance().getSUPERAPP(), UserSession.getInstance().getUserEmail(), 50, 0, new ApiCallback<List<ObjectBoundary>>() {
            @Override
            public void onSuccess(List<ObjectBoundary> result) {
                List<Order> orders = Order.convertObjectBoundaryList(result);
                callback.onSuccess(orders);
            }

            @Override
            public void onError(String error) {
                Log.e("HomeViewModel", "getOrders: " + error);
            }
        });

    }



    public void updateOrder(Order order){
        ObjectBoundary objectBoundary = order.convert(order);
        repository.updateObject(objectBoundary);
    }

    public void setCurrentLocation(Location location) {
        currentLocation.setValue(location);

    }
    public void getPendingOrdersByLocation(double distance, ApiCallback<List<Order>> callback) {
        repository.getAllOrdersByCommandAndLocation(DistanceUnit.KILOMETERS, currentLocation.getValue(), distance, new ApiCallback<List<ObjectBoundary>>() {
            @Override
            public void onSuccess(List<ObjectBoundary> result) {
                List<Order> orders = Order.convertObjectBoundaryList(result);
                for (int i = 0; i < orders.size(); i++) {
                    if (orders.get(i).getOrderStatus() != OrderStatus.PENDING) {
                        orders.remove(i);
                    }
                }
                callback.onSuccess(orders);
            }

            @Override
            public void onError(String error) {
                callback.onError(error);
            }
        });
    }
}