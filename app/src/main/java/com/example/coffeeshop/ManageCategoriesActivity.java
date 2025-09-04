package com.example.coffeeshop;

import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;

public class ManageCategoriesActivity extends AppCompatActivity {

    private Button btnAddCategory;
    private ListView listCategories;

    private ArrayList<String> categories;
    private ArrayAdapter<String> adapter;

    private DatabaseHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manage_categories);

        btnAddCategory = findViewById(R.id.btnAddCategory);
        listCategories = findViewById(R.id.listCategories);
        dbHelper = new DatabaseHelper(this);

        loadCategories();

        btnAddCategory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showAddCategoryDialog();
            }
        });

        listCategories.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                showUpdateDeleteDialog(position);
            }
        });
    }

    private void loadCategories() {
        categories = dbHelper.getCategories();   // Now from DB, not ArrayList
        adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, categories);
        listCategories.setAdapter(adapter);
    }

    private void showAddCategoryDialog() {
        final EditText input = new EditText(this);
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Add New Category");
        builder.setView(input);

        builder.setPositiveButton("Add", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String categoryName = input.getText().toString().trim();
                if (!categoryName.isEmpty()) {
                    dbHelper.addCategory(categoryName);   // Insert into DB
                    loadCategories(); // Refresh list
                    Toast.makeText(ManageCategoriesActivity.this, "Category Added", Toast.LENGTH_SHORT).show();
                }
            }
        });

        builder.setNegativeButton("Cancel", null);
        builder.show();
    }

    private void showUpdateDeleteDialog(final int position) {
        final String currentCategory = categories.get(position);

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Manage Category");
        builder.setMessage("What do you want to do with \"" + currentCategory + "\"?");

        builder.setPositiveButton("Update", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                showUpdateCategoryDialog(position);
            }
        });

        builder.setNegativeButton("Delete", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // Parse ID from string (format: "1. Hot Coffee")
                String item = categories.get(position);
                int id = Integer.parseInt(item.split("\\.")[0]);
                dbHelper.deleteCategory(id);  // Delete from DB
                loadCategories();
                Toast.makeText(ManageCategoriesActivity.this, "Category Deleted", Toast.LENGTH_SHORT).show();
            }
        });

        builder.setNeutralButton("Cancel", null);
        builder.show();
    }

    private void showUpdateCategoryDialog(final int position) {
        final EditText input = new EditText(this);
        input.setText(categories.get(position).split("\\. ")[1]); // Extract name only

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Update Category");
        builder.setView(input);

        builder.setPositiveButton("Update", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String newName = input.getText().toString().trim();
                if (!newName.isEmpty()) {
                    // Parse ID from string
                    String item = categories.get(position);
                    int id = Integer.parseInt(item.split("\\.")[0]);

                    dbHelper.updateCategory(id, newName); // Update DB
                    loadCategories();
                    Toast.makeText(ManageCategoriesActivity.this, "Category Updated", Toast.LENGTH_SHORT).show();
                }
            }
        });

        builder.setNegativeButton("Cancel", null);
        builder.show();
    }
}
