package com.example.feedforward_courier.ui.dashboard;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.feedforward_courier.R;
import com.example.feedforward_courier.adapters.ActiveOrdersAdapter;
import com.example.feedforward_courier.databinding.FragmentDashboardBinding;
import com.example.feedforward_courier.interfacea.ApiCallback;
import com.example.feedforward_courier.models.Order;
import com.example.feedforward_courier.models.OrderStatus;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.Priority;
import com.google.android.material.button.MaterialButtonToggleGroup;

import java.util.ArrayList;
import java.util.List;

public class DashboardFragment extends Fragment {


    private RecyclerView recyclerView;
    private DashboardViewModel dashboardViewModel;
    private ActiveOrdersAdapter adapter;
    private MaterialButtonToggleGroup toggleButtonGroup;
    private static final int LOCATION_PERMISSION_REQUEST_CODE = 1;
    private Location currentLocation = null;
    private FusedLocationProviderClient fusedLocationClient;
    private ActivityResultLauncher<String[]> locationPermissionLauncher;
    private Order orderToLaunch; // Temporary storage to hold order until locationisfetched

    private FragmentDashboardBinding binding;
    private List<Order> allOrders = new ArrayList<>();

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        dashboardViewModel = new ViewModelProvider(this).get(DashboardViewModel.class);

        binding = FragmentDashboardBinding.inflate(inflater, container, false);

        locationPermissionLauncher = registerForActivityResult(
                new ActivityResultContracts.RequestMultiplePermissions(),
                result -> {
                    Boolean fineLocationGranted = result.getOrDefault(Manifest.permission.ACCESS_FINE_LOCATION, false);
                    Boolean coarseLocationGranted = result.getOrDefault(Manifest.permission.ACCESS_COARSE_LOCATION, false);

                    if ((fineLocationGranted != null && fineLocationGranted) || (coarseLocationGranted != null && coarseLocationGranted)) {
                        // Permissions granted, proceed with the task
                        if (orderToLaunch != null) {
                            getLastLocationAndLaunchGoogleMaps(orderToLaunch);
                        }
                    } else {
                        // Permission denied, handle accordingly
                    }
   });

        View root = binding.getRoot();
        findViews();
        initViews();
        setupToggleButtons();

        return root;
    }

    private void initViews() {
        adapter = new ActiveOrdersAdapter(getContext(), new ArrayList<>());
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        dashboardViewModel.getCourierOrders(new ApiCallback<List<Order>>() {
            @Override
            public void onSuccess(List<Order> result) {
                allOrders = result;
                filterOrdersByStatus();
            }

            @Override
            public void onError(String error) {
                // Handle error
            }
        });
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(getContext());
    }

    private void setupToggleButtons() {
        toggleButtonGroup.addOnButtonCheckedListener((group, checkedId, isChecked) -> {
            if (isChecked) {
                List<Order> filteredOrders = new ArrayList<>();
                if (checkedId == R.id.button1) {
                    for (Order order : allOrders) {
                        if (order.getOrderStatus() == OrderStatus.ACTIVE) {
                            filteredOrders.add(order);
                        }
                    }
                } else if (checkedId == R.id.button2) {
                    for (Order order : allOrders) {
                        if (order.getOrderStatus() == OrderStatus.DELIVERED) {
                            filteredOrders.add(order);
                        }
                    }
                }
                adapter.setOrders(filteredOrders);
            }
        });
        toggleButtonGroup.check(R.id.button1); // Ensure one button is always checked
    }

    private void findViews() {
        recyclerView = binding.orderShippingRecyclerView;
        toggleButtonGroup = binding.toggleButton;
    }

    private void filterOrdersByStatus() {
        List<Order> filteredOrders = new ArrayList<>();
        int checkedButtonId = toggleButtonGroup.getCheckedButtonId();

        if (checkedButtonId == R.id.button1) { // Ongoing
            for (Order order : allOrders) {
                if (order.getOrderStatus() == OrderStatus.ACTIVE) {
                    filteredOrders.add(order);
                }
            }
        } else if (checkedButtonId == R.id.button2) { // Finished
            for (Order order : allOrders) {
                if (order.getOrderStatus() == OrderStatus.DELIVERED) {
                    filteredOrders.add(order);
                }
            }
        }
        adapter.setOrders(filteredOrders);
    }

    private void finishOrder(Order order) {
        order.setOrderStatus(OrderStatus.DELIVERED);
        dashboardViewModel.updateOrderStatus(order, new ApiCallback<Void>() {
            @Override
            public void onSuccess(Void result) {
                filterOrdersByStatus(); // Update the list to hide the button
            }

            @Override
            public void onError(String error) {
                // Handle error
            }
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }


    private void checkLocationPermissionAndLaunchGoogleMaps(Order order) {
        if (ContextCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED ||
                ContextCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            // Store order temporarily and request permissions
            orderToLaunch = order;
            locationPermissionLauncher.launch(new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION});
        } else {
            // Permissions are already granted
            getLastLocationAndLaunchGoogleMaps(order);
        }
    }

    private void getLastLocationAndLaunchGoogleMaps(Order order) {
        if (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }

        fusedLocationClient.getCurrentLocation(Priority.PRIORITY_BALANCED_POWER_ACCURACY, null).addOnSuccessListener(getActivity(), location -> {
            if (location != null) {
                currentLocation = location;

                double currentLatitude = currentLocation.getLatitude();
                double currentLongitude = currentLocation.getLongitude();

                String waypoint = String.format("%f,%f", 32.023388, 34.762420);
                String destination = String.format("%f,%f", order.getAssociationLocation().getLat(), order.getAssociationLocation().getLng());
                Log.d("@@@@@@@@@@@@@@@@@@@@2", "getLastLocationAndLaunchGoogleMaps: "+waypoint+" "+destination);
                Uri gmmIntentUri = Uri.parse("https://www.google.com/maps/dir/?api=1" +
                        "&origin=" + currentLatitude + "," + currentLongitude +
                        "&destination=" + destination +
                        "&waypoints=" + waypoint);

                Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
                mapIntent.setPackage("com.google.android.apps.maps");

                if (mapIntent.resolveActivity(getContext().getPackageManager()) != null) {
                    startActivity(mapIntent);
                }
            } else {
                // Handle the case where current location is not available
            }
 });
}
}
