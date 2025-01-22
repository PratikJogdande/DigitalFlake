package com.example.digital_flake.adapter

import android.content.ContentValues.TAG
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
import com.example.digital_flake.model.Booking
import com.example.digital_flake.model.Slot

class SlotAdapter(private var slots: List<Slot>,context: Context) : RecyclerView.Adapter<SlotAdapter.SlotViewHolder>() {
    private val sharedPreferences: SharedPreferences = context.getSharedPreferences("SlotPrefs", Context.MODE_PRIVATE)
   private var selectedPosition: Int = -1
    class SlotViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val name: TextView = view.findViewById(R.id.tvDesk)  // Assuming you only show name for simplicity
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SlotViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_slot, parent, false)
        return SlotViewHolder(view)
    }

    override fun onBindViewHolder(holder: SlotViewHolder, position: Int) {
        val slot = slots[position]
        holder.name.text = slot.slot_name

        if (!slot.slot_active) { // Booked
            holder.itemView.setBackgroundColor(Color.parseColor("#D3D3D3"))
            holder.name.setTextColor(Color.parseColor("#808080"))
        } else {
            holder.itemView.setBackgroundColor(Color.parseColor("#FFFFFF"))
            holder.name.setTextColor(Color.parseColor("#000000"))
        }

        if (position == selectedPosition) {
            holder.itemView.setBackgroundColor(Color.parseColor("#5167EB"))
            holder.name.setTextColor(Color.parseColor("#FFFFFF"))
            Log.i(TAG, "onBindViewHolder22: "+slot.slot_name)

        }

        holder.itemView.setOnClickListener {
            sharedPreferences.edit()
                .putInt("selected_slot_id", slot.slot_id)  // Save position
                .putString("selected_slot_name", slot.slot_name) // Save slot name
                .commit()

            selectedPosition = position
            notifyDataSetChanged()

        }
    }

    override fun getItemCount(): Int = slots.size

    fun updateSlots(newSlots: List<Slot>) {
        slots = newSlots
        notifyDataSetChanged()
    }
}