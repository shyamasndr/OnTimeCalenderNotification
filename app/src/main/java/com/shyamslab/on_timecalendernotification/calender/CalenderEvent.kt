package com.shyamslab.on_timecalendernotification.calender

class  CalenderEvent(
    val calendarId: Long,
    val eventId: Long,
    var title: String?,
    var isAllDay: Boolean,
    var alarmTime: Long?,
    var startTime: Long?,
    var endTime: Long?,
    val instanceStart: Long?,
    val instanceEnd: Long?,
    val duration:Long

)
{

}