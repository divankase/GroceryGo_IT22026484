package com.example.grocerygo

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView

class GroceryAdapter (private var grocery: List<Grocery>,context: Context):
    RecyclerView.Adapter<GroceryAdapter.GroceryViewHolder>() {

        private val db: GroceryDatabaseHelper = GroceryDatabaseHelper(context)

    class GroceryViewHolder(itemView: View): RecyclerView.ViewHolder(itemView){
        val titleTextView:TextView=itemView.findViewById(R.id.titleTextView)
        val contentTextView:TextView=itemView.findViewById(R.id.contentTextView)
        val updateButton:ImageView=itemView.findViewById(R.id.updateButton)
        val viewButton:ImageView=itemView.findViewById(R.id.viewButton)
        val deleteButton:ImageView=itemView.findViewById(R.id.deleteButton)

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GroceryViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.grocery_item,parent,false)
        return GroceryViewHolder(view)
    }

    override fun getItemCount(): Int =grocery.size

    override fun onBindViewHolder(holder: GroceryViewHolder, position: Int) {
        val grocery = grocery[position]
        holder.titleTextView.text=grocery.title
        holder.contentTextView.text=grocery.content

        holder.updateButton.setOnClickListener{
            val intent = Intent(holder.itemView.context,UpdateGroceryActivity::class.java).apply {
                putExtra("grocery_id",grocery.id)
            }
            holder.itemView.context.startActivity(intent)
        }

        holder.viewButton.setOnClickListener{
            val intent = Intent(holder.itemView.context,GroceryList::class.java).apply {
                putExtra("grocery_id",grocery.id)
            }
            holder.itemView.context.startActivity(intent)
        }

        holder.deleteButton.setOnClickListener{
            db.deleteGrocery(grocery.id)
            refreshData(db.getAllGroceries())
            Toast.makeText(holder.itemView.context,"Grocery List Deleted",Toast.LENGTH_SHORT).show()
        }


    }

    fun refreshData(newGrocery:List<Grocery>){
        grocery = newGrocery
        notifyDataSetChanged()
    }


}