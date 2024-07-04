package com.example.feedforward_courier.interfacea;

import com.example.feedforward_courier.models.Order;

public interface OrderCallback {
    void onStartOrder(Order order);
}
