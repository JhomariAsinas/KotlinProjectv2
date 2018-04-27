package com.example.jhomasinas.mshopping.Activity

import android.app.Activity
import android.support.design.widget.TabLayout
import android.support.v7.app.AppCompatActivity

import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentPagerAdapter
import android.os.Bundle
import android.view.View
import android.widget.Button
import com.example.jhomasinas.mshopping.Config.ProductApi
import com.example.jhomasinas.mshopping.Config.ProductResponse
import com.example.jhomasinas.mshopping.Config.SharedPref
import com.example.jhomasinas.mshopping.R
import com.example.jhomasinas.mshopping.Fragment.TabbedFragment1
import com.example.jhomasinas.mshopping.Fragment.TabbedFragment2
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers

import kotlinx.android.synthetic.main.activity_payment.*
import org.jetbrains.anko.toast

class PaymentActivity : AppCompatActivity() {


    val productApiserve by lazy {
        ProductApi.create()
    }

    var disposable = CompositeDisposable()


    private var mSectionsPagerAdapter: SectionsPagerAdapter? = null

    private var code : String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_payment)

        setSupportActionBar(toolbar)

        mSectionsPagerAdapter = SectionsPagerAdapter(supportFragmentManager)

        container.adapter = mSectionsPagerAdapter

        container.addOnPageChangeListener(TabLayout.TabLayoutOnPageChangeListener(tabs))
        tabs.addOnTabSelectedListener(TabLayout.ViewPagerOnTabSelectedListener(container))





    }

    inner class SectionsPagerAdapter(fm: FragmentManager) : FragmentPagerAdapter(fm) {

        override fun getItem(position: Int): Fragment? {
            when(position){
                0 -> return TabbedFragment1()
                1 -> return TabbedFragment2()
                else -> return null
            }
        }

        override fun getCount(): Int {
            // Show 3 total pages.
            return 2
        }
    }

     fun choose(view: View) {
        val answers = view as Button
        when(answers.id){
            R.id.btnCash -> {
                Transaction("Cash on Delivery")
            }
            R.id.btnRemmit -> {
                Transaction("Remmitance")
            }
        }

    }

    fun Transaction(payment:String){
        disposable.add(productApiserve.transactProd(
                SharedPref.getmInstance(this).codeProd!!,
                SharedPref.getmInstance(this).customerAddress!!,
                SharedPref.getmInstance(this).customerName!!,
                SharedPref.getmInstance(this).customerContact!!,
                SharedPref.getmInstance(this).customerUser!!,
                payment)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        {result -> handleResponse(result)},
                        {error  -> toast("Error ${error.localizedMessage}") }
                ))
    }

    fun handleResponse(response: ProductResponse){
        if(response.response!!){
            toast("Successfully Cashout")
            setResult(Activity.RESULT_OK, intent.putExtra("asdss", "asds"))
            finish()
        }else{
            toast("Transaction was Failed")
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        disposable.clear()
    }
}
