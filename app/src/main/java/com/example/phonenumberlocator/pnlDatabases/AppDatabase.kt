package com.example.phonenumberlocator.pnlDatabases

import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import com.example.phonenumberlocator.PhoneNumberLocator
import com.example.phonenumberlocator.pnlDatabases.pnlDao.PhoneNumberDao
import com.example.phonenumberlocator.pnlDatabases.pnlEntity.PhoneNumberDatabase

@Database(entities = [PhoneNumberDatabase::class], version = 1, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {
    companion object {
        @Volatile
        private var instance: AppDatabase? = null
        operator fun invoke() = instance ?: synchronized(this) {
            instance ?: initialize().also {
                instance = it
            }
        }
        private fun initialize() =
            Room.databaseBuilder(PhoneNumberLocator.instance, AppDatabase::class.java, "trackLocation")
                .addMigrations(MIGRATE_1_2).build()

        fun destroyInstance() {
            instance = null
        }

        private val MIGRATE_1_2 = object : Migration(1, 2) {
            override fun migrate(database: SupportSQLiteDatabase) {
                database.execSQL(
                    """CREATE TABLE "last_location" (
                    	"id"	TEXT NOT NULL PRIMARY KEY,
                    	"user"	TEXT NOT NULL,
                    	"lat"	FLOAT NOT NULL,
                    	"lng"	FLOAT NOT NULL,
                    	"isGroup"	BOOLEAN NOT NULL
                    );
                """.trimIndent()
                )

            }
        }
    }
    abstract fun myGroupLocationDao(): PhoneNumberDao
}