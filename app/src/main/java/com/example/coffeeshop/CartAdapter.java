package com.example.coffeeshop;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;

import java.util.ArrayList;

public class CartAdapter extends BaseAdapter {

    private Context context;
    private ArrayList<CartItem> cartItems;
    private CartListener listener;

    public interface CartListener {
        void onCartUpdated();
    }

    public CartAdapter(Context context, ArrayList<CartItem> cartItems, CartListener listener) {
        this.context = context;
        this.cartItems = cartItems;
        this.listener = listener;
    }

    @Override
    public int getCount() {
        return cartItems.size();
    }

    @Override
    public Object getItem(int position) {
        return cartItems.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.cart_item, parent, false);
        }

        TextView tvName = convertView.findViewById(R.id.tvCartItemName);
        TextView tvQuantity = convertView.findViewById(R.id.tvQuantity);
        Button btnDecrease = convertView.findViewById(R.id.btnDecrease);
        Button btnIncrease = convertView.findViewById(R.id.btnIncrease);
        Button btnRemove = convertView.findViewById(R.id.btnRemove);

        CartItem item = cartItems.get(position);

        tvName.setText(item.getName() + " - ₹" + item.getPrice());
        tvQuantity.setText(String.valueOf(item.getQuantity()));

        btnDecrease.setOnClickListener(v -> {
            if (item.getQuantity() > 1) {
                item.setQuantity(item.getQuantity() - 1);
                notifyDataSetChanged();
                listener.onCartUpdated();
            }
        });

        btnIncrease.setOnClickListener(v -> {
            item.setQuantity(item.getQuantity() + 1);
            notifyDataSetChanged();
            listener.onCartUpdated();
        });

        btnRemove.setOnClickListener(v -> {
            cartItems.remove(position);
            notifyDataSetChanged();
            listener.onCartUpdated();
        });

        return convertView;
    }
}
