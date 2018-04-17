package com.example.jhomasinas.mshopping


import android.content.Intent
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.app.AlertDialog
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import com.example.jhomasinas.mshopping.Adapter.CartAdapter
import com.example.jhomasinas.mshopping.Adapter.ProcessAdapter
import com.example.jhomasinas.mshopping.Config.ProductApi
import com.example.jhomasinas.mshopping.Config.SharedPref
import com.example.jhomasinas.mshopping.Model.Approved
import com.example.jhomasinas.mshopping.Model.Cart
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import org.jetbrains.anko.support.v4.intentFor
import org.jetbrains.anko.support.v4.toast
import org.jetbrains.anko.toast


/**
 * A simple [Fragment] subclass.
 */
class CartFragment : Fragment(),CartAdapter.Delegate {
    override fun onTransactCart(cart: Cart) {
        val int : Intent = Intent(activity,PaymentActivity::class.java)
        SharedPref.getmInstance(activity!!).addProd(cart.product_code)
        activity!!.startActivityForResult(int,60)
    }

    override fun onDeleteCart(cart: Cart) {
        deleteCart(cart.product_code)
    }

    val productApiserve by lazy {
        ProductApi.create()
    }

    var disposable : Disposable? = null
    var textAddress : TextView?  = null

    private var cartRecycler: RecyclerView?     = null
    private var cartWarning:   TextView?        = null
    private var approvedRecycler: RecyclerView? = null
    private var processWarning:   TextView?     = null


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val root = inflater!!.inflate(R.layout.fragment_cart, container, false)

        processWarning   = root.findViewById(R.id.processWarning)
        cartRecycler     = root.findViewById(R.id.recyclerCard)
        cartWarning      = root.findViewById(R.id.cartWarning)
        textAddress      = root.findViewById(R.id.autofitTextView)
        approvedRecycler = root.findViewById(R.id.recyclerProcess)

        val dialog   = root.findViewById<Button>(R.id.dialogButton)

        dialog.setOnClickListener { createAddressDialog() }

        initAddress()
        initRecycler()
        initprocessRecycler()
        getCart()
        getApproved()
        return root
    }

    fun initRecycler(){
        cartRecycler?.setHasFixedSize(true)
        cartRecycler?.isNestedScrollingEnabled = true
        cartRecycler?.layoutManager = LinearLayoutManager(activity,LinearLayout.VERTICAL,false)

    }

    fun initprocessRecycler(){
        approvedRecycler?.setHasFixedSize(true)
        approvedRecycler?.isNestedScrollingEnabled = true
        approvedRecycler?.layoutManager = LinearLayoutManager(activity,LinearLayout.VERTICAL,false)

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

    fun getApproved(){
        val user = SharedPref.getmInstance(activity!!).customerUser
        disposable = productApiserve.getTransact(user!!)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        {result -> handleResponse2(result)},
                        {error  -> toast("Error ${error.localizedMessage}")}
                )
    }


    fun deleteCart(code: String){
        disposable = productApiserve.deleteCart(code,SharedPref.getmInstance(activity!!).customerUser!!)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        {result -> toast("Items deleted in your Cart")
                                   getCart()},
                        {error  -> toast("Error ${error.localizedMessage}") }
                )
    }


    fun handleResponse(cartList: List<Cart>) {
        val mCartList: ArrayList<Cart> = ArrayList(cartList)
        val adapter = CartAdapter(mCartList,this)
        if(mCartList != null){
            cartRecycler?.adapter   = adapter
            cartWarning?.visibility = View.GONE
        }

    }

    fun handleResponse2(approvedList: List<Approved>) {
        val mApprovedList: ArrayList<Approved> = ArrayList(approvedList)
        val adapter = ProcessAdapter(mApprovedList)
        if(mApprovedList != null){
            approvedRecycler?.adapter  = adapter
            processWarning?.visibility = View.GONE
        }

    }


    fun updateAdd(address: String){
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
            updateAdd(edit.text.toString())
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
