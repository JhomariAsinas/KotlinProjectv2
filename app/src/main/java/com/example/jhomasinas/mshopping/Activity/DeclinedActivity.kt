package com.example.jhomasinas.mshopping.Activity

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import com.example.jhomasinas.mshopping.R
import kotlinx.android.synthetic.main.activity_declined.*

class DeclinedActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_declined)
        setSupportActionBar(toolbar2)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowHomeEnabled(true)
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return super.onSupportNavigateUp()
    }
}
