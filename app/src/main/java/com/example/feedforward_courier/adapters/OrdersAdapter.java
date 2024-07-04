package com.example.feedforward_courier.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.feedforward_courier.R;
import com.example.feedforward_courier.databinding.OrderItemBinding;
import com.example.feedforward_courier.interfacea.OrderCallback;
import com.example.feedforward_courier.models.Food;
import com.example.feedforward_courier.models.Order;
import com.example.feedforward_courier.models.OrderStatus;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textview.MaterialTextView;

import java.util.ArrayList;
import java.util.List;

public class OrdersAdapter extends RecyclerView.Adapter<OrdersAdapter.OrderViewHolder> {
    private Context context;
    private OrderItemBinding binding;
    private List<Order> originalOrders;
    private List<Order> filteredOrders;
    private List<OrderStatus> currentFilterStatuses;
    private OrderCallback orderCallback;

    public OrdersAdapter(Context context, List<Order> donations) {
        this.context = context;
        this.originalOrders = donations != null ? donations : new ArrayList<>();
        this.filteredOrders = new ArrayList<>(this.originalOrders);
        this.currentFilterStatuses = new ArrayList<>();

    }

    public void setOrderCallback(OrderCallback orderCallback) {
        this.orderCallback = orderCallback;
    }

    @NonNull
    @Override
    public OrderViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        binding = binding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
        return new OrderViewHolder(binding);
    }



    @Override
    public void onBindViewHolder(@NonNull OrderViewHolder holder, int position) {
        Order order = filteredOrders.get(position);
        holder.restaurantName.setText(order.getDonatorName());
        holder.restaurantLocation.setText(order.getDonatorLocation().toString());
        switch (order.getOrderStatus()) {
            case PENDING:
                holder.statusButton.setText("Pending");
                holder.statusButton.setIcon(context.getDrawable(R.drawable.ic_pending));
                break;
            case DELIVERED:
                holder.statusButton.setText("Delivered\nWrite a review");
                holder.statusButton.setIcon(context.getDrawable(R.drawable.ic_done));
                break;
            case ACTIVE:
                holder.statusButton.setText("Active");
                holder.statusButton.setIcon(context.getDrawable(R.drawable.ic_ongoing_shipment));
                break;

        }
        StringBuilder items = new StringBuilder();
        for (Food food : order.getFoods()) {
            items.append(food.getName()).append(", ");
        }
        if (items.charAt(items.length() - 1) == ' ')
            items.deleteCharAt(items.length() - 2);
        holder.foodItems.setText(items);//TODO: Implement a way to show the food items
        holder.donationDate.setText(order.getOrderDate());
        holder.donationTime.setText(order.getOrderTime());
        if(order.getOrderStatus() == OrderStatus.PENDING){
            holder.startButton.setVisibility(View.VISIBLE);
            holder.startButton.setOnClickListener(v -> {
                if (orderCallback != null) {
                    orderCallback.onStartOrder(order);
                }
            });
        }
    }

    public void setDonations(List<Order> orders) {
        this.originalOrders = orders != null ? orders : new ArrayList<>();
        filterDonationsByStatus(this.currentFilterStatuses);
    }





    public void filterDonationsByStatus(List<OrderStatus> statuses) {
        this.currentFilterStatuses = statuses;
        if (statuses.isEmpty()) {
            filteredOrders = new ArrayList<>(originalOrders);
        } else {
            filteredOrders = new ArrayList<>();
            for (Order order : originalOrders) {
                if (statuses.contains(order.getOrderStatus())) {
                    filteredOrders.add(order);
                }
            }
        }
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        if (filteredOrders != null) {
            return filteredOrders.size();
        }
        return 0;
    }
    public class OrderViewHolder extends RecyclerView.ViewHolder {
        MaterialTextView associationName;
        MaterialTextView associationLocation;
        MaterialTextView foodItems;
        MaterialButton statusButton;
        MaterialButton startButton;
        MaterialTextView donationDate;
        MaterialTextView donationTime;
        MaterialTextView restaurantName;
        MaterialTextView restaurantLocation;

        public OrderViewHolder(@NonNull OrderItemBinding binding) {
            super(binding.getRoot());
            associationName = binding.TXTAssociationName;
            associationLocation = binding.TXTAssociationLocation;
            foodItems = binding.TXTOrderDetails;
            statusButton = binding.BTNOrderStatus;
            donationDate = binding.TXTDate;
            donationTime = binding.TXTTime;
            restaurantName = binding.TXTRestaurantName;
            restaurantLocation = binding.TXTRestaurantLocation;
            startButton = binding.BTNStartOrder;

        }
    }





}
