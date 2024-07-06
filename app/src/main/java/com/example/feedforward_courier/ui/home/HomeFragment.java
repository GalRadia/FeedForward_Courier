package com.example.feedforward_courier.ui.home;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.feedforward_courier.adapters.OrdersAdapter;
import com.example.feedforward_courier.databinding.FragmentHomeBinding;
import com.example.feedforward_courier.interfacea.ApiCallback;
import com.example.feedforward_courier.interfacea.OrderCallback;
import com.example.feedforward_courier.models.Order;
import com.example.feedforward_courier.models.OrderStatus;
import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;

import java.util.ArrayList;
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



    private void findViews() {
        recyclerView = binding.RCVOngoingOrder;
        chipGroup = binding.GRPFilter;
        chipPendning = binding.chipPending;
        chipOngoing = binding.chipOngoing;
        chipfinished = binding.chipFinished;

    }

    private void initViews() {
        adapter = new OrdersAdapter(getContext(),new ArrayList<>());
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        chipGroup.setOnCheckedStateChangeListener((group, checkedIds) -> {
            List<OrderStatus> orderStatuses = new ArrayList<>();

            if (checkedIds.contains(chipPendning.getId())) {
                orderStatuses.add(OrderStatus.PENDING);
            }
            if (checkedIds.contains(chipOngoing.getId())) {
                orderStatuses.add(OrderStatus.ACTIVE);
            }
            if (checkedIds.contains(chipfinished.getId())) {
                orderStatuses.add(OrderStatus.DELIVERED);
            }
            adapter.filterDonationsByStatus(orderStatuses);

        });
        adapter.setOrderCallback(order -> {
            order.setOrderStatus(OrderStatus.ACTIVE);
            homeViewModel.updateOrder(order);
        });

        homeViewModel.getOrders(new ApiCallback<List<Order>>() {
            @Override
            public void onSuccess(List<Order> result) {
                adapter.setDonations(result);
            }

            @Override
            public void onError(String error) {
                Toast.makeText(getContext(), "Cant show orders", Toast.LENGTH_SHORT).show();
            }
        });

    }


}