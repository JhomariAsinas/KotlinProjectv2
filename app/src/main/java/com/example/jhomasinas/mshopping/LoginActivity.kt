package com.example.jhomasinas.mshopping

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import com.example.jhomasinas.mshopping.Config.ProductApi
import com.example.jhomasinas.mshopping.Config.SharedPref
import com.example.jhomasinas.mshopping.Model.Customer
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_login.*
import org.jetbrains.anko.intentFor
import org.jetbrains.anko.toast

class LoginActivity : AppCompatActivity() {

    val productApiserve by lazy {
        ProductApi.create()
    }

    var disposable : Disposable? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        btnSignUp.setOnClickListener { startActivity(intentFor<SignUpActivity>()) }
        btnLogin.setOnClickListener { login() }

    }


    fun login(){
        disposable = productApiserve.logIn(logUsername.text.toString(),logPassword.text.toString())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        {result-> handleResponse(result)},
                        {error -> toast("Error ${error.localizedMessage}")}
                )
    }

    fun handleResponse(user: List<Customer>){
        SharedPref.getmInstance(this).userLogin(
                user.get(0).customer_username,
                user.get(0).customer_fullname,
                user.get(0).customer_address,
                user.get(0).customer_contact
        )
        toast("Your account was login successfully")
        startActivity(intentFor<MainActivity>())
    }
}
