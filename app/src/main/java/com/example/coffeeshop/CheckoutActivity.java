package com.example.coffeeshop;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class CheckoutActivity extends AppCompatActivity {

    private EditText etName, etAddress, etPhone;
    private Button btnPlaceOrder;

    private DatabaseHelper dbHelper;
    private double totalPrice = 0.0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_checkout);

        etName = findViewById(R.id.etName);
        etAddress = findViewById(R.id.etAddress);
        etPhone = findViewById(R.id.etPhone);
        btnPlaceOrder = findViewById(R.id.btnPlaceOrder);

        dbHelper = new DatabaseHelper(this);

        if (getIntent() != null) {
            totalPrice = getIntent().getDoubleExtra("totalPrice", 0.0);
        }

        btnPlaceOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = etName.getText().toString().trim();
                String address = etAddress.getText().toString().trim();
                String phone = etPhone.getText().toString().trim();

                if (TextUtils.isEmpty(name) || TextUtils.isEmpty(address) || TextUtils.isEmpty(phone)) {
                    Toast.makeText(CheckoutActivity.this, "Please fill all details", Toast.LENGTH_SHORT).show();
                } else {
                    long orderId = dbHelper.insertOrder(name, address, phone, totalPrice);

                    if (orderId != -1) {
                        Toast.makeText(CheckoutActivity.this, "Order saved. Proceed to Payment", Toast.LENGTH_SHORT).show();

                        // Redirect to PaymentActivity
                        Intent intent = new Intent(CheckoutActivity.this, PaymentActivity.class);
                        intent.putExtra("orderId", orderId);
                        intent.putExtra("customerName", name);
                        intent.putExtra("customerAddress", address);
                        intent.putExtra("customerPhone", phone);
                        intent.putExtra("totalPrice", totalPrice);
                        startActivity(intent);
                        finish();
                    } else {
                        Toast.makeText(CheckoutActivity.this, "Failed to save order", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
    }
}
