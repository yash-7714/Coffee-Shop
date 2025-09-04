package com.example.coffeeshop;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;

public class CartActivity extends AppCompatActivity {

    private ListView listCartItems;
    private TextView tvTotalPrice;
    private Button btnCheckout;

    private ArrayList<CartItem> cartItems;
    private CartAdapter cartAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);

        listCartItems = findViewById(R.id.listCartItems);
        tvTotalPrice = findViewById(R.id.tvTotalPrice);
        btnCheckout = findViewById(R.id.btnCheckout);

        cartItems = new ArrayList<>();
        cartItems.add(new CartItem("Latte", 2, 180.0));

        cartAdapter = new CartAdapter(this, cartItems, new CartAdapter.CartListener() {
            @Override
            public void onCartUpdated() {
                updateTotalPrice();
            }
        });
        listCartItems.setAdapter(cartAdapter);

        // Checkout button
        btnCheckout.setOnClickListener(v -> {
            Intent intent = new Intent(CartActivity.this, CheckoutActivity.class);
            intent.putExtra("cartItems", cartItems); // send list
            startActivity(intent);
        });


        updateTotalPrice();
    }

    private void updateTotalPrice() {
        double total = 0;
        for (CartItem item : cartItems) {
            total += item.getQuantity() * item.getPrice();
        }
        tvTotalPrice.setText("Total: ₹" + String.format("%.2f", total));
    }
}
