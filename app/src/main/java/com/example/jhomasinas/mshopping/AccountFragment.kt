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
import org.jetbrains.anko.support.v4.intentFor


/**
 * A simple [Fragment] subclass.
 */
class AccountFragment : Fragment() {

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {

        val root = inflater!!.inflate(R.layout.fragment_account, container, false)
        val btnSign = root.findViewById<Button>(R.id.btnSignLayout)
        val btnLog  = root.findViewById<Button>(R.id.btnLoginLayout)

        btnSign.setOnClickListener { startActivity(intentFor<SignUpActivity>()) }
        btnLog.setOnClickListener  { startActivity(intentFor<LoginActivity>())  }

        return root
    }

}
