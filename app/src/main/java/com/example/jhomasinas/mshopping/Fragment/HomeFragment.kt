package com.example.jhomasinas.mshopping.Fragment


import android.app.ProgressDialog
import android.content.Intent
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.widget.SwipeRefreshLayout
import android.support.v7.app.AlertDialog
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import com.example.jhomasinas.mshopping.Activity.ProductDetail
import com.example.jhomasinas.mshopping.Adapter.ProductAdapter
import com.example.jhomasinas.mshopping.Config.ProductApi
import com.example.jhomasinas.mshopping.Config.ProductResponse
import com.example.jhomasinas.mshopping.Config.SharedPref
import com.example.jhomasinas.mshopping.Model.Product
import com.example.jhomasinas.mshopping.R
import com.squareup.picasso.Picasso
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import org.jetbrains.anko.support.v4.indeterminateProgressDialog
import org.jetbrains.anko.support.v4.toast


/**
 * A simple [Fragment] subclass.
 */
class HomeFragment : Fragment(),ProductAdapter.Delegate {
    override fun onClickImage(product: Product) {
        createImageDialog(product.product_image)
    }

    override fun onClickProduct(product: Product) {
        val int : Intent = Intent(activity, ProductDetail::class.java)
        int.putExtra("Code"    ,product.product_code)
        int.putExtra("Name"    ,product.product_name)
        int.putExtra("Category",product.product_category)
        int.putExtra("Items"   ,product.product_items)
        int.putExtra("Price"   ,product.product_price)
        int.putExtra("Descrip"   ,product.product_des)
        int.putExtra("Image"   ,product.product_image)
        activity!!.startActivityForResult(int,50)
    }

    companion object {
        fun newInstance(): HomeFragment {
            var fragmentHome = HomeFragment()
            var args = Bundle()
            fragmentHome.arguments = args
            return fragmentHome
        }
    }
    private var recyclerView1: RecyclerView? = null

    val productApiserve by lazy {
        ProductApi.create()
    }

    var disposable = CompositeDisposable()
    var swipe : SwipeRefreshLayout? = null
    var progressDialog : ProgressDialog? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val root =  inflater.inflate(R.layout.fragment_home, container, false)

        swipe = root.findViewById(R.id.mSwipeRefreshLayout)
        recyclerView1  = root.findViewById(R.id.recyclerView)as RecyclerView

        progressDialog = indeterminateProgressDialog("Fetching Product")
        progressDialog!!.setCancelable(false)
        progressDialog!!.show()

        initRecyclerView()

        getProduct()

        swipe?.setOnRefreshListener {
            getProduct()
            swipe?.isRefreshing = false
        }
        return root

    }

    fun initRecyclerView(){
        recyclerView1!!.setHasFixedSize(true)
        recyclerView1!!.layoutManager = GridLayoutManager(activity,2)
    }

    fun getProduct(){
        disposable.add(productApiserve.getProduct()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        {result -> handleResponse(result)},
                        {error  ->  toast("Error ${error.localizedMessage}")
                            progressDialog!!.dismiss()}
                ))
    }

    override fun onDestroyView() {
        super.onDestroyView()
        disposable.clear()
    }

    fun handleResponse(product: List<Product>){
        val mArrayProduct: ArrayList<Product> = ArrayList(product)
        val adapter = ProductAdapter(mArrayProduct,this)
        recyclerView1!!.adapter = adapter
        getCount()
        progressDialog?.dismiss()

    }

    fun createImageDialog(img: String){
            val dialogView = LayoutInflater.from(activity).inflate(R.layout.dialog_image, null)
            var imgView = dialogView.findViewById<ImageView>(R.id.imageDialog)

            Picasso.get()
                    .load("http://192.168.1.17/e-commerce/assets/image/"+img)
                    .resize(350, 350)
                    .centerCrop()
                    .into(imgView)

            val builder = AlertDialog.Builder(activity!!)
                    .setView(dialogView)
            val dialog = builder.show()

            dialog.show()

    }

    fun getCount(){
        disposable.add(productApiserve.getCartNo(SharedPref.getmInstance(activity).customerUser!!)
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
            SharedPref.getmInstance(activity).getItems(items.toString())
        }else{
            SharedPref.getmInstance(activity).getItems(response.items!!.toString())
        }
    }



}// Required empty public constructor
