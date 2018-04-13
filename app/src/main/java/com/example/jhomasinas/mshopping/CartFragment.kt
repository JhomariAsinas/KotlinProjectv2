package com.example.jhomasinas.mshopping


import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.app.AlertDialog
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.TextView
import com.example.jhomasinas.mshopping.Adapter.CartAdapter
import com.example.jhomasinas.mshopping.Config.ProductApi
import com.example.jhomasinas.mshopping.Config.SharedPref
import com.example.jhomasinas.mshopping.Model.Cart
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import org.jetbrains.anko.support.v4.toast
import org.jetbrains.anko.toast


/**
 * A simple [Fragment] subclass.
 */
class CartFragment : Fragment(),CartAdapter.Delegate {
    override fun onTransactCart(cart: Cart) {
        transactCart(cart.product_code)
    }

    override fun onDeleteCart(cart: Cart) {
        deleteCart(cart.product_code)
    }

    val productApiserve by lazy {
        ProductApi.create()
    }

    var disposable : Disposable? = null
    var textAddress : TextView?  = null
    private var cartRecycler: RecyclerView? = null
    private var cartWarning:   TextView?     = null
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val root = inflater!!.inflate(R.layout.fragment_cart, container, false)
        cartRecycler = root.findViewById(R.id.recyclerCard)
        cartWarning  = root.findViewById(R.id.cartWarning)
        textAddress  = root.findViewById<TextView>(R.id.autofitTextView)
        val dialog   = root.findViewById<Button>(R.id.dialogButton)

        dialog.setOnClickListener { createAddressDialog() }

        initAddress()
        initRecycler()
        getCart()
        return root
    }

    fun initRecycler(){
        cartRecycler?.setHasFixedSize(true)
        cartRecycler?.isNestedScrollingEnabled = true
        cartRecycler?.layoutManager = LinearLayoutManager(activity,LinearLayout.VERTICAL,false)

    }

    fun getCart(){
        val user = SharedPref.getmInstance(activity!!).customerUser
       disposable = productApiserve.getCart(user!!)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(
                                {result -> handleResponse(result)},
                                {error  -> toast("Error ${error.localizedMessage}")}
                        )
    }

    fun deleteCart(code: String){
        disposable = productApiserve.deleteCart(code)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        {result -> toast("Items deleted in your Cart")
                                   getCart()},
                        {error  -> toast("Error ${error.localizedMessage}") }
                )
    }

    fun transactCart(code: String){
        val name    = SharedPref.getmInstance(activity!!).customerName
        val address = SharedPref.getmInstance(activity!!).customerAddress
        val contact = SharedPref.getmInstance(activity!!).customerContact
        val user = SharedPref.getmInstance(activity!!).customerUser

        disposable = productApiserve.transactProd(code,address!!,name!!,contact!!,user!!)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        {result -> toast("Transaction Complete")},
                        {error  -> toast("Error ${error.localizedMessage}")}
                )
    }

    fun handleResponse(cartList: List<Cart>) {
        val mCartList: ArrayList<Cart> = ArrayList(cartList)
        val adapter = CartAdapter(mCartList,this)
        cartRecycler?.adapter   = adapter
        cartWarning?.visibility = View.GONE
    }


    fun updateProd(address: String){
        val user = SharedPref.getmInstance(activity!!).customerUser
        disposable = productApiserve.updateAddress(user!!,address)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        {result -> SharedPref.getmInstance(activity!!).changeAdd(address)
                                   toast("Your address was successfully updated")
                        },
                        {error  -> toast("Error ${error.localizedMessage}") }
                )
    }

    fun createAddressDialog(){
        val dialogView = LayoutInflater.from(activity).inflate(R.layout.dialog_address, null)
        var edit = dialogView.findViewById<EditText>(R.id.editAddress)

        val builder = AlertDialog.Builder(activity!!)
                .setView(dialogView)
                .setPositiveButton("Change Address",null)
                .setNegativeButton("Cancel",null)
        val dialog = builder.show()

        dialog.show()

        dialog.getButton(AlertDialog.BUTTON_NEGATIVE).setOnClickListener {
            dialog.dismiss()
        }

        dialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener {
            updateProd(edit.text.toString())
            initAddress()
            dialog.dismiss()

        }
    }

    fun initAddress(){
        textAddress?.text = SharedPref.getmInstance(activity!!).customerAddress
    }

    override fun onPause() {
        super.onPause()
        disposable?.dispose()
    }
}
