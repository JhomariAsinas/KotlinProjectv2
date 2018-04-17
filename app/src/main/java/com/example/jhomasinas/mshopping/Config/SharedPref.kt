package com.example.jhomasinas.mshopping.Config

import android.content.Context
import android.content.SharedPreferences

/**
 * Created by villaluna on 11/3/2017.
 */

class SharedPref private constructor(context: Context) {

    val isLoggedIn: Boolean
        get() {
            val sharedPreferences = con.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE)
            if (sharedPreferences.getString(KEY_USERNAME, null) != null) {
                return true
            } else {
               return false
            }
        }


    val customerName: String?
        get() {
            val sharedPreferences = con.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE)
            return sharedPreferences.getString(KEY_FULLNAME, null)
        }

    val customerUser: String?
        get() {
            val sharedPreferences = con.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE)
            return sharedPreferences.getString(KEY_USERNAME, null)
        }

    val customerAddress: String?
        get() {
            val sharedPreferences = con.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE)
            return sharedPreferences.getString(KEY_ADDRESS, null)
        }

    val customerContact: String?
        get() {
            val sharedPreferences = con.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE)
            return sharedPreferences.getString(KEY_CONTACT, null)
        }

    val codeProd: String?
        get() {
            val sharedPreferences = con.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE)
            return sharedPreferences.getString(KEY_CODE, null)
        }

    init {
        con = context
    }

    fun userLogin(username: String, fname: String, address: String, contact: String): Boolean {

        val sharedPreferences = con.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE)
        val edit = sharedPreferences.edit()

        edit.putString(KEY_USERNAME, username)
        edit.putString(KEY_ADDRESS, address)
        edit.putString(KEY_CONTACT, contact)
        edit.putString(KEY_FULLNAME, fname)

        edit.apply()

        return true
    }

    fun changeAdd(address: String): Boolean {

        val sharedPreferences = con.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE)
        val edit = sharedPreferences.edit()

        edit.putString(KEY_ADDRESS, address)

        edit.apply()

        return true
    }

    fun addProd(code: String): Boolean {

        val sharedPreferences = con.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE)
        val edit = sharedPreferences.edit()

        edit.putString(KEY_CODE, code)

        edit.apply()

        return true
    }



    fun logout(): Boolean {
        val sharedPreferences = con.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        editor.clear()
        editor.apply()
        return true
    }

    companion object {
        private var mInstance: SharedPref? = null
        private lateinit var con: Context

        private val SHARED_PREF_NAME = "sharedpref1212"
        private val KEY_USERNAME = "username"
        private val KEY_FULLNAME = "fname"
        private val KEY_ADDRESS = "address"
        private val KEY_CONTACT = "srcode"
        private val KEY_CODE = "code"

        @Synchronized
        fun getmInstance(context: Context): SharedPref {
            if (mInstance == null) {
                mInstance = SharedPref(context)
            }
            return mInstance as SharedPref
        }
    }

}
