package com.example.jhomasinas.mshopping


import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import com.example.jhomasinas.mshopping.Config.ProductApi
import com.example.jhomasinas.mshopping.Config.ProductResponse
import com.example.jhomasinas.mshopping.Config.SharedPref
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import org.jetbrains.anko.support.v4.toast
import org.jetbrains.anko.toast


/**
 * A simple [Fragment] subclass.
 */
class TabbedFragment2 : Fragment() {




    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {

        val root = inflater.inflate(R.layout.fragment_tabbed_fragment2, container, false)

        return root
    }



}// Required empty public constructor
