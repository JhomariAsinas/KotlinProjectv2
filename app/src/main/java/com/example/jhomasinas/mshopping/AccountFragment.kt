package com.example.jhomasinas.mshopping


import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import com.example.jhomasinas.mshopping.Config.SharedPref
import kotlinx.android.synthetic.main.fragment_account.*
import org.jetbrains.anko.noButton
import org.jetbrains.anko.support.v4.alert
import org.jetbrains.anko.support.v4.intentFor
import org.jetbrains.anko.yesButton
import org.w3c.dom.Text


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

        val root = inflater!!.inflate(R.layout.fragment_account, container, false)
        btnSign     = root.findViewById(R.id.btnSignLayout)
        btnLog      = root.findViewById(R.id.btnLoginLayout)
        btnAccount  = root.findViewById(R.id.btnChangeAccount)
        txtHeader   = root.findViewById(R.id.textView12)
        txtUser     = root.findViewById(R.id.textView13)
        txtName     = root.findViewById(R.id.textView15)

        btnSign?.setOnClickListener { startActivity(intentFor<SignUpActivity>()) }
        btnLog?.setOnClickListener  { startActivity(intentFor<LoginActivity>())  }

        btnAccount?.setOnClickListener {
            alert("Are you sure you want to logout?") {
                title = "Alert"
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
             txtName?.text = SharedPref.getmInstance(activity!!).customerName
        }
    }

    fun logout(){
        SharedPref.getmInstance(activity!!).logout()
        startActivity(intentFor<MainActivity>())
    }

}
