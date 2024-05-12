package com.example.grocerygo

import android.content.Context
import android.content.Intent
import android.graphics.Paint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
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
        val checkBox: CheckBox= itemView.findViewById(R.id.checkBox)

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
            holder.checkBox.isChecked=false
            db.close()
            holder.checkBox.isEnabled=true
            Toast.makeText(holder.itemView.context,"Grocery List Deleted",Toast.LENGTH_SHORT).show()
        }

        holder.checkBox.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                // Apply strike-through effect to text views
                holder.titleTextView.paintFlags =
                    holder.titleTextView.paintFlags or Paint.STRIKE_THRU_TEXT_FLAG
                holder.contentTextView.paintFlags =
                    holder.contentTextView.paintFlags or Paint.STRIKE_THRU_TEXT_FLAG
                holder.checkBox.isEnabled=false

                // Show toast message indicating grocery list completion
                Toast.makeText(
                    holder.itemView.context,
                    "Grocery List Completed",
                    Toast.LENGTH_SHORT
                ).show()

                // Show alert dialog to confirm deletion
                val builder = AlertDialog.Builder(holder.itemView.context)
                builder.setTitle("Delete Grocery Item")
                    .setMessage("Are you sure you want to delete this item?")
                    .setPositiveButton("Yes") { _, _ ->
                        // Delete item from database and refresh RecyclerView
                        db.deleteGrocery(grocery.id)
                        refreshData(db.getAllGroceries())
                        holder.checkBox.isChecked=false
                        db.close()
                        holder.checkBox.isEnabled=true
                    }
                    .setNegativeButton("No") { dialog, _ ->
                        // Dismiss the dialog if "No" is clicked
                        dialog.dismiss()
                        Toast.makeText(
                            holder.itemView.context,
                            "Automatically delete within 1 min",
                            Toast.LENGTH_SHORT
                            ).show()
                        android.os.Handler().postDelayed({
                            db.deleteGrocery(grocery.id)
                            Toast.makeText(
                                holder.itemView.context,
                                "Deleted ${grocery.title} list",
                                Toast.LENGTH_SHORT
                            ).show()
                            refreshData(db.getAllGroceries())
                            holder.checkBox.isChecked=false
                            db.close()
                            holder.checkBox.isEnabled=true
                        }, 6000)
                    }
                    .create()
                    .show()
            } else {
                // Remove strike-through effect when checkbox is unchecked
                holder.titleTextView.paintFlags =
                    holder.titleTextView.paintFlags and Paint.STRIKE_THRU_TEXT_FLAG.inv()
                holder.contentTextView.paintFlags =
                    holder.contentTextView.paintFlags and Paint.STRIKE_THRU_TEXT_FLAG.inv()
            }
        }


    }

    fun refreshData(newGrocery:List<Grocery>){
        grocery = newGrocery
        notifyDataSetChanged()
    }


}