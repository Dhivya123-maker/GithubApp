package com.example.githubapp

import android.content.Context
import androidx.room.AutoMigration
import androidx.room.Database
import androidx.room.RenameColumn
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.migration.AutoMigrationSpec

@Database(
    version = 4,
    entities = [Item::class],
    exportSchema = true
)
abstract class ItemsDatabase : RoomDatabase() {
    abstract val itemsDao: ItemsDao


    companion object {
        @Volatile
        private var INSTANCE: ItemsDatabase? = null

        fun getInstance(context: Context): ItemsDatabase {
            synchronized(this) {
                var instance = INSTANCE
                if (instance == null) {
                    instance = Room.databaseBuilder(
                        context.applicationContext,
                        ItemsDatabase::class.java,
                        "items_database",

                    ).build()
                    INSTANCE = instance
                }
                return instance
            }
        }
    }
}
