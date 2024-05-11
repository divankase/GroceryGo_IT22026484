package com.example.grocerygo

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.grocerygo.databinding.ActivityUpdateGroceryBinding

class UpdateGroceryActivity : AppCompatActivity() {

    private lateinit var binding:ActivityUpdateGroceryBinding
    private lateinit var db:GroceryDatabaseHelper
    private var groceryId: Int = -1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//        enableEdgeToEdge()
//        setContentView(R.layout.activity_update_grocery)
        binding=ActivityUpdateGroceryBinding.inflate(layoutInflater)
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

        binding.updateSaveButton.setOnClickListener{
            val newTitle = binding.updateTitleEditText.text.toString()
            val newContent = binding.updateContentEditText.text.toString()

            val updatedGrocery = Grocery(groceryId,newTitle,newContent)
            db.updateGrocery(updatedGrocery)
            finish()
            Toast.makeText(this,"Grocery List Updated", Toast.LENGTH_SHORT).show()
        }
    }
}