package com.shyamslab.on_timecalendernotification.calender

import android.net.Uri
import android.provider.CalendarContract
import android.util.Log
import android.content.Context
import android.database.Cursor
import android.content.ContentResolver
import android.content.ContentUris
import android.content.ContentValues
import java.util.*
import com.shyamslab.on_timecalendernotification.PermissionsManager

class  CalenderUtilties(context:Context) {
    val context = context;
    val LOG_TAG = "CalenderUtilties";
    fun getCalender(id:Long):CalendarAccount? {
        val uri: Uri = CalendarContract.Calendars.CONTENT_URI
        var calander: CalendarAccount?=null
        val calenderFields =
            arrayOf(
                CalendarContract.Calendars.ACCOUNT_NAME,
                CalendarContract.Calendars.MAX_REMINDERS
            )
        val selection = CalendarContract.Calendars._ID + "=?"
        val cursor: Cursor? =
            context.contentResolver.query(
                uri,
                calenderFields,
                selection,
                arrayOf(id.toString()),
                null
            )

        if (cursor != null && cursor.moveToFirst()) {

            calander = cursorToCalendarAccount(cursor)
        }
        cursor?.close()
        return calander
    }


    private fun cursorToCalendarAccount(cursor: Cursor): CalendarAccount {
        val ACCOUNT_NAME = 0
        val MAX_REMINDERS = 1


        val accountName: String = cursor.getString(ACCOUNT_NAME)
        val maxReminder: Long = cursor.getLong(MAX_REMINDERS)

        val calendarAccount =
            CalendarAccount(
                accountName = accountName,
                maxReminder = maxReminder

            )
        return calendarAccount;
    }


    fun getReminderaForEvent(id:Long):List<EventReminder> {
        val uri: Uri = CalendarContract.Reminders.CONTENT_URI
        var calander: CalendarAccount?=null
        val calenderFields =
            arrayOf(
                CalendarContract.Reminders._ID,
                CalendarContract.Reminders.EVENT_ID,
                        CalendarContract.Reminders.MINUTES
            )
        val selection = CalendarContract.Reminders.EVENT_ID+ "=?"
        val cursor: Cursor? =
            context.contentResolver.query(uri,
                calenderFields,
                selection,
                arrayOf(id.toString()),
                CalendarContract.Reminders.MINUTES
            )
        val eventReminders = arrayListOf<EventReminder>()
        if (cursor != null && cursor.moveToFirst()) {
            do {
                eventReminders.add(cursorToEventReminder(cursor))
            } while (cursor.moveToNext())
        } else {
            Log.i(LOG_TAG, "No reminders found for event!")
        }

        cursor?.close()
        return eventReminders;
    }

    private fun cursorToEventReminder(cursor: Cursor): EventReminder {

        val PROJECTION_INDEX_ID = 0
        val PROJECTION_INDEX_EVENT_ID = 0
        val PROJECTION_INDEX_MINUTES = 1


        val id: Long = cursor.getLong(PROJECTION_INDEX_ID)
        val eventId: Long = cursor.getLong(PROJECTION_INDEX_EVENT_ID)
        val minutes: Long = cursor.getLong(PROJECTION_INDEX_MINUTES)

        val eventReminder =
            EventReminder( id = id,
                eventId = eventId,
                minutes = minutes
            )
        return  eventReminder;
    }

    fun getEventsForReminder(reminderUri: Uri): List<CalenderEvent>? {

         val alertFields =
            arrayOf(
                CalendarContract.CalendarAlerts.EVENT_ID,
                CalendarContract.Events.CALENDAR_ID,
                CalendarContract.Events.TITLE,
                CalendarContract.Events.DTSTART,
                CalendarContract.Events.DTEND,
                CalendarContract.CalendarAlerts.ALARM_TIME,
                CalendarContract.CalendarAlerts.BEGIN,
                CalendarContract.CalendarAlerts.END,
                CalendarContract.Events.ALL_DAY,
                CalendarContract.Events.DURATION
            )

        if (!PermissionsManager.hasReadCalendar(context)) {
            Log.e(CalenderHandler.LOG_TAG, "No read permission for calender!!")
            return null
        }
        val ret = arrayListOf<CalenderEvent>()
        val alarmTime = reminderUri.lastPathSegment
        val selection = CalendarContract.CalendarAlerts.ALARM_TIME + "=?"

        val cursor: Cursor? =
            context.contentResolver.query(
                CalendarContract.CalendarAlerts.CONTENT_URI_BY_INSTANCE,
                alertFields,
                selection,
                arrayOf(alarmTime.toString()),
                null
            )

        if (cursor != null && cursor.moveToFirst()) {
            do {
               var event = cursorToEvent(cursor)
                ret.add(event)

            } while (cursor.moveToNext())
        } else {

        }
        cursor?.close()
        return ret
    }

    private fun cursorToEvent(cursor: Cursor): CalenderEvent {

        val PROJECTION_INDEX_EVENT_ID = 0
        val PROJECTION_INDEX_CALENDAR_ID = 1
        val PROJECTION_INDEX_TITLE = 2
        val PROJECTION_INDEX_DTSTART = 3
        val PROJECTION_INDEX_DTEND = 4
        val PROJECTION_INDEX_ALARM_TIME =5
        val PROJECTION_INDEX_INSTANCE_BEGIN =6
        val PROJECTION_INDEX_INSTANCE_END = 7
        val PROJECTION_INDEX_ALL_DAY = 8
        val PROJECTION_INDEX_DURATION = 9

        val eventId: Long = cursor.getLong(PROJECTION_INDEX_EVENT_ID)
        val title: String? = cursor.getString(PROJECTION_INDEX_TITLE)
        val calendarId: Long = cursor.getLong(PROJECTION_INDEX_CALENDAR_ID)
        val startTime: Long? = cursor.getLong(PROJECTION_INDEX_DTSTART)
        val endTime: Long? = cursor.getLong(PROJECTION_INDEX_DTEND)
        val instanceStart: Long? = cursor.getLong(PROJECTION_INDEX_INSTANCE_BEGIN)
        val instanceEnd: Long? = cursor.getLong(PROJECTION_INDEX_INSTANCE_END)
        val allDay: Int? = cursor.getInt(PROJECTION_INDEX_ALL_DAY)
        val alarmTime: Long? = cursor.getLong(PROJECTION_INDEX_ALARM_TIME)
        val duration: Long? = cursor.getLong(PROJECTION_INDEX_DURATION)

        val event =
            CalenderEvent( calendarId = calendarId,
                eventId = eventId,
                title = title,
                isAllDay = (allDay ?: 0) != 0,
                alarmTime = alarmTime ,
                startTime = startTime,
                endTime = endTime ?: 0L,
                instanceStart = instanceStart ?: 0L,
                instanceEnd = instanceEnd ?: 0L,
                duration=duration?:0L
            )
        return  event;
    }

    fun addReminderToEvent(id:Long) {
        val values = ContentValues().apply {
            put(CalendarContract.Reminders.MINUTES, 1)
            put(CalendarContract.Reminders.EVENT_ID, id)
            put(CalendarContract.Reminders.METHOD, CalendarContract.Reminders.METHOD_ALERT)
        }
        context.contentResolver.insert(CalendarContract.Reminders.CONTENT_URI, values)
    }



}