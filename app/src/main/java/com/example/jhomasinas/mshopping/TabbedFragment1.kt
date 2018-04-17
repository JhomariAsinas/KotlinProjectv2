package com.example.jhomasinas.mshopping


import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import com.example.jhomasinas.mshopping.Config.SharedPref


/**
 * A simple [Fragment] subclass.
 */
class TabbedFragment1 : Fragment() {

    private var editName    : EditText? = null
    private var editAddress : EditText? = null
    private var editContact : EditText? = null
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {

        val root = inflater.inflate(R.layout.fragment_tabbed_fragment1, container, false)
         editName    = root.findViewById(R.id.editName)
         editAddress = root.findViewById(R.id.editAddress)
         editContact = root.findViewById(R.id.editContact)
         getData()
         return root
    }

    fun getData(){
        editName?.setText(SharedPref.getmInstance(activity!!).customerName)
        editContact?.setText(SharedPref.getmInstance(activity!!).customerContact)
        editAddress?.setText(SharedPref.getmInstance(activity!!).customerAddress)
    }



}// Required empty public constructor
