package com.example.coffeeshop;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class PaymentActivity extends AppCompatActivity {

    private RadioGroup radioGroupPayment;
    private LinearLayout layoutUpi, layoutCard;
    private EditText etUpiId, etCardNumber, etExpiry, etCvv;
    private Button btnProceedPayment;

    private DatabaseHelper dbHelper;
    private long orderId;
    private double totalPrice;
    private String customerName, customerAddress, customerPhone;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment);

        radioGroupPayment = findViewById(R.id.radioGroupPayment);
        layoutUpi = findViewById(R.id.layoutUpi);
        layoutCard = findViewById(R.id.layoutCard);
        etUpiId = findViewById(R.id.etUpiId);
        etCardNumber = findViewById(R.id.etCardNumber);
        etExpiry = findViewById(R.id.etExpiry);
        etCvv = findViewById(R.id.etCvv);
        btnProceedPayment = findViewById(R.id.btnProceedPayment);

        dbHelper = new DatabaseHelper(this);

        orderId = getIntent().getLongExtra("orderId", -1);
        totalPrice = getIntent().getDoubleExtra("totalPrice", 0.0);
        customerName = getIntent().getStringExtra("customerName");
        customerAddress = getIntent().getStringExtra("customerAddress");
        customerPhone = getIntent().getStringExtra("customerPhone");

        radioGroupPayment.setOnCheckedChangeListener((group, checkedId) -> {
            if (checkedId == R.id.radioUpi) {
                layoutUpi.setVisibility(View.VISIBLE);
                layoutCard.setVisibility(View.GONE);
            } else if (checkedId == R.id.radioCard) {
                layoutCard.setVisibility(View.VISIBLE);
                layoutUpi.setVisibility(View.GONE);
            } else {
                layoutUpi.setVisibility(View.GONE);
                layoutCard.setVisibility(View.GONE);
            }
        });

        btnProceedPayment.setOnClickListener(v -> {
            int selectedId = radioGroupPayment.getCheckedRadioButtonId();
            if (selectedId == -1) {
                Toast.makeText(PaymentActivity.this, "Please select a payment method", Toast.LENGTH_SHORT).show();
                return;
            }

            dbHelper.updateOrderStatus(orderId, "Paid");

            // Go to confirmation
            Intent intent = new Intent(PaymentActivity.this, OrderConfirmationActivity.class);
            intent.putExtra("orderId", orderId);
            startActivity(intent);
            finish();
        });
    }
}
