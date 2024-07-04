package com.example.feedforward_courier.interfacea;

public interface ApiCallback<T> {
    void onSuccess(T result);
    void onError(String error);

}
