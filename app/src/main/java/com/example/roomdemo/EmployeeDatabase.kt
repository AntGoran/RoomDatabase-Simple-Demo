package com.example.roomdemo

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [EmployeeEntity::class], version = 1)
abstract class EmployeeDatabase : RoomDatabase() {

    // Connect database to Dao
    abstract val employeeDao: EmployeeDao

    /* Companion object allows us to add functions on the employee
    database class.
    Classes can call employee database, get instance context to instantiate a new employee
     */
    companion object {

        @Volatile
        private var INSTANCE: EmployeeDatabase? = null

        fun getInstance(context: Context): EmployeeDatabase {
            /* Multiple threads can ask for database at the same time
               Ensure we only initialize it once by using synchronized function
             */
            synchronized(this) {
                var instance = INSTANCE

                if (instance == null) {
                    instance = Room.databaseBuilder(
                        context.applicationContext,
                        // Which database to build
                        EmployeeDatabase::class.java,
                        // Database you want to use
                        "employee_database"
                    )
                        .fallbackToDestructiveMigration()
                        .build()

                    INSTANCE = instance
                }
                return instance
            }
        }
    }
}