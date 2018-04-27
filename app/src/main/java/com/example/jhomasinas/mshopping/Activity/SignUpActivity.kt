package com.example.jhomasinas.mshopping.Activity

import android.os.AsyncTask
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.example.jhomasinas.mshopping.Config.ProductApi
import com.example.jhomasinas.mshopping.Config.ProductResponse
import com.example.jhomasinas.mshopping.R
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_sign_up.*
import me.pushy.sdk.Pushy
import me.pushy.sdk.util.exceptions.PushyException
import org.jetbrains.anko.intentFor
import org.jetbrains.anko.toast

class SignUpActivity : AppCompatActivity() {
    private var deviceToken: String? = null

    val productApiserve by lazy {
        ProductApi.create()
    }

    var disposable : Disposable? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_up)
        Pushy.listen(this);
        btnSignUp.setOnClickListener {
            RegisterForPushNotificationAsync().execute()
            if(deviceToken == null){
                toast("Cannot SignUp")
            }else{
                signUp()
            }
        }
    }

    fun signUp(){
        disposable = productApiserve.signUp(
                     customUser.text.toString(),
                     customPass.text.toString(),
                     customFullname.text.toString(),
                customStreet.text.toString()+"  "+customProvince.text.toString(),
                     customContact.text.toString(),
                     deviceToken!!)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        {result -> handleResponse(result)},
                        {error  -> toast("Error ${error.localizedMessage}")}
                )
    }

    fun handleResponse(response : ProductResponse){
        if(response.response!!){
            startActivity(intentFor<LoginActivity>())
            toast("Account was created successfully")
            finish()
        }else{
            toast("Cannot Sign In")
        }
    }

    private inner class RegisterForPushNotificationAsync : AsyncTask<Void, Void, Exception>(){

        override fun doInBackground(vararg params: Void): Exception? {
            try {
                deviceToken = Pushy.register(applicationContext)
                Log.d("MyApp", "Pushy device token: $deviceToken")
            } catch (e: PushyException) {
                e.printStackTrace()
            }

            return null
        }

        override fun onPostExecute(e: Exception?) {
            if (e != null) {
                Toast.makeText(applicationContext, e.toString(), Toast.LENGTH_SHORT).show()
                return
            }
        }
    }


}
