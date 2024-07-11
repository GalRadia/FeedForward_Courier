package com.example.feedforward_courier.ui.dashboard;

import androidx.lifecycle.ViewModel;

import com.example.feedforward_courier.interfacea.ApiCallback;
import com.example.feedforward_courier.models.Courier;
import com.example.feedforward_courier.models.Order;
import com.example.feedforward_courier.models.server.object.ObjectBoundary;
import com.example.feedforward_courier.models.server.user.UserSession;
import com.example.feedforward_courier.utils.Repository;

import java.util.ArrayList;
import java.util.List;

public class DashboardViewModel extends ViewModel {
    Repository repository;

    public DashboardViewModel() {
        repository = Repository.getInstance();
    }

    public void getCourierOrders(ApiCallback<List<Order>> callback) {
        Courier courier = UserSession.getInstance().getCourier();
        ArrayList<String> orderIds = courier.getAllOrders();
        if (orderIds.isEmpty())
            callback.onError("EMPTY");
        List<Order> orders = new ArrayList<>();

        for (String orderId : orderIds) {
            repository.getSpecificObject("2024b.gal.said", orderId, UserSession.getInstance().getSUPERAPP(), UserSession.getInstance().getUser().getUserId().getEmail(), new ApiCallback<ObjectBoundary>() {
                @Override
                public void onSuccess(ObjectBoundary orderObjectBoundary) {
                    Order order = new Order(orderObjectBoundary);
                    orders.add(order);

                    // Check if all orders have been fetched
                    if (orders.size() == orderIds.size()) {
                        callback.onSuccess(orders);
                    }
                }

                @Override
                public void onError(String error) {

                }


            });
        }


    }


    public void updateOrderStatus(Order order, ApiCallback<Void> callback) {
        ObjectBoundary objectBoundary = order.convert(order, UserSession.getInstance().getUser().getUserId().getEmail());
        repository.updateObject(objectBoundary, new ApiCallback<Void>() {
            @Override
            public void onSuccess(Void result) {
                callback.onSuccess(null);
            }

            @Override
            public void onError(String error) {
                callback.onError(error);
            }
        });
    }
}