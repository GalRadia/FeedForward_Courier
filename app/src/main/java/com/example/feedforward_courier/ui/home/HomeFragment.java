package com.example.feedforward_courier.ui.home;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.RecyclerView;

import com.example.feedforward_courier.adapters.OrdersAdapter;
import com.example.feedforward_courier.databinding.FragmentHomeBinding;
import com.example.feedforward_courier.models.Order;
import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;

import java.util.List;

public class HomeFragment extends Fragment {

    private FragmentHomeBinding binding;
    private HomeViewModel homeViewModel;
    private RecyclerView recyclerView;
    private OrdersAdapter adapter;
    private ChipGroup chipGroup;
    private Chip chipPendning, chipOngoing, chipfinished;



    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        homeViewModel =
                new ViewModelProvider(this).get(HomeViewModel.class);

        binding = FragmentHomeBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
        findViews();
        initViews();
        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    private void updateUI(List<Order> orders) {
        // Update the UI
    }

    private void findViews() {
        recyclerView = binding.RCVOngoingOrder;
        chipGroup = binding.GRPFilter;
        chipPendning = binding.chipPending;
        chipOngoing = binding.chipOngoing;
        chipfinished = binding.chipFinished;

    }

    private void initViews() {

    }

    private void observeData() {
        homeViewModel.getOrders().observe(this, orders -> updateUI(orders));
    }
}