package com.example.feedforward_courier.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.feedforward_courier.databinding.OrderShippingItemBinding;
import com.example.feedforward_courier.interfacea.OrderCallback;
import com.example.feedforward_courier.models.Food;
import com.example.feedforward_courier.models.Order;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.textview.MaterialTextView;

import java.util.List;

public class ActiveOrdersAdapter extends RecyclerView.Adapter<ActiveOrdersAdapter.ActiveOrdersViewHolder> {
    Context context;
    OrderShippingItemBinding binding;
    private List<Order> orderList;
    private OrderCallback activeOrderCallback;
   public ActiveOrdersAdapter(Context context, List<Order> orders) {
        this.context = context;
        this.orderList = orders;
    }
    public void setActiveOrderCallback(OrderCallback activeOrderCallback) {
        this.activeOrderCallback = activeOrderCallback;
    }

    @NonNull
    @Override
    public ActiveOrdersViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        binding = OrderShippingItemBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
        return new ActiveOrdersViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull ActiveOrdersViewHolder holder, int position) {
        Order order = orderList.get(position);
        holder.restaurantName.setText(order.getDonatorName());
        holder.restaurantLocation.setText(order.getDonatorAddress());
        holder.associationLocation.setText(order.getAssociationAddress());
        StringBuilder items = new StringBuilder();
        for (Food food : order.getFoods()) {
            items.append(food.getName()).append(", ");
        }
        if (items.charAt(items.length() - 1) == ' ')
            items.deleteCharAt(items.length() - 2);
        holder.foodItems.setText(items);//TODO: Implement a way to show the food items
        holder.donationDate.setText(order.getOrderDate());
        holder.donationTime.setText(order.getOrderTime());
        holder.statusButton.setOnClickListener(v -> activeOrderCallback.onStartOrder(order));


    }

    public void setOrders(List<Order> orders) {
        this.orderList = orders;
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        if (orderList != null)
            return orderList.size();
        return 0;
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
        }
    }
}
