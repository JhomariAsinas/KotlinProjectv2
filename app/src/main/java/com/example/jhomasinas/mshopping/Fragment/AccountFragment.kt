package com.example.jhomasinas.mshopping.Fragment


import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import com.example.jhomasinas.mshopping.Activity.LoginActivity
import com.example.jhomasinas.mshopping.Activity.MainActivity
import com.example.jhomasinas.mshopping.Activity.SettingsActivity
import com.example.jhomasinas.mshopping.Activity.SignUpActivity
import com.example.jhomasinas.mshopping.Config.SharedPref
import com.example.jhomasinas.mshopping.R
import com.example.jhomasinas.mshopping.SettingsFragment
import org.jetbrains.anko.noButton
import org.jetbrains.anko.support.v4.alert
import org.jetbrains.anko.support.v4.intentFor
import org.jetbrains.anko.support.v4.toast
import org.jetbrains.anko.yesButton


/**
 * A simple [Fragment] subclass.
 */
class AccountFragment : Fragment() {

    private var btnSign    : Button?   = null
    private var btnLog     : Button?   = null
    private var btnAccount : Button?   = null
    private var txtHeader  : TextView? = null
    private var txtUser    : TextView? = null
    private var txtName    : TextView? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        val root = inflater.inflate(R.layout.fragment_account, container, false)
        btnSign     = root.findViewById(R.id.btnSignLayout)
        btnLog      = root.findViewById(R.id.btnLoginLayout)
        btnAccount  = root.findViewById(R.id.btnChangeAccount)
        txtHeader   = root.findViewById(R.id.textView12)
        txtUser     = root.findViewById(R.id.textView13)
        txtName     = root.findViewById(R.id.textView15)
        btnSign?.setOnClickListener { startActivity(intentFor<SignUpActivity>()) }
        btnLog?.setOnClickListener  { startActivity(intentFor<LoginActivity>())  }
        settings()
        btnAccount?.setOnClickListener {
            alert("Are you sure you want to logout?") {
                title = "LOGOUT"
                yesButton { logout() }
                noButton {  }
            }.show()
        }

        checkAccount()
        return root
    }

    fun checkAccount(){
        if(SharedPref.getmInstance(activity!!).isLoggedIn){
             btnLog?.visibility      = View.INVISIBLE
             btnSign?.visibility     = View.INVISIBLE
             btnAccount?.visibility  = View.VISIBLE
             txtUser?.visibility     = View.VISIBLE
             txtName?.visibility     = View.VISIBLE
             txtName?.text = SharedPref.getmInstance(activity).customerName
        }
    }

    fun logout(){
        if(SharedPref.getmInstance(activity!!).logout()){
            startActivity(intentFor<MainActivity>())
        }else{
           toast("Failed to Logout")
        }

    }

    fun settings(){
        activity.fragmentManager.beginTransaction()
                .replace(R.id.settingsLayout,SettingsFragment())
                .commit()
    }

}


