package com.example.digital_flake.adapter

import android.content.Context
import android.content.SharedPreferences
import android.graphics.Color
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.digital_flake.R
import com.example.digital_flake.model.Availability

class AvailabilityAdapter(private var slots: List<Availability>, context: Context) : RecyclerView.Adapter<AvailabilityAdapter.AvailabilityViewModel>() {

    private val sharedPreferences: SharedPreferences = context.getSharedPreferences("AvailabilityPrefs", Context.MODE_PRIVATE)
    private var selectedPosition: Int = -1

    companion object {
        private const val TAG = "AvailabilityAdapter"
    }

    class AvailabilityViewModel(view: View) : RecyclerView.ViewHolder(view) {
        val name: TextView = view.findViewById(R.id.tvDesk)  // Assuming you only show name for simplicity
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AvailabilityViewModel {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_slot, parent, false)
        return AvailabilityViewModel(view)
    }

    override fun onBindViewHolder(holder: AvailabilityViewModel, position: Int) {
        val slot = slots[position]
        holder.name.text = slot.workspace_name

        // Set colors based on the availability of the workspace
        if (!slot.workspace_active) {
            holder.itemView.setBackgroundColor(Color.parseColor("#D3D3D3"))
            holder.name.setTextColor(Color.parseColor("#FFFFFF"))
        } else {
            holder.itemView.setBackgroundColor(Color.parseColor("#F0F5FF"))
            holder.name.setTextColor(Color.parseColor("#000000"))
        }

        // If the item is selected, apply a different color
        if (position == selectedPosition) {
            holder.itemView.setBackgroundColor(Color.parseColor("#5167EB"))
            holder.name.setTextColor(Color.parseColor("#FFFFFF"))
            Log.i(TAG, "Selected workspace: ${slot.workspace_name}")
        }

        holder.itemView.setOnClickListener {
            if (slot.workspace_active) { // Only allow selection for unbooked (inactive) desks
                selectedPosition = position // Update the selected position
                notifyDataSetChanged() // Notify the adapter to update the UI

                // Save the selected workspace details in SharedPreferences
                sharedPreferences.edit()
                    .putInt("selected_workspace_id", slot.workspace_id)  // Save workspace id
                    .putString("selected_workspace_name", slot.workspace_name) // Save workspace name
                    .apply() // Using apply() instead of commit() for async operations
                Log.i(TAG, "Workspace ID: ${slot.workspace_id}, Workspace Name: ${slot.workspace_name}")
            }
        }
    }

    override fun getItemCount(): Int = slots.size

    // Method to update the slots list and notify changes
    fun updateSlots(newSlots: List<Availability>) {
        slots = newSlots
        notifyDataSetChanged()
    }
}
