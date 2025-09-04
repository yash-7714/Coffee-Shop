package com.example.coffeeshop;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

public class OrderConfirmationActivity extends AppCompatActivity {

    private Button btnBackToHome;
    private long orderId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_confirmation);

        btnBackToHome = findViewById(R.id.btnBackToHome);

        orderId = getIntent().getLongExtra("orderId", -1);

        btnBackToHome.setOnClickListener(v -> {
            Intent intent = new Intent(OrderConfirmationActivity.this, ProductListingActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
            finish();
        });
    }
}
