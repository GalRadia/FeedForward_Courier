package com.example.feedforward_courier.ui.dashboard;

import androidx.lifecycle.ViewModel;

import com.example.feedforward_courier.interfacea.ApiCallback;
import com.example.feedforward_courier.models.Order;
import com.example.feedforward_courier.models.server.object.ObjectBoundary;
import com.example.feedforward_courier.models.server.user.UserSession;
import com.example.feedforward_courier.utils.Repository;

import java.util.List;

public class DashboardViewModel extends ViewModel {
    Repository repository;

    public DashboardViewModel() {
        repository = Repository.getInstance();
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

            }
        });

    }



}