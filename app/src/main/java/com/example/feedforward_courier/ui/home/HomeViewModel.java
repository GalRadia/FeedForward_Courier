package com.example.feedforward_courier.ui.home;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.feedforward_courier.models.Order;
import com.example.feedforward_courier.models.server.DistanceUnit;
import com.example.feedforward_courier.models.server.object.Location;
import com.example.feedforward_courier.utils.Repository;

import java.util.List;

public class HomeViewModel extends ViewModel {

    private final MutableLiveData<String> mText;
    private LiveData<List<Order>> ordersLiveData;
    private Repository repository;

    public HomeViewModel() {
        repository = Repository.getInstance();
        mText = new MutableLiveData<>();
        mText.setValue("This is home fragment");
        ordersLiveData = repository.getOrdersLiveData();

    }

    public LiveData<String> getText() {
        return mText;
    }
    public LiveData<List<Order>> getOrders() {
        return ordersLiveData;
    }

    public void fetchAllOrders(DistanceUnit unit, Location location, double distance) {
        repository.getAllOrdersByCommandAndLocationLiveData(unit, location, distance);
    }
    public void updateOrder(Order order){
        repository.updateObject(order.convert(order));
    }
}