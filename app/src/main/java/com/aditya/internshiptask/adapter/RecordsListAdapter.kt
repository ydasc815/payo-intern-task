package com.aditya.internshiptask.adapter

import android.app.AlertDialog
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.aditya.internshiptask.R
import com.aditya.internshiptask.model.RecordsData
import com.bumptech.glide.Glide

class RecordsListAdapter(c: Context) : RecyclerView.Adapter<RecordsListAdapter.ViewHolder>() {
    private var dataList: ArrayList<RecordsData> = ArrayList()
    private val context: Context = c
    fun setRecordsData(dataList: ArrayList<RecordsData>, sortFlag: Int) {
        when (sortFlag) {
            1 -> dataList.sortBy { it.firstName }
            2 -> dataList.sortBy { it.lastName }
        }
        this.dataList = dataList
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val unitView =
            LayoutInflater.from(parent.context).inflate(R.layout.item_view, parent, false)
        return ViewHolder(unitView)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val currentItem = dataList[position]
        holder.email.text = currentItem.email
        holder.firstName.text = currentItem.firstName
        holder.lastName.text = currentItem.lastName
        Glide.with(context).load(currentItem.imgUrl).placeholder(R.drawable.placeholder)
            .circleCrop().into(holder.profilePhoto)
        holder.itemView.setOnLongClickListener {
            val alertBuilder = AlertDialog.Builder(context)
            alertBuilder.setMessage("Do you want to delete this item ?")
                .setCancelable(false)
                .setPositiveButton("Yes") { _, _ ->
                    dataList.removeAt(position)
                    notifyItemRemoved(position)
                    notifyItemRangeChanged(position, dataList.size)
                    notifyDataSetChanged()
                }
                .setNegativeButton("No") { dialog, _ ->
                    dialog.dismiss()
                }
            val alert = alertBuilder.create()
            alert.show()
            true
        }
    }

    override fun getItemCount(): Int = dataList.size
    class ViewHolder(unitView: View) : RecyclerView.ViewHolder(unitView) {
        val profilePhoto: ImageView = unitView.findViewById(R.id.profile_photo)
        val firstName: TextView = unitView.findViewById(R.id.first_name)
        val lastName: TextView = unitView.findViewById(R.id.last_name)
        val email: TextView = unitView.findViewById(R.id.email)
    }

}