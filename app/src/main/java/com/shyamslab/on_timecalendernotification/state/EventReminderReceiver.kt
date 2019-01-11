package com.shyamslab.on_timecalendernotification.state

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log
import android.widget.Toast
import kotlin.math.log

class EventReminderReceiver: BroadcastReceiver() {

    override  fun onReceive(context: Context?, intent: Intent?) {
        if (context == null || intent == null) {
            Log.e(LOG_TAG, "either context or intent is null!!")
            return
        }

        Log.i(LOG_TAG,"Event reminder received, ${intent.data}, ${intent.action}");
    }
    companion object {
        private const val LOG_TAG = "EventReminderReceiver"
    }
}