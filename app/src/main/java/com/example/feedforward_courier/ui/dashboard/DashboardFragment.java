package com.example.feedforward_courier.ui.dashboard;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.RecyclerView;

import com.example.feedforward_courier.adapters.ActiveOrdersAdapter;
import com.example.feedforward_courier.databinding.FragmentDashboardBinding;
import com.example.feedforward_courier.databinding.OrderShippingItemBinding;
import com.example.feedforward_courier.models.Order;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.Priority;
import com.google.android.gms.maps.model.LatLng;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executor;

public class DashboardFragment extends Fragment {
    private FusedLocationProviderClient fusedLocationClient;
    private RecyclerView recyclerView;
    private DashboardViewModel dashboardViewModel;
    private Location currentLocation = null;
    private ActiveOrdersAdapter adapter;


    private FragmentDashboardBinding binding;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        dashboardViewModel =
                new ViewModelProvider(this).get(DashboardViewModel.class);

        binding = FragmentDashboardBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
        findViews();
        initViews();


        return root;
    }

    private void initViews() {
        adapter = new ActiveOrdersAdapter(getContext(), new ArrayList<>());
        recyclerView.setAdapter(adapter);
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(getContext());
        adapter.setActiveOrderCallback(order -> launchGoogleMapsIntent(order));
    }

    private void findViews() {
        recyclerView = binding.orderShippingRecyclerView;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    private void launchGoogleMapsIntent(Order order) {
        if (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            //   return TODO;
        }
        fusedLocationClient.getCurrentLocation(Priority.PRIORITY_BALANCED_POWER_ACCURACY, null).addOnSuccessListener(getActivity(), location -> {
            // Got last known location. In some rare situations this can be null.
            if (location != null) {
                currentLocation = location;
            }
        });
        if (currentLocation != null) {
            double currentLatitude = currentLocation.getLatitude();
            double currentLongitude = currentLocation.getLongitude();

            // Define your waypoint and destination coordinates
            String waypoint = String.format("%f,%f", order.getDonatorLocation().getLatitude(), order.getDonatorLocation().getLongitude());
            String destination = String.format("%f,%f", order.getAssociationLocation().getLatitude(), order.getAssociationLocation().getLongitude());


            // Create the Uri for the Google Maps directions intent
            Uri gmmIntentUri = Uri.parse("https://www.google.com/maps/dir/?api=1" +
                    "&origin=" + currentLatitude + "," + currentLongitude +
                    "&destination=" + destination +
                    "&waypoints=" + waypoint);

            Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
            mapIntent.setPackage("com.google.android.apps.maps");

            // Verify that the intent resolves to an activity
            if (mapIntent.resolveActivity(getContext().getPackageManager()) != null) {
                startActivity(mapIntent);
            } else {
                // Handle the case where no activity can handle the intent
            }
        } else {
            // Handle the case where current location is not available
        }
    }

}




