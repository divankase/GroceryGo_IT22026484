package com.example.grocerygo

//import androidx.core.view.ViewCompat
//import androidx.core.view.WindowInsetsCompat
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.grocerygo.databinding.ActivityAddGroceryBinding

//import com.example.grocerygo.databinding.ActivityFirstViewsBinding

class AddGrocery : AppCompatActivity() {

    private lateinit var binding: ActivityAddGroceryBinding

    private lateinit var db : GroceryDatabaseHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddGroceryBinding.inflate(layoutInflater)
        setContentView(binding.root)
        //enableEdgeToEdge()
        //setContentView(R.layout.activity_add_grocery)
//        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
//            val systemBars=insets.getInsets(WindowInsetsCompat.Type.systemBars())
//            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
//            insets
//        }

        db = GroceryDatabaseHelper(this)
        binding.saveButton.setOnClickListener {
            val title = binding.datePickerEditText.text.toString()
            val content = binding.contentEditText.text.toString()
            val grocery = Grocery(0, title, content, isCompleted = false)

            val isSuccess = db.insertGrocery(grocery)
            if (isSuccess) {
                Toast.makeText(this, "Grocery list saved", Toast.LENGTH_SHORT).show()
                finish()
            } else {
                Toast.makeText(this, "Failed to save grocery list", Toast.LENGTH_SHORT).show()
            }
        }



    }


}