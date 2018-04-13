package com.example.jhomasinas.mshopping


import android.app.ProgressDialog
import android.content.Intent
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.jhomasinas.mshopping.Adapter.ProductAdapter
import com.example.jhomasinas.mshopping.Config.ProductApi
import com.example.jhomasinas.mshopping.Model.Product
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_product_detail.*
import org.jetbrains.anko.support.v4.indeterminateProgressDialog
import org.jetbrains.anko.support.v4.intentFor
import org.jetbrains.anko.support.v4.toast


/**
 * A simple [Fragment] subclass.
 */
class HomeFragment : Fragment(),ProductAdapter.Delegate {
    override fun onClickProduct(product: Product) {
        val int : Intent = Intent(activity,ProductDetail::class.java)
        int.putExtra("Code"    ,product.product_code)
        int.putExtra("Name"    ,product.product_name)
        int.putExtra("Category",product.product_category)
        int.putExtra("Items"   ,product.product_items)
        int.putExtra("Price"   ,product.product_price)
        int.putExtra("Descrip"   ,product.product_des)
        int.putExtra("Image"   ,product.product_image)
        activity.startActivityForResult(int,50)
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

    var disposable : Disposable? = null
    var progressDialog : ProgressDialog? = null

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val root =  inflater!!.inflate(R.layout.fragment_home, container, false)

        recyclerView1  = root.findViewById(R.id.recyclerView)as RecyclerView

        progressDialog = indeterminateProgressDialog("Fetching Product")
        progressDialog!!.setCancelable(false)
        progressDialog!!.show()

        initRecyclerView()

        getProduct()
        return root

    }

    fun initRecyclerView(){
        recyclerView1!!.setHasFixedSize(true)
        recyclerView1!!.layoutManager = GridLayoutManager(activity,2)
    }

    fun getProduct(){
        disposable = productApiserve.getProduct()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        {result -> handleResponse(result)},
                        {error  ->  toast("Error ${error.localizedMessage}")
                            progressDialog!!.dismiss()}
                )
    }

    override fun onPause() {
        super.onPause()
        disposable?.dispose()
    }

    fun handleResponse(product: List<Product>){
        val mArrayProduct: ArrayList<Product> = ArrayList(product)
        val adapter = ProductAdapter(mArrayProduct,this)
        recyclerView1!!.adapter = adapter
        progressDialog?.dismiss()
    }

}// Required empty public constructor
