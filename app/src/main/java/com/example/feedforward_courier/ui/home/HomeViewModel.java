package com.example.feedforward_courier.ui.home;

import android.util.Log;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.feedforward_courier.interfacea.ApiCallback;
import com.example.feedforward_courier.models.Courier;
import com.example.feedforward_courier.models.Order;
import com.example.feedforward_courier.models.OrderStatus;
import com.example.feedforward_courier.models.server.DistanceUnit;
import com.example.feedforward_courier.models.server.object.Location;
import com.example.feedforward_courier.models.server.object.ObjectBoundary;
import com.example.feedforward_courier.models.server.user.UserSession;
import com.example.feedforward_courier.utils.Repository;

import java.util.ArrayList;
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


    public void updateOrder(Order order, ApiCallback<Void> callback) {
        ObjectBoundary objectBoundary = order.convert(order, order.getDonatorEmail());
        repository.updateObject(objectBoundary, callback);
    }

    public void setCurrentLocation(Location location) {
        currentLocation.setValue(location);

    }

    public void getPendingOrdersByLocation(double distance, ApiCallback<List<Order>> callback) {
        repository.getAllOrdersByCommandAndLocation(DistanceUnit.KILOMETERS, currentLocation.getValue(), distance, new ApiCallback<List<ObjectBoundary>>() {
            @Override
            public void onSuccess(List<ObjectBoundary> result) {
                List<Order> orders = Order.convertObjectBoundaryList(result);
                List<Order> filteredOrders = new ArrayList<>();
                for (Order order :
                        orders) {
                    if (order.getOrderStatus() == OrderStatus.PENDING)
                        filteredOrders.add(order);
                }
                callback.onSuccess(filteredOrders);
            }

            @Override
            public void onError(String error) {
                callback.onError(error);
            }
        });
    }

    public void updateCourier(Courier courier, ApiCallback<Void> callback) {
        repository.updateObject(courier.toObjectBoundary(), callback);
    }
}