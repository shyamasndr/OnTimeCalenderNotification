package com.shyamslab.on_timecalendernotification.calender

import android.content.Context
import android.content.Intent
import android.util.EventLog
import android.util.Log
import com.shyamslab.on_timecalendernotification.PermissionsManager

object CalenderHandler {
    const val LOG_TAG = "CalenderHandler";
    fun handlereminder(context: Context, intent: Intent) {

        val uri = intent.data;
        val alertTime = uri?.lastPathSegment?.toLongOrNull()
        if (alertTime == null) {
            Log.e(LOG_TAG, "ERROR alertTime is null!!")
            return
        }
        val calUtility = CalenderUtilties(context);
        val calenderEvent: List<CalenderEvent>? = calUtility.getEventsForReminder(intent.data);
        for (event in calenderEvent!!) {
            val calandar = calUtility.getCalender(event.calendarId)
            Log.i(LOG_TAG, "calandar account ${calandar?.accountName} ${calandar?.maxReminder}!")
            Log.i(LOG_TAG, "Reminder for event ${event.eventId} ${event.title}, ${event.duration}!")
            val eventReminders: List<EventReminder>? = calUtility.getReminderaForEvent(event.eventId)
            if(!(eventReminders!!.count()>1) && !oneMinuteReminder(eventReminders)) {
                Log.i(LOG_TAG, "No one minute reminder found, so adding!")
                calUtility.addReminderToEvent(event.eventId);
            }
        }
    }

    private fun oneMinuteReminder(eventReminders:List<EventReminder>?): Boolean {
        var anyOneMinuteReminder: Boolean = false
        for (eventReminder in eventReminders!!) {

            var reminderToCompare: Long = 1L;
            if (eventReminder.minutes.equals(reminderToCompare)) {
                return true
            }
        }
        return false
    }
}
