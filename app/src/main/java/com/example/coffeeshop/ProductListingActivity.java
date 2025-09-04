package com.example.coffeeshop;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;

public class ProductListingActivity extends AppCompatActivity {

    private ListView listProducts;

    String[] products = {"Espresso", "Cappuccino", "Latte", "Mocha", "Americano" , "Flat White" , "Iced Coffee" ,"Iced Latte", "Cortado" , "Vienna" , "Mazagran" , "Hot Choclate" , "Dark Roast", "Nitro Cold Brew", "Vanila Creme"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_listing);

        listProducts = findViewById(R.id.listProducts);

        ArrayAdapter<String> adapter = new ArrayAdapter<>(
                this,
                android.R.layout.simple_list_item_1,
                products
        );
        listProducts.setAdapter(adapter);

        listProducts.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String selectedProduct = products[position];

                Intent intent = new Intent(ProductListingActivity.this, ProductDetailsActivity.class);
                intent.putExtra("productName", selectedProduct);
                startActivity(intent);
            }
        });
    }
}