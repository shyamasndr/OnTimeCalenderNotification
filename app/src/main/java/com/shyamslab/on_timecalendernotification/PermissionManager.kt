package com.shyamslab.on_timecalendernotification


import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.pm.PackageManager
import android.support.v4.app.ActivityCompat
import android.support.v4.content.ContextCompat

object PermissionsManager {

    private fun Context.hasPermission(perm: String) =
        ContextCompat.checkSelfPermission(this, perm) == PackageManager.PERMISSION_GRANTED;

    fun hasWriteCalendar(context: Context)
            = context.hasPermission(Manifest.permission.WRITE_CALENDAR)

    fun hasReadCalendar(context: Context)
            = context.hasPermission(Manifest.permission.READ_CALENDAR)

    fun hasAllPermissions(context: Context) = hasWriteCalendar(context) && hasReadCalendar(context)

    fun requestPermissions(activity: Activity) =
        ActivityCompat.requestPermissions(activity,
            arrayOf(Manifest.permission.READ_CALENDAR, Manifest.permission.WRITE_CALENDAR), 0)
}