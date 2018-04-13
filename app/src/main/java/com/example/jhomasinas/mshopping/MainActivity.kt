package com.example.jhomasinas.mshopping

import android.content.Intent
import android.os.Bundle
import android.support.design.widget.BottomNavigationView
import android.support.v4.app.Fragment
import android.support.v7.app.AppCompatActivity
import android.widget.FrameLayout
import kotlinx.android.synthetic.main.activity_main.*
import org.jetbrains.anko.toast

class MainActivity : AppCompatActivity() {
    private var container: FrameLayout? = null
    private var fragment: Fragment? = null
    private val mOnNavigationItemSelectedListener = BottomNavigationView.OnNavigationItemSelectedListener { item ->
        when (item.itemId) {
            R.id.navigation_home -> {
                fragment = HomeFragment.Companion.newInstance()
                addFragment(fragment!!)
                return@OnNavigationItemSelectedListener true
            }
            R.id.navigation_dashboard -> {
                fragment = CartFragment()
                addFragment(fragment!!)
                return@OnNavigationItemSelectedListener true
            }
            R.id.navigation_notifications -> {
                fragment = AccountFragment()
                addFragment(fragment!!)
                return@OnNavigationItemSelectedListener true
            }
        }
        false
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(requestCode == 50 && data != null){
            fragment = CartFragment()
            addFragment(fragment!!)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener)
        fragment = HomeFragment.Companion.newInstance()
        addFragment(fragment!!)
    }

    private fun addFragment(fragment: Fragment){
        supportFragmentManager
                .beginTransaction()
                .setCustomAnimations(R.anim.design_bottom_sheet_slide_in, R.anim.design_bottom_sheet_slide_out)
                .replace(R.id.content,fragment,fragment.javaClass.simpleName)
                .commit()
    }

    override fun onBackPressed() {
        if(fragment != null){
           val fragment2: Fragment = HomeFragment.Companion.newInstance()
           addFragment(fragment2)
            fragment = null
        }else{
            super.onBackPressed()
        }
    }


}
