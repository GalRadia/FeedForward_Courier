package com.example.feedforward_courier.interfacea;

import com.example.feedforward_courier.models.Order;

public interface ActiveOrderCallback {
    void onStartNavigation(Order order);
    void onFinishOrder(Order order);
}
