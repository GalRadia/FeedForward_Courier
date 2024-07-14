package com.example.feedforward_courier.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.feedforward_courier.databinding.OrderShippingItemBinding;
import com.example.feedforward_courier.interfacea.ActiveOrderCallback;
import com.example.feedforward_courier.interfacea.OrderCallback;
import com.example.feedforward_courier.models.Food;
import com.example.feedforward_courier.models.Order;
import com.example.feedforward_courier.models.OrderStatus;
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.textview.MaterialTextView;

import java.util.List;

public class ActiveOrdersAdapter extends RecyclerView.Adapter<ActiveOrdersAdapter.ActiveOrdersViewHolder> {
    Context context;
    private List<Order> orderList;
    private ActiveOrderCallback activeOrderCallback;

    public ActiveOrdersAdapter(Context context, List<Order> orders) {
        this.context = context;
        this.orderList = orders;
    }

    public void setActiveOrderCallback(ActiveOrderCallback activeOrderCallback) {
        this.activeOrderCallback = activeOrderCallback;
    }

    @NonNull
    @Override
    public ActiveOrdersViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        OrderShippingItemBinding binding = OrderShippingItemBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
        return new ActiveOrdersViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull ActiveOrdersViewHolder holder, int position) {
        Order order = orderList.get(position);
        holder.restaurantLocation.setText(order.getDonatorAddress());
        holder.associationLocation.setText(order.getAssociationAddress());
        StringBuilder items = new StringBuilder();
        for (Food food : order.getFoods()) {
            items.append(food.getName()).append(", ");
        }
        if (items.length() > 0 && items.charAt(items.length() - 1) == ' ')
            items.deleteCharAt(items.length() - 2);
        holder.foodItems.setText(items);
        holder.donationDate.setText(order.getOrderDate());
        holder.donationTime.setText(order.getOrderTime());
        holder.finishButton.setOnClickListener(v -> {
            order.setOrderStatus(OrderStatus.DELIVERED);
            activeOrderCallback.onFinishOrder(order);
            holder.finishButton.setVisibility(View.GONE);
            notifyDataSetChanged();
            notifyItemRemoved(position);
        });
        if (order.getOrderStatus() == OrderStatus.ACTIVE) {
            holder.finishButton.setVisibility(View.VISIBLE);

        } else {
            holder.finishButton.setVisibility(View.GONE);
        }

        holder.statusButton.setOnClickListener(v -> activeOrderCallback.onStartNavigation(order));
    }

    public void setOrders(List<Order> orders) {
        this.orderList = orders;
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return orderList != null ? orderList.size() : 0;
    }

    public class ActiveOrdersViewHolder extends RecyclerView.ViewHolder {
        MaterialTextView associationName;
        MaterialTextView associationLocation;
        MaterialTextView foodItems;
        FloatingActionButton statusButton;
        MaterialTextView donationDate;
        MaterialTextView donationTime;
        MaterialTextView restaurantName;
        MaterialTextView restaurantLocation;
        ExtendedFloatingActionButton finishButton;

        public ActiveOrdersViewHolder(OrderShippingItemBinding binding) {
            super(binding.getRoot());
            associationName = binding.TXTAssociationName;
            associationLocation = binding.TXTAssociationLocation;
            foodItems = binding.TXTOrderDetails;
            statusButton = binding.BTNOrderStart;
            donationDate = binding.TXTDate;
            donationTime = binding.TXTTime;
            restaurantName = binding.TXTRestaurantName;
            restaurantLocation = binding.TXTRestaurantLocation;
            finishButton = binding.BTNPCKFinish;
        }
    }
}
