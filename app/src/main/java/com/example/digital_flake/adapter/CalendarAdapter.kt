package com.example.digital_flake.adapter

import android.content.ContentValues
import android.content.ContentValues.TAG
import android.content.Context
import android.content.SharedPreferences
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.digital_flake.R
import com.example.digital_flake.model.CalendarDateModel
import com.example.digital_flake.model.DateFormat
import com.example.digital_flake.model.DateManager
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class CalendarAdapter(private val listener: (calendarDateModel: CalendarDateModel, position: Int) -> Unit) :
    RecyclerView.Adapter<CalendarAdapter.MyViewHolder>() {
    private val list = ArrayList<CalendarDateModel>()

    inner class MyViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        fun bind(calendarDateModel: CalendarDateModel) {

            val calendarDay = itemView.findViewById<TextView>(R.id.tv_calendar_day)
            val calendarDate = itemView.findViewById<TextView>(R.id.tv_calendar_date)
            val cardView = itemView.findViewById<CardView>(R.id.card_calendar)

            if (calendarDateModel.isSelected) {
                Log.i(ContentValues.TAG, "calendarDay: "+ calendarDateModel.calendarDay+" formated date: "+calendarDateModel.formattedDate+ " calendarDate: "+calendarDateModel.calendarDate+" Year: "+calendarDateModel.data)
            //     calendarDay: Fri formated date: 2025-01-03 calendarDate: 3 Year: Fri Jan 03 11:32:59 GMT+05:30 2025
                // calendarDateModel.formattedDate = 2025-01-03

                val dateFormatIntoNumber = calendarDateModel.formattedDate

                DateFormat.dateFormatIntoNumber = dateFormatIntoNumber

                val inputFormat = SimpleDateFormat("EEE MMM dd HH:mm:ss z yyyy", Locale.ENGLISH)

                val date: Date? = inputFormat.parse(calendarDateModel.data.toString())

                if (date != null) {

                    val outputFormat = SimpleDateFormat("EEE MMM dd", Locale.ENGLISH)

                    val formattedDateChanged = outputFormat.format(date)

                    Log.i(TAG, "bind22222: "+formattedDateChanged)  // Fri Jan 03

                    DateManager.currentDate.formattedDate = formattedDateChanged

                } else {
                    println("Error: Date parsing failed!")
                }

                val sdf = SimpleDateFormat("yyyy-MM-dd")
                Log.i(ContentValues.TAG, "yyyy-MM-dd: "+sdf)

                val calendarDateModel = CalendarDateModel(data = Date()) // Example instantiation


                val dateInstance: Date = calendarDateModel.data

                println("Date from CalendarDateModel: $dateInstance")
                val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
                val formattedDate = dateFormat.format(dateInstance)

                Log.d("FormattedDate", "Formatted Date: $formattedDate")

                calendarDay.setTextColor(
                    ContextCompat.getColor(
                        itemView.context,
                        R.color.white
                    )
                )
                calendarDate.setTextColor(
                    ContextCompat.getColor(
                        itemView.context,
                        R.color.white
                    )
                )
                cardView.setCardBackgroundColor(
                    ContextCompat.getColor(
                        itemView.context,
                        R.color.select
                    )
                )
            } else {
                calendarDay.setTextColor(
                    ContextCompat.getColor(
                        itemView.context,
                        R.color.black
                    )
                )
                calendarDate.setTextColor(
                    ContextCompat.getColor(
                        itemView.context,
                        R.color.black
                    )
                )
                cardView.setCardBackgroundColor(
                    ContextCompat.getColor(
                        itemView.context,
                        R.color.white
                    )
                )
            }

            calendarDay.text = calendarDateModel.calendarDay
            calendarDate.text = calendarDateModel.calendarDate

            cardView.setOnClickListener {
                listener.invoke(calendarDateModel, adapterPosition)
            }
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.row_calendar_date, parent, false)
        return MyViewHolder(view)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.bind(list[position])
    }

    override fun getItemCount(): Int {
        return list.size
    }

    fun setData(calendarList: ArrayList<CalendarDateModel>) {
        list.clear()
        list.addAll(calendarList)
        notifyDataSetChanged()
    }
}
