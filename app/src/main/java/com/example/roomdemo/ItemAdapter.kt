package com.example.roomdemo

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.roomdemo.databinding.ItemsRowBinding

class ItemAdapter(
    private val items: ArrayList<EmployeeEntity>,
    // Update listener waiting to be clicked
    // Unit is void keyword from Java
    private val updateListener: (id: Int) -> Unit,
    // Update listener waiting to be clicked
    private val deleteListener: (id: Int) -> Unit
) : RecyclerView.Adapter<ItemAdapter.ViewHolder>() {

    // ItemsRowBinding is the xml
    class ViewHolder(binding: ItemsRowBinding) : RecyclerView.ViewHolder(binding.root) {

        // Binding xml(items_row) properties
        val llMain = binding.llMain
        val tvName = binding.tvName
        val tvEmail = binding.tvEmail
        val ivEdit = binding.ivEdit
        val ivDelete = binding.ivDelete
    }

    // Return viewHolder object
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            ItemsRowBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val context = holder.itemView.context
        val item = items[position]

        // Assign property to the name/email from EmployeeEntity
        holder.tvName.text = item.name
        holder.tvEmail.text = item.email

        // Get the background color
        if (position % 2 == 0) {
            holder.llMain.setBackgroundColor(
                ContextCompat.getColor(
                    holder.itemView.context,
                    R.color.lightGray
                )
            )
        } else {
            holder.llMain.setBackgroundColor(
                ContextCompat.getColor(
                    holder.itemView.context,
                    R.color.white
                )
            )
        }

        // Handle button clicks (delete, update)
        holder.ivEdit.setOnClickListener {
            updateListener.invoke(item.id)
        }
        holder.ivDelete.setOnClickListener {
            deleteListener.invoke(item.id)
        }
    }

    override fun getItemCount(): Int {
        return items.size
    }

}