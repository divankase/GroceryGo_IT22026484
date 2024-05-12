package com.example.grocerygo

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.grocerygo.databinding.ActivityFirstViewsBinding

class FirstViews : AppCompatActivity() {

    private lateinit var binding: ActivityFirstViewsBinding
    private lateinit var db: GroceryDatabaseHelper
    private lateinit var groceryAdapter: GroceryAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityFirstViewsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        db = GroceryDatabaseHelper(this)

        groceryAdapter = GroceryAdapter(db.getAllGroceries(), this)

        binding.groceryRecyclerView.layoutManager = LinearLayoutManager(this)
        binding.groceryRecyclerView.adapter = groceryAdapter

        // Set click listener for addButton
        binding.addButton.setOnClickListener {
            val intent = Intent(this, AddGrocery::class.java)
            startActivity(intent)
        }

}

    override fun onResume() {
        super.onResume()
        groceryAdapter.refreshData(db.getAllGroceries())
    }
}