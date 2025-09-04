package com.example.coffeeshop;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class AddProductActivity extends AppCompatActivity {

    private Spinner spinnerCategories;
    private EditText etProductName, etProductDescription, etProductPrice, etProductImageUrl;
    private Button btnSaveProduct;

    private DatabaseHelper dbHelper;
    private int editProductId = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_product);

        spinnerCategories = findViewById(R.id.spinnerCategories);
        etProductName = findViewById(R.id.etProductName);
        etProductDescription = findViewById(R.id.etProductDescription);
        etProductPrice = findViewById(R.id.etProductPrice);
        etProductImageUrl = findViewById(R.id.etImageUrl);
        btnSaveProduct = findViewById(R.id.btnSaveProduct);

        dbHelper = new DatabaseHelper(this);

        if (getIntent().hasExtra("productId")) {
            editProductId = getIntent().getIntExtra("productId", -1);
            String productName = getIntent().getStringExtra("productName");
            String productPrice = getIntent().getStringExtra("productPrice");
            String productCategory = getIntent().getStringExtra("productCategory");
            String productImage = getIntent().getStringExtra("productImage");

            etProductName.setText(productName);
            etProductPrice.setText(productPrice);
            etProductImageUrl.setText(productImage);

            btnSaveProduct.setText("Update Product");
        }

        btnSaveProduct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String category = spinnerCategories.getSelectedItem().toString();
                String name = etProductName.getText().toString().trim();
                String description = etProductDescription.getText().toString().trim();
                String price = etProductPrice.getText().toString().trim();
                String imageUrl = etProductImageUrl.getText().toString().trim();

                if (name.isEmpty() || price.isEmpty()) {
                    Toast.makeText(AddProductActivity.this, "Please enter name and price", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (editProductId == -1) {
                    dbHelper.addProduct(name, price, category, imageUrl);
                    Toast.makeText(AddProductActivity.this, "Product Added", Toast.LENGTH_SHORT).show();
                } else {
                    dbHelper.updateProduct(editProductId, name, price, category, imageUrl);
                    Toast.makeText(AddProductActivity.this, "Product Updated", Toast.LENGTH_SHORT).show();
                }

                finish();
            }
        });
    }
}
