package com.example.feedforward_courier.ui.dashboard;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.net.Uri;
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

import com.example.feedforward_courier.R;
import com.example.feedforward_courier.adapters.ActiveOrdersAdapter;
import com.example.feedforward_courier.databinding.FragmentDashboardBinding;
import com.example.feedforward_courier.interfacea.ActiveOrderCallback;
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
    private FusedLocationProviderClient fusedLocationClient;
    private ActivityResultLauncher<String[]> locationPermissionLauncher;
    private Order orderToLaunch; // Temporary storage to hold order until location is fetched
    private Location currentLocation;

    private FragmentDashboardBinding binding;
    private List<Order> allOrders = new ArrayList<>();

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        dashboardViewModel = new ViewModelProvider(this).get(DashboardViewModel.class);
        binding = FragmentDashboardBinding.inflate(inflater, container, false);

        // Initialize the FusedLocationProviderClient
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(requireContext());

        locationPermissionLauncher = registerForActivityResult(
                new ActivityResultContracts.RequestMultiplePermissions(),
                result -> {
                    Boolean fineLocationGranted = result.getOrDefault(Manifest.permission.ACCESS_FINE_LOCATION, false);
                    Boolean coarseLocationGranted = result.getOrDefault(Manifest.permission.ACCESS_COARSE_LOCATION, false);

                    if ((fineLocationGranted != null && fineLocationGranted) || (coarseLocationGranted != null && coarseLocationGranted)) {
                        // Permissions granted, proceed with the task
                        if (orderToLaunch != null) {
                            getLastLocationAndLaunchGoogleMaps(orderToLaunch);
                        } else {
                            getLastLocation();
                        }
                    } else {
                        // Permission denied, handle accordingly
                        Log.e("DashboardFragment", "Location permission not granted");
                    }
                });

        View root = binding.getRoot();
        findViews();
        initViews();
        setupToggleButtons();
        requestLocationPermissions();

        return root;
    }

    private void findViews() {
        recyclerView = binding.orderShippingRecyclerView;
        toggleButtonGroup = binding.toggleButton;
    }

    private void initViews() {
        adapter = new ActiveOrdersAdapter(getContext(), new ArrayList<>());
        adapter.setActiveOrderCallback(new ActiveOrderCallback() {
            @Override
            public void onStartNavigation(Order order) {
                checkLocationPermissionAndLaunchGoogleMaps(order);
            }

            @Override
            public void onFinishOrder(Order order) {
                // Implement the logic for finishing the order if needed
                order.setOrderStatus(OrderStatus.DELIVERED);
                dashboardViewModel.updateOrderStatus(order, new ApiCallback<Void>() {
                    @Override
                    public void onSuccess(Void result) {
                        Toast.makeText(getContext(), getString(R.string.order_updated),Toast.LENGTH_LONG);
                        filterOrdersByStatus();
                    }

                    @Override
                    public void onError(String error) {
                        Toast.makeText(getContext(), getString(R.string.order_couldnt_be_updated),Toast.LENGTH_LONG);

                    }
                });
            }
        });
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
                Log.e("DashboardFragment", "Error fetching orders: " + error);
            }
        });
    }

    private void setupToggleButtons() {
        toggleButtonGroup.addOnButtonCheckedListener((group, checkedId, isChecked) -> {
            if (isChecked) {
                filterOrdersByStatus();
            }
        });
        toggleButtonGroup.check(R.id.button1); // Ensure one button is always checked
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

    private void requestLocationPermissions() {
        if (ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED ||
                ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            locationPermissionLauncher.launch(new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION});
        } else {
            getLastLocation();
        }
    }

    @SuppressLint("MissingPermission")
    private void getLastLocation() {
        fusedLocationClient.getCurrentLocation(Priority.PRIORITY_BALANCED_POWER_ACCURACY, null).addOnSuccessListener(location -> {
            if (location != null) {
                currentLocation = location;
                Log.d("DashboardFragment", "Current location: " + currentLocation.getLatitude() + ", " + currentLocation.getLongitude());
                // If there was an order waiting for location, launch Google Maps
                if (orderToLaunch != null) {
                    launchGoogleMaps(orderToLaunch);
                    orderToLaunch = null; // Reset the temporary storage
                }
            } else {
                Log.e("DashboardFragment", "Current location is null");
            }
        });
    }

    private void checkLocationPermissionAndLaunchGoogleMaps(Order order) {
        orderToLaunch = order; // Store the order to be launched
        if (ContextCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED ||
                ContextCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            locationPermissionLauncher.launch(new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION});
        } else {
            // Permissions are already granted
            getLastLocationAndLaunchGoogleMaps(order);
        }
    }

    @SuppressLint("MissingPermission")
    private void getLastLocationAndLaunchGoogleMaps(Order order) {
        fusedLocationClient.getCurrentLocation(Priority.PRIORITY_BALANCED_POWER_ACCURACY, null).addOnSuccessListener(location -> {
            if (location != null) {
                currentLocation = location;
                launchGoogleMaps(order);
            } else {
                Log.e("DashboardFragment", "Current location is null");
            }
        });
    }

    private void launchGoogleMaps(Order order) {
        double currentLatitude = currentLocation.getLatitude();
        double currentLongitude = currentLocation.getLongitude();

        String waypoint = String.format("%f,%f", order.getDonatorLocation().getLat(), order.getDonatorLocation().getLng());
        String destination = String.format("%f,%f", order.getAssociationLocation().getLat(), order.getAssociationLocation().getLng());
        Log.d("DashboardFragment", "getLastLocationAndLaunchGoogleMaps: " + waypoint + " " + destination);
        Uri gmmIntentUri = Uri.parse("https://www.google.com/maps/dir/?api=1" +
                "&origin=" + currentLatitude + "," + currentLongitude +
                "&destination=" + destination +
                "&waypoints=" + waypoint);

        Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
        mapIntent.setPackage("com.google.android.apps.maps");

        if (mapIntent.resolveActivity(getContext().getPackageManager()) != null) {
            startActivity(mapIntent);
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
