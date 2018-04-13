package com.example.jhomasinas.mshopping

import android.app.Activity
import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.app.AlertDialog
import android.support.v7.widget.Toolbar
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuItem
import android.widget.EditText
import com.example.jhomasinas.mshopping.Config.ProductApi
import com.example.jhomasinas.mshopping.Config.SharedPref
import com.squareup.picasso.Picasso
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_product_detail.*
import kotlinx.android.synthetic.main.list_product.*
import org.jetbrains.anko._Toolbar
import org.jetbrains.anko.appcompat.v7.activityChooserView
import org.jetbrains.anko.intentFor
import org.jetbrains.anko.toast

class ProductDetail : AppCompatActivity() {

    val productApiserve by lazy {
        ProductApi.create()
    }
    var disposable : Disposable? = null

    var code:    String? = null
    var prodimg: String? = null
    var items1:  String?    = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_product_detail)

        val toolbar = findViewById<Toolbar>(R.id.prodToolbar)
        setSupportActionBar(toolbar)
        supportActionBar?.title = "Product Detail"

        prodName2.text     = intent.getStringExtra("Name")
        prodDes.text       = intent.getStringExtra("Descrip")
        prodCategory.text  = intent.getStringExtra("Category")
        code               = intent.getStringExtra("Code")
        items1             = intent.getStringExtra("Items")
        prodPrice2.text    = intent.getStringExtra("Price")
        prodCode.text      = code
        prodimg            = intent.getStringExtra("Image")
        prodItems.text     = items1

        loadImg()
        btnAddCart.setOnClickListener {
            if(SharedPref.getmInstance(this).isLoggedIn) {
                dialogCart()
            }else{
                startActivity(intentFor<LoginActivity>())
            }
        }
    }

    fun loadImg(){
        Picasso.get()
                .load("http://192.168.1.50/e-commerce/assets/image/"+prodimg)
                .resize(450, 450)
                .centerCrop()
                .into(viewProd)
    }

    fun addtoCart(items3: String){
        val user = SharedPref.getmInstance(this).customerUser
        disposable = productApiserve.addtoCart(code!!,items3,user!!)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        {result -> toast("Add Cart Successfully")},
                        {error -> toast("Error ${error.localizedMessage}")}
                )
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
            if(items2 > items1!!){
                toast("Insufficient Items for your Order")
            }else{
                addtoCart(items2)
            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu,menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            R.id.cartFrag -> {
                setResult(Activity.RESULT_OK, intent.putExtra("asdss", "asds"))
                finish()
                return true

            }
            else -> return super.onOptionsItemSelected(item)
        }
    }


}
