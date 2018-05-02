@file:Suppress("DEPRECATION")

package com.example.jhomasinas.mshopping.Activity

import android.app.Activity
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.v4.view.MenuItemCompat
import android.support.v7.app.AlertDialog
import android.support.v7.widget.Toolbar
import android.util.Log
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import com.example.jhomasinas.mshopping.Config.ProductApi
import com.example.jhomasinas.mshopping.Config.ProductResponse
import com.example.jhomasinas.mshopping.Config.SharedPref
import com.example.jhomasinas.mshopping.R
import com.squareup.picasso.Picasso
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_product_detail.*
import me.pushy.sdk.Pushy
import me.pushy.sdk.util.exceptions.PushyException
import org.jetbrains.anko.intentFor
import org.jetbrains.anko.support.v4.toast
import org.jetbrains.anko.toast

class ProductDetail : AppCompatActivity() {

    val productApiserve by lazy {
        ProductApi.create()
    }
    var disposable = CompositeDisposable()

    var code:    String? = null
    var prodimg: String? = null
    var items1:  String? = null
    var view: View?      = null
    var bool: Boolean?   = null
    var textCartItem: TextView? = null



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_product_detail)

        val toolbar = findViewById<Toolbar>(R.id.prodToolbar)
        view = findViewById(R.id.viewSnack)
        setSupportActionBar(toolbar)
        supportActionBar?.title = "Product Detail"
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowHomeEnabled(true)

        prodName2.text     = intent.getStringExtra("Name")
        prodDes.text       = intent.getStringExtra("Descrip")
        prodCategory.text  = intent.getStringExtra("Category")
        code               = intent.getStringExtra("Code")
        items1             = intent.getStringExtra("Items")
        prodPrice2.text    = "$"+intent.getStringExtra("Price")+".00"
        prodCode.text      = code
        prodimg            = intent.getStringExtra("Image")
        prodItems.text     = items1+" Items Available"

        loadImg()

        btnAddCart.setOnClickListener {
            if(SharedPref.getmInstance(this).isLoggedIn) {
                dialogCart()
            }else{
                startActivity(intentFor<LoginActivity>())
            }
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return super.onSupportNavigateUp()
    }

    fun loadImg(){
        Picasso.get()
                .load("http://192.168.1.17/e-commerce/assets/image/"+prodimg)
                .resize(450, 450)
                .centerCrop()
                .into(viewProd)
    }

    fun addtoCart(items3: String){
        val user = SharedPref.getmInstance(this).customerUser
        disposable.add( productApiserve.addtoCart(code!!,items3,user!!)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        {result -> bool = result.response
                            if(bool!!){
                                Snackbar.make(view!!, "Added to Cart Successfully", Snackbar.LENGTH_LONG)
                                        .setAction("Go to Cart", { setResult(Activity.RESULT_OK, intent.putExtra("asdss", "asds"))
                                            finish()}).show()
                                getCount()
                            }else{
                                toast("Items Already in your Cart")
                            }},
                        {error ->  toast("Error ${error.localizedMessage}")}
                ))
    }

    fun dialogCart(){
        val dialogView = LayoutInflater.from(this).inflate(R.layout.dialog_items, null)
        var inputItems = dialogView.findViewById<EditText>(R.id.editTextItems)

        val builder = AlertDialog.Builder(this)
                .setView(dialogView)
                .setPositiveButton("Proceed",null)
                .setNegativeButton("Cancel",null)
        val dialog = builder.show()

        dialog.show()

        dialog.getButton(AlertDialog.BUTTON_NEGATIVE).setOnClickListener {
            dialog.dismiss()
        }

        dialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener {
            val items2 = inputItems.text.toString()
                if(items2 == ""){
                    toast("Enter the no. of Order")
                }else{
                    if(items2.toInt() > items1.toString().toInt()){
                        toast("Insufficient Items for your Order")
                    }else{
                        addtoCart(items2.toString())
                         dialog.dismiss()
                    }
                }
        }
    }


    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu,menu)
        val menuItem = menu?.findItem(R.id.cartFrag)
        val actionView = MenuItemCompat.getActionView(menuItem)
        textCartItem = actionView.findViewById(R.id.cart_Badge)
        setupBadge()

        actionView.setOnClickListener {
            onOptionsItemSelected(menuItem!!)
        }
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            R.id.cartFrag -> {
                setResult(Activity.RESULT_OK, intent.putExtra("asdss", "asds"))
                finish()
                return true
            }
            R.id.settingsCart -> {
                startActivity(intentFor<SettingsActivity>())
                return true
            }
            else -> return super.onOptionsItemSelected(item)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        disposable.clear()
    }


    fun setupBadge(){
        val items = SharedPref.getmInstance(this).badgeCount
        if (items!!.toInt() == 0){
            textCartItem?.visibility = View.GONE
        }else{
            textCartItem?.setText(items)
            textCartItem?.visibility = View.VISIBLE
        }

    }

    fun getCount(){
        disposable.add(productApiserve.getCartNo(SharedPref.getmInstance(this).customerUser!!)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        {result -> responseItems(result)  },
                        {error  -> toast("Error ${error.localizedMessage}") }
                ))
    }

    fun responseItems(response: ProductResponse){
        if(response.items == 0){
            val items = 0
            SharedPref.getmInstance(this).getItems(items.toString())
        }else{
            SharedPref.getmInstance(this).getItems(response.items!!.toString())
            setupBadge()
        }
    }
}
