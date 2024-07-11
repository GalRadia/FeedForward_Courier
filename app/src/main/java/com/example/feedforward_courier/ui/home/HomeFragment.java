package com.example.feedforward_courier.ui.home;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.feedforward_courier.adapters.OrdersAdapter;
import com.example.feedforward_courier.databinding.FragmentHomeBinding;
import com.example.feedforward_courier.interfacea.ApiCallback;
import com.example.feedforward_courier.models.Courier;
import com.example.feedforward_courier.models.Order;
import com.example.feedforward_courier.models.OrderStatus;
import com.example.feedforward_courier.models.server.object.Location;
import com.example.feedforward_courier.models.server.user.UserSession;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.button.MaterialButtonToggleGroup;

import java.util.ArrayList;
import java.util.List;

public class HomeFragment extends Fragment {

    private FragmentHomeBinding binding;
    private HomeViewModel homeViewModel;
    private RecyclerView recyclerView;
    private OrdersAdapter adapter;
    private MaterialButtonToggleGroup toggleGroup;
    private MaterialButton KM5, KM15, KM30;
    private FusedLocationProviderClient fusedLocationClient;
    private ActivityResultLauncher<String[]> locationPermissionLauncher;
    private Location currentLocation;
    private double currentDistance = 5;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(requireActivity());
        locationPermissionLauncher = registerForActivityResult(
                new ActivityResultContracts.RequestMultiplePermissions(),
                result -> {
                    Boolean fineLocationGranted = result.getOrDefault(Manifest.permission.ACCESS_FINE_LOCATION, false);
                    Boolean coarseLocationGranted = result.getOrDefault(Manifest.permission.ACCESS_COARSE_LOCATION, false);
                    if (fineLocationGranted != null && fineLocationGranted) {
                        getCurrentLocation();
                    } else if (coarseLocationGranted != null && coarseLocationGranted) {
                        getCurrentLocation();
                    } else {
                        Log.e("HomeFragment", "Location permission not granted");
                    }
                }
        );
        homeViewModel = new ViewModelProvider(this).get(HomeViewModel.class);

        binding = FragmentHomeBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
        requestLocationPermissions();
        findViews();
        initViews();
        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    private void findViews() {
        recyclerView = binding.RCVOngoingOrder;
        toggleGroup = binding.buttonGroup;
        KM5 = binding.KM5BTN;
        KM15 = binding.KM15BTN;
        KM30 = binding.KM30BTN;
    }

    private void initViews() {
        adapter = new OrdersAdapter(getContext(), new ArrayList<>());
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        adapter.setOrderCallback(order -> {
            order.setOrderStatus(OrderStatus.ACTIVE);
            homeViewModel.updateOrder(order, new ApiCallback<Void>() {
                @Override
                public void onSuccess(Void result) {
                    // Handle success
                    Toast.makeText(getContext(),"Order Updated",Toast.LENGTH_LONG);
                    Courier courier = UserSession.getInstance().getCourier();
                    courier.getAllOrders().add(order.getOrderID().getId());
                    homeViewModel.updateCourier(courier, new ApiCallback<Void>() {
                        @Override
                        public void onSuccess(Void result) {
                            loadOrdersByLocation(currentDistance);
                        }

                        @Override
                        public void onError(String error) {

                        }
                    });

                }

                @Override
                public void onError(String error) {
                    // Handle error
                }
            });
        });
        loadOrdersByLocation(currentDistance);
        toggleGroup.addOnButtonCheckedListener((group, checkedId, isChecked) -> {
            if (isChecked) {
                if (checkedId == KM5.getId()) {
                    currentDistance = 5;
                } else if (checkedId == KM15.getId()) {
                    currentDistance = 15;
                } else if (checkedId == KM30.getId()) {
                    currentDistance = 30;
                }
                loadOrdersByLocation(currentDistance);
            }
        });
    }

    private void loadOrdersByLocation(double distance) {
        if (currentLocation != null) {
            homeViewModel.getPendingOrdersByLocation(distance, new ApiCallback<List<Order>>() {
                @Override
                public void onSuccess(List<Order> orders) {
                    Toast.makeText(getContext(),"THERE IS LOCATION!!", Toast.LENGTH_LONG);
                    adapter.setDonations(orders);
                }

                @Override
                public void onError(String error) {
                    Toast.makeText(getContext(), "Error", Toast.LENGTH_SHORT).show();
                    Log.d("HomeFragment", "onError: " + error);
                }
            });
        } else {
            Toast.makeText(getContext(), "Location isn't available", Toast.LENGTH_SHORT).show();
        }
    }

    private void requestLocationPermissions() {
        if (ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            locationPermissionLauncher.launch(new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION});
        } else {
            getCurrentLocation();
        }
    }

    @SuppressLint("MissingPermission")
    private void getCurrentLocation() {
        fusedLocationClient.getLastLocation().addOnSuccessListener(location -> {
            if (location != null) {
                currentLocation = new Location(location.getLatitude(), location.getLongitude());
                homeViewModel.setCurrentLocation(currentLocation);
                loadOrdersByLocation(5);  // Load initial orders with default distance
            } else {
                Log.e("HomeFragment", "Location is null");
            }
        });
    }
}
