package com.example.jhomasinas.mshopping

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import com.example.jhomasinas.mshopping.Config.ProductApi
import com.example.jhomasinas.mshopping.Config.ProductResponse
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_sign_up.*
import org.jetbrains.anko.intentFor
import org.jetbrains.anko.toast

class SignUpActivity : AppCompatActivity() {

    val productApiserve by lazy {
        ProductApi.create()
    }

    var disposable : Disposable? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_up)
        btnSignUp.setOnClickListener {
            signUp()
        }
    }

    fun signUp(){
        disposable = productApiserve.signUp(
                     customUser.text.toString(),
                     customPass.text.toString(),
                     customFullname.text.toString(),
                     customStreet.text.toString()+"  "+customProvince.text.toString(),
                     customContact.text.toString())

                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        {result -> startActivity(intentFor<LoginActivity>())
                                   toast("Account was created successfully")
                                   finish()
                        },
                        {error  -> toast("Error ${error.localizedMessage}")}
                )
    }

}
