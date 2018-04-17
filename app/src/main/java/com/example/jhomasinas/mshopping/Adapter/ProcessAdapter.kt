package com.example.jhomasinas.mshopping.Adapter

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.example.jhomasinas.mshopping.Model.Approved
import com.example.jhomasinas.mshopping.R
import com.squareup.picasso.Picasso

/**
 * Created by JhomAsinas on 4/17/2018.
 */
class ProcessAdapter(val processlist: ArrayList<Approved>): RecyclerView.Adapter<ProcessAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): ViewHolder {
        val v = LayoutInflater.from(parent?.context).inflate(R.layout.list_approved,parent,false)
        return ViewHolder(v)
    }

    override fun getItemCount(): Int {
      return processlist.size
    }

    override fun onBindViewHolder(holder: ViewHolder?, position: Int) {
        val approved: Approved = processlist[position]

        holder?.prodName?.text  = approved.product_name
        holder?.txtAmount?.text = "$ "+approved.product_price+".00"


            if(approved.product_payment == "Cash on Delivery"){
                holder?.iconRemit?.visibility = View.INVISIBLE
                holder?.txtRemit?.visibility  = View.INVISIBLE
            }else{
                holder?.iconCash?.visibility = View.INVISIBLE
                holder?.txtCash?.visibility  = View.INVISIBLE
            }

        Picasso.get()
                .load("http://192.168.1.50/e-commerce/assets/image/"+approved.product_image)
                .resize(350, 350)
                .centerCrop()
                .into(holder?.prodImage)

    }

    class ViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
        val prodName  : TextView  = itemView.findViewById(R.id.approvedName)
        val iconCash  : ImageView = itemView.findViewById(R.id.imgCash)
        val iconRemit : ImageView = itemView.findViewById(R.id.imgRemit)
        val txtCash   : TextView  = itemView.findViewById(R.id.txtCash)
        val txtRemit  : TextView  = itemView.findViewById(R.id.txtRemit)
        val txtAmount : TextView  = itemView.findViewById(R.id.txtAmount)
        val prodImage : ImageView = itemView.findViewById(R.id.imageView8)
    }

}