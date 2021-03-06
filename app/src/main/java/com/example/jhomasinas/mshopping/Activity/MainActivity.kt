@file:Suppress("DEPRECATION")

package com.example.jhomasinas.mshopping.Activity


import android.content.Intent
import android.os.Bundle
import android.support.design.internal.BottomNavigationItemView
import android.support.design.internal.BottomNavigationMenuView
import android.support.design.widget.BottomNavigationView
import android.support.v4.app.Fragment
import android.support.v4.view.MenuItemCompat
import android.support.v7.app.AppCompatActivity
import android.view.MenuItem
import android.view.View
import android.widget.FrameLayout
import android.widget.TextView
import com.example.jhomasinas.mshopping.Config.PicassoImageLoadingService
import com.example.jhomasinas.mshopping.Config.SharedPref
import com.example.jhomasinas.mshopping.Fragment.*
import com.example.jhomasinas.mshopping.R
import com.example.jhomasinas.mshopping.SettingsFragment
import kotlinx.android.synthetic.main.activity_main.*
import me.pushy.sdk.Pushy
import org.jetbrains.anko.intentFor
import q.rorbin.badgeview.QBadgeView
import ss.com.bannerslider.ImageLoadingService
import ss.com.bannerslider.Slider


class MainActivity : AppCompatActivity() {

    private var fragmentint: Int? = null
    private var container:   FrameLayout? = null
    private var fragment:    Fragment? = null
    private var txtCartItem: TextView? = null

    private val mOnNavigationItemSelectedListener = BottomNavigationView.OnNavigationItemSelectedListener { item ->
        when (item.itemId) {
            R.id.navigation_home -> {
                fragment = HomeFragment.newInstance()
                addFragment(fragment!!)
                fragmentint = 1
                return@OnNavigationItemSelectedListener true
            }
            R.id.navigation_dashboard -> {
                if(SharedPref.getmInstance(this).isLoggedIn){
                    fragment = CartFragment()
                    addFragment(fragment!!)
                    fragmentint = 2
                }else{
                    startActivity(intentFor<LoginActivity>())
                }
                return@OnNavigationItemSelectedListener true
            }
            R.id.navigation_notifications -> {
                fragment = AccountFragment()
                addFragment(fragment!!)
                fragmentint = 3
                return@OnNavigationItemSelectedListener true
            }
        }
        true
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(requestCode == 50 && data != null){
            if(SharedPref.getmInstance(this).isLoggedIn){
                fragment = CartFragment()
                addFragment(fragment!!)
                fragmentint = 2
                navigation.menu.getItem(1).setChecked(true)
                setBadge()
            }else{
                startActivity(intentFor<LoginActivity>())
            }

        }else if(requestCode == 60 && data!= null){
            fragment = CartFragment()
            addFragment(fragment!!)
            fragmentint = 2
            navigation.menu.getItem(1).setChecked(true)
            setBadge()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        Pushy.listen(this);
        Slider.init(PicassoImageLoadingService())
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener)
        fragment = HomeFragment.newInstance()
        addFragment(fragment!!)
        fragmentint = 1
        checkBadge()
    }

    private fun addFragment(fragment: Fragment){
        supportFragmentManager
                .beginTransaction()
                .setCustomAnimations(R.anim.design_bottom_sheet_slide_in, R.anim.design_bottom_sheet_slide_out)
                .replace(R.id.container2,fragment,fragment.javaClass.simpleName)
                .commit()
    }

    override fun onBackPressed() {
        if(fragmentint  == 1){
            super.onBackPressed()
        }else{
            fragment = HomeFragment.newInstance()
            addFragment(fragment!!)
            fragmentint = 1
            navigation.menu.getItem(0).setChecked(true)
        }
    }

    fun setBadge(){
        val bottomNavigationMenuView : BottomNavigationMenuView = navigation.getChildAt(0) as BottomNavigationMenuView
        val v : View = bottomNavigationMenuView.getChildAt(1)
        val items = SharedPref.getmInstance(this).badgeCount
        if(items?.toInt() == 0){
            return
        }else{
            QBadgeView(this).bindTarget(v)
                    .setBadgeNumber(items!!.toInt())
                    .setGravityOffset(30f,1f,true)
                    .setShowShadow(false)
        }

    }

    fun checkBadge(){
        if(SharedPref.getmInstance(this).isLoggedIn){
            setBadge()
        }else{
            SharedPref.getmInstance(this).getItems(0.toString())
            setBadge()
        }
    }













}
