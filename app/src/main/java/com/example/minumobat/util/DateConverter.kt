package com.example.minumobat.util

import androidx.room.TypeConverter
import java.sql.Date
import java.sql.Time

// kelas konverter
// dari tipe data time ke long dan sebaliknya
// dari tipe data date ke long dan sebaliknya
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