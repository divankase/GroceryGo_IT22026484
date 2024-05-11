package com.example.grocerygo

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.grocerygo.databinding.ActivityGroceryListBinding

class GroceryList : AppCompatActivity() {

    private lateinit var binding: ActivityGroceryListBinding
    private lateinit var db:GroceryDatabaseHelper
    private var groceryId: Int = -1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//        enableEdgeToEdge()
//        setContentView(R.layout.activity_update_grocery)
        binding=ActivityGroceryListBinding.inflate(layoutInflater)
        setContentView(binding.root)

        db = GroceryDatabaseHelper(this)

        groceryId = intent.getIntExtra("grocery_id",-1)
        if(groceryId==-1){
            finish()
            return
        }

        val grocery = db.getGroceryByID(groceryId)
        binding.updateTitleEditText.setText(grocery.title)
        binding.updateContentEditText.setText(grocery.content)

    }
}