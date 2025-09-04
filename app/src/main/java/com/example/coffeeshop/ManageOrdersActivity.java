package com.example.coffeeshop;

import android.content.DialogInterface;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;

public class ManageOrdersActivity extends AppCompatActivity {

    private ListView listOrders;
    private DatabaseHelper dbHelper;
    private ArrayList<String> orders;
    private ArrayAdapter<String> adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manage_orders);

        listOrders = findViewById(R.id.listOrders);
        dbHelper = new DatabaseHelper(this);

        loadOrders();

        listOrders.setOnItemClickListener((parent, view, position, id) -> {
            String orderStr = orders.get(position);
            int orderId = Integer.parseInt(orderStr.split("\\.")[0]);

            showOrderOptionsDialog(orderId);
        });
    }

    private void loadOrders() {
        orders = dbHelper.getOrders();
        adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, orders);
        listOrders.setAdapter(adapter);
    }

    private void showOrderOptionsDialog(final int orderId) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Manage Order");
        builder.setItems(new CharSequence[]{"Mark as Delivered", "Mark as Cancelled", "Delete Order", "Close"},
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        switch (which) {
                            case 0: // Delivered
                                dbHelper.updateOrderStatus(orderId, "Delivered");
                                Toast.makeText(ManageOrdersActivity.this, "Order marked as Delivered", Toast.LENGTH_SHORT).show();
                                loadOrders();
                                break;
                            case 1: // Cancelled
                                dbHelper.updateOrderStatus(orderId, "Cancelled");
                                Toast.makeText(ManageOrdersActivity.this, "Order Cancelled", Toast.LENGTH_SHORT).show();
                                loadOrders();
                                break;
                            case 2: // Close
                                dialog.dismiss();
                                break;
                        }
                    }
                });
        builder.show();
    }
}
