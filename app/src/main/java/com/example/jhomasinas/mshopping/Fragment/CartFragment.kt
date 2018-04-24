package com.example.jhomasinas.mshopping.Fragment


import android.app.Activity.RESULT_OK
import android.app.ProgressDialog
import android.content.Intent
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import com.example.jhomasinas.mshopping.Activity.LoginActivity
import com.example.jhomasinas.mshopping.Activity.PaymentActivity
import com.example.jhomasinas.mshopping.Adapter.CartAdapter
import com.example.jhomasinas.mshopping.Adapter.ProcessAdapter
import com.example.jhomasinas.mshopping.Config.ProductApi
import com.example.jhomasinas.mshopping.Config.SharedPref
import com.example.jhomasinas.mshopping.Model.Approved
import com.example.jhomasinas.mshopping.Model.Cart
import com.example.jhomasinas.mshopping.R
import com.google.android.gms.common.GooglePlayServicesNotAvailableException
import com.google.android.gms.location.places.ui.PlacePicker
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.fragment_cart.*
import org.jetbrains.anko.support.v4.indeterminateProgressDialog
import org.jetbrains.anko.support.v4.intentFor
import org.jetbrains.anko.support.v4.toast


/**
 * A simple [Fragment] subclass.
 */
class CartFragment : Fragment(),CartAdapter.Delegate {
    override fun onTransactCart(cart: Cart) {
        val int = Intent(activity, PaymentActivity::class.java)
        SharedPref.getmInstance(activity!!).addProd(cart.product_code)
        activity!!.startActivityForResult(int,60)
    }

    override fun onDeleteCart(cart: Cart) {
        deleteCart(cart.product_code)
        getCart()
    }

    companion object {
        private const val REQUEST_PLACE_PICKER = 1

    }

    val productApiserve by lazy {
        ProductApi.create()
    }

    var disposable : Disposable? = null
    var textAddress : TextView?  = null

    var bool : Boolean? = null

    var cartRecycler: RecyclerView?     = null
    var cartWarning:   TextView?        = null
    var approvedRecycler: RecyclerView? = null
    var processWarning:   TextView?     = null
    var progressDialog : ProgressDialog? = null



    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        if(!SharedPref.getmInstance(activity).isLoggedIn){
            startActivity(intentFor<LoginActivity>())
        }
        val root = inflater.inflate(R.layout.fragment_cart, container, false)

        processWarning   = root.findViewById(R.id.processWarning)
        cartRecycler     = root.findViewById(R.id.recyclerCard)
        cartWarning      = root.findViewById(R.id.cartWarning)
        textAddress      = root.findViewById(R.id.autofitTextView)
        approvedRecycler = root.findViewById(R.id.recyclerProcess)

        val dialog   = root.findViewById<Button>(R.id.dialogButton)

        dialog.setOnClickListener {
            loadPlacePicker()
        }

        textAddress?.text = SharedPref.getmInstance(activity!!).customerAddress
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
                                {result -> handleCart(result)},
                                {error  -> toast("Error ${error.localizedMessage}")}
                        )
    }

    fun getApproved(){
        val user = SharedPref.getmInstance(activity!!).customerUser
        disposable = productApiserve.getTransact(user!!)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        {result -> handleOrder(result)},
                        {error  -> toast("Error ${error.localizedMessage}")}
                )
    }


    fun deleteCart(code: String){
        disposable = productApiserve.deleteCart(code,SharedPref.getmInstance(activity!!).customerUser!!)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        {result ->if(result.response!!){
                                        getCart()
                                 }else{
                                        toast("Deleting was Failed")
                        }},
                        {error  -> toast("Error ${error.localizedMessage}") }
                )
    }


    fun handleCart(cartList: List<Cart>) {
        if(cartList == null || cartList.isEmpty()){
              val mCartList: ArrayList<Cart> = ArrayList(cartList)
              val adapter = CartAdapter(mCartList,this)
              cartRecycler?.adapter   = adapter
              cartWarning?.visibility  = View.VISIBLE
              progressCart.visibility = View.GONE
        }else{
              val mCartList: ArrayList<Cart> = ArrayList(cartList)
              val adapter = CartAdapter(mCartList,this)
              cartRecycler?.adapter   = adapter
              cartWarning?.visibility = View.GONE
              progressCart.visibility = View.GONE

        }

    }

    fun handleOrder(approvedList: List<Approved>) {
        if(approvedList == null || approvedList.isEmpty()){
            processWarning?.visibility  = View.VISIBLE
            progressApproved.visibility = View.GONE
        }else{
            val mApprovedList: ArrayList<Approved> = ArrayList(approvedList)
            val adapter = ProcessAdapter(mApprovedList)
            approvedRecycler?.adapter   = adapter
            processWarning?.visibility  = View.GONE
            progressApproved.visibility = View.GONE

        }

    }


    fun updateAdd(address: String){
        progressDialog = indeterminateProgressDialog("Fetching Product")
        progressDialog!!.setCancelable(false)
        progressDialog!!.show()
        val user = SharedPref.getmInstance(activity!!).customerUser
        disposable = productApiserve.updateAddress(user!!,address)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        {result -> bool = result.response
                                   changeAdd(address)},
                        {error  -> toast("Error ${error.localizedMessage}") }
                )
    }

    fun changeAdd(add: String){
        if(bool!!){
            if(SharedPref.getmInstance(activity!!).changeAdd(add)){
                textAddress?.text = SharedPref.getmInstance(activity!!).customerAddress
            }else{
                toast("Error in saving your Location")
            }
        }else{
            toast("Error in saving your Location")
        }
        progressDialog?.dismiss()

    }


    override fun onPause() {
        super.onPause()
        disposable?.dispose()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(requestCode == REQUEST_PLACE_PICKER){
            if(resultCode == RESULT_OK){
                val place = PlacePicker.getPlace(activity!!,data)
                var addresstext = place.name.toString()
                addresstext += " ${place.address.toString()}"
                updateAdd(addresstext)
            }
        }
    }

    fun loadPlacePicker(){
        val builder = PlacePicker.IntentBuilder()

        try {
            startActivityForResult(builder.build(activity!!), REQUEST_PLACE_PICKER)
        }catch (e: GooglePlayServicesNotAvailableException) {
            e.printStackTrace()
        }
    }

    override fun onStop() {
        super.onStop()
        disposable?.dispose()
    }


}
