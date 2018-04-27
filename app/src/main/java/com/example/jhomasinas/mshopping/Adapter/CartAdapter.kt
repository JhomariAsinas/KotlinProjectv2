package com.example.jhomasinas.mshopping.Adapter

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.PopupMenu
import android.widget.TextView
import com.example.jhomasinas.mshopping.Model.Cart
import com.example.jhomasinas.mshopping.Model.Product
import com.example.jhomasinas.mshopping.R
import com.squareup.picasso.Picasso

/**
 * Created by JhomAsinas on 4/12/2018.
 */
class CartAdapter(val cartList: ArrayList<Cart>, val delegate: Delegate, val context: Context) : RecyclerView.Adapter<CartAdapter.ViewHolder>(){

    interface Delegate{
        fun onDeleteCart  (cart:Cart)
        fun onTransactCart(cart:Cart)
        fun onEditCart    (cart:Cart)
    }

    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): ViewHolder {
       val v = LayoutInflater.from(parent?.context).inflate(R.layout.list_cart,parent,false)
        return ViewHolder(v)
    }

    override fun getItemCount(): Int {
        return cartList.size
    }

    override fun onBindViewHolder(holder: ViewHolder?, position: Int) {
        val cart: Cart = cartList[position]

        holder?.prodName?.text  = cart.product_name
        holder?.prodPrice?.text = "$"+cart.product_price+".00"
        holder?.prodItems?.text = cart.product_items+" Items"

        Picasso.get()
                .load("http://192.168.1.17/e-commerce/assets/image/"+cart.product_image)
                .resize(350, 350)
                .centerCrop()
                .into(holder?.prodImg)

        holder?.btnCheck?.setOnClickListener {
            delegate.onTransactCart(cartList.get(position))
        }

        holder?.txtOption?.setOnClickListener {
            val popup = PopupMenu(context,holder.txtOption)
            popup.inflate(R.menu.menu_options)

           popup.setOnMenuItemClickListener { menuItem ->
                when(menuItem.itemId){
                    R.id.removeMenu -> {
                        delegate.onDeleteCart(cartList.get(position))
                        return@setOnMenuItemClickListener true
                    }
                    R.id.editMenu -> {
                        delegate.onEditCart(cartList.get(position))
                        return@setOnMenuItemClickListener true
                    }
                    else -> true
                }

            }


            popup.show()
        }

    }

    class ViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
        val prodName    = itemView.findViewById<TextView>(R.id.cartName)
        val prodImg     = itemView.findViewById<ImageView>(R.id.cartImage)
        val prodPrice   = itemView.findViewById<TextView>(R.id.cartPrice)
        val prodItems   = itemView.findViewById<TextView>(R.id.cartItems)
        val btnCheck    = itemView.findViewById<Button>(R.id.btnTransact)
        val txtOption   = itemView.findViewById<TextView>(R.id.textViewOptions);
    }
}