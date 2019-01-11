package com.shyamslab.on_timecalendernotification

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.widget.SwitchCompat
import android.view.Menu
import android.widget.CompoundButton

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {

        val inflater = menuInflater
        inflater.inflate(R.menu.notification_activity_menu, menu)

        val item = menu?.findItem(R.id.vibration_switch)
        val mainSwitch = item?.actionView as SwitchCompat
        //mainSwitch.setOnCheckedChangeListener { buttonView, isChecked -> settingsModel.setVibrationServiceOn(isChecked) }


        return true
    }
}
