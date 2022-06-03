package com.example.minumobat.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import com.example.minumobat.BuildConfig
import com.example.minumobat.dao.schedule.ScheduleDao
import com.example.minumobat.model.schedule_model.ScheduleModel
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

@Database(entities = arrayOf(ScheduleModel::class), version = 1, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {

    // implement fungsi dari class interface shedule dao
    abstract fun scheduleDao(): ScheduleDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        // fungsi untuk mendapatkan instance
        // koneksi ke database sql lite
        fun getDatabase(ctx: Context): AppDatabase {
            val tempInstance = INSTANCE

            // jika sudah terinisialisasi
            // stop dan kembalikan hasil
            if (tempInstance != null) {
                return tempInstance
            }

            // fungsi untuk unisialisasi dengan
            // syncronisasion instance
            synchronized(this) {
                val instance = Room.databaseBuilder(ctx.applicationContext, AppDatabase::class.java, BuildConfig.DB_NAME)
                    .addCallback(appDatabaseCallback)
                    .build()

                INSTANCE = instance
                return instance
            }
        }

        // fungsi untuk mengecheck koneksi
        private val NUMBER_OF_THREADS = 4
        val databaseWriteExecutor: ExecutorService = Executors.newFixedThreadPool(NUMBER_OF_THREADS)
        private val appDatabaseCallback = object : RoomDatabase.Callback() {
            override fun onOpen(db: SupportSQLiteDatabase) {
                super.onOpen(db)
                databaseWriteExecutor.execute(Runnable {

                })
            }
        }
    }
}