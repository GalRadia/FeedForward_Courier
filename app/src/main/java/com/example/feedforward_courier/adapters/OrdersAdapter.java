package com.example.feedforward_courier.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.feedforward_courier.databinding.OrderItemBinding;
import com.example.feedforward_courier.interfacea.OrderCallback;
import com.example.feedforward_courier.models.Food;
import com.example.feedforward_courier.models.Order;
import com.example.feedforward_courier.models.OrderStatus;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton;
import com.google.android.material.textview.MaterialTextView;

import java.util.ArrayList;
import java.util.List;

public class OrdersAdapter extends RecyclerView.Adapter<OrdersAdapter.OrderViewHolder> {
    private Context context;
    private OrderItemBinding binding;
    private List<Order> orders;
    private OrderCallback orderCallback;

    public OrdersAdapter(Context context, List<Order> donations) {
        this.context = context;
        this.orders = donations != null ? donations : new ArrayList<>();

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
        Order order = orders.get(position);
        holder.associationName.setText(order.getAssociationName());
        holder.restaurantName.setText(order.getDonatorName());
        holder.restaurantLocation.setText(order.getDonatorAddress());
        holder.associationLocation.setText(order.getAssociationAddress());

        StringBuilder items = new StringBuilder();
        for (Food food : order.getFoods()) {
            items.append(food.getName() + " x" + food.getAmount()).append("\n");
        }
        if (items.charAt(items.length() - 1) == ' ')
            items.deleteCharAt(items.length() - 2);
        holder.foodItems.setText(items);//TODO: Implement a way to show the food items
        holder.donationDate.setText(order.getOrderDate());
        holder.donationTime.setText(order.getOrderTime());
        holder.startButton.setOnClickListener(v -> {
            if (orderCallback != null) {
                orderCallback.onStartOrder(order);
                notifyDataSetChanged();
                notifyItemRemoved(position);
            }
        });
    }

    public void setDonations(List<Order> orders) {
        this.orders = orders;
        notifyDataSetChanged(); // Notify the adapter that the data has changed
    }


    @Override
    public int getItemCount() {
        if (orders != null) {
            return orders.size();
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
        ExtendedFloatingActionButton finishButton;

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
