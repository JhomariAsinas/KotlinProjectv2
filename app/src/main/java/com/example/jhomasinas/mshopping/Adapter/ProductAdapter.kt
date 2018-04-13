package com.example.jhomasinas.mshopping.Adapter

import android.support.v4.app.FragmentActivity
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import com.example.jhomasinas.mshopping.Model.Product
import com.example.jhomasinas.mshopping.R
import com.squareup.picasso.Picasso

/**
 * Created by JhomAsinas on 4/10/2018.
 */

class ProductAdapter(val prodList: ArrayList<Product>, val delegate: Delegate) : RecyclerView.Adapter<ProductAdapter.ViewHolder>(){

    interface Delegate{
        fun onClickProduct(product:Product)
        fun onClickImage(product:Product)
    }

    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): ViewHolder {
        val v = LayoutInflater.from(parent?.context).inflate(R.layout.list_product,parent,false)
        return ViewHolder(v)
    }

    override fun getItemCount(): Int {
       return prodList.size
    }

    override fun onBindViewHolder(holder: ViewHolder?, position: Int) {
      val product: Product = prodList[position]

        holder?.prodName?.text = product.product_name
        holder?.prodPrice?.text = "$${product.product_price}.00"
        Picasso.get()
                .load("http://192.168.1.50/e-commerce/assets/image/"+product.product_image)
                .resize(350, 350)
                .centerCrop()
                .into(holder?.prodImg)

        holder?.btnView?.setOnClickListener(object : View.OnClickListener {
            override fun onClick(p0: View?) {
                delegate.onClickProduct(prodList.get(position))
            }
        })

        holder?.prodImg?.setOnClickListener(object : View.OnClickListener {
            override fun onClick(p0: View?) {
                delegate.onClickImage(prodList.get(position))
            }
        })


    }

    class ViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
        val prodName  = itemView.findViewById<TextView>(R.id.prodName)
        val prodImg   = itemView.findViewById<ImageView>(R.id.prodImg)
        val prodPrice = itemView.findViewById<TextView>(R.id.prodPrice)
        val btnView   = itemView.findViewById<Button>(R.id.btnView)
    }

}
