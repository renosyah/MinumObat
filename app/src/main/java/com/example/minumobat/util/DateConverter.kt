package com.example.minumobat.util

import androidx.room.TypeConverter
import java.sql.Date
import java.sql.Time

class DateConverter {
    @TypeConverter
    fun toDate(timestamp: Long): Date {
        return Date(timestamp)
    }

    @TypeConverter
    fun toTimestamp(date: Date): Long {
        return date.time
    }

    @TypeConverter
    fun toTime(timestamp: Long): Time {
        return Time(timestamp)
    }

    @TypeConverter
    fun toTimestamp(time: Time): Long {
        return time.time
    }
}