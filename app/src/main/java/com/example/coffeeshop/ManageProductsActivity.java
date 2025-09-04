package com.example.coffeeshop;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;

public class ManageProductsActivity extends AppCompatActivity {

    private Button btnAddProduct;
    private ListView listProducts;

    private ArrayList<String> products;
    private ArrayAdapter<String> adapter;

    private DatabaseHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manage_products);

        btnAddProduct = findViewById(R.id.btnAddProduct);
        listProducts = findViewById(R.id.listProducts);
        dbHelper = new DatabaseHelper(this);

        loadProducts();

        btnAddProduct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ManageProductsActivity.this, AddProductActivity.class);
                startActivityForResult(intent, 100);
            }
        });

        listProducts.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {
                showUpdateDeleteDialog(position);
            }
        });
    }

    private void loadProducts() {
        products = dbHelper.getProducts();
        adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, products);
        listProducts.setAdapter(adapter);
    }

    private void showUpdateDeleteDialog(final int position) {
        String productStr = products.get(position);

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Manage Product");
        builder.setMessage("What do you want to do with \"" + productStr + "\"?");

        builder.setPositiveButton("Update", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Intent intent = new Intent(ManageProductsActivity.this, AddProductActivity.class);
                intent.putExtra("productName", productStr);
                intent.putExtra("position", position);
                startActivityForResult(intent, 101);
            }
        });

        builder.setNegativeButton("Delete", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                int productId = Integer.parseInt(productStr.split("\\.")[0]);
                dbHelper.deleteProduct(productId);
                loadProducts();
                Toast.makeText(ManageProductsActivity.this, "Product Deleted", Toast.LENGTH_SHORT).show();
            }
        });

        builder.setNeutralButton("Cancel", null);
        builder.show();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK && data != null) {
            String product = data.getStringExtra("product");

            if (requestCode == 100) { // Add new
                loadProducts(); // Refresh from DB
                Toast.makeText(this, "Product Added", Toast.LENGTH_SHORT).show();
            } else if (requestCode == 101) { // Update existing
                int position = data.getIntExtra("position", -1);
                if (position != -1) {
                    loadProducts(); // Refresh from DB
                    Toast.makeText(this, "Product Updated", Toast.LENGTH_SHORT).show();
                }
            }
        }
    }
}
