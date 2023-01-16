package com.chathurangashan.backgroundtasks.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.chathurangashan.backgroundtasks.database.dao.DeliveryHistoryDao
import com.chathurangashan.backgroundtasks.database.entities.DeliveryHistory

@Database(entities = [DeliveryHistory::class], version = 1)
abstract class AppDatabase : RoomDatabase() {
    abstract fun deliveryHistoryDao(): DeliveryHistoryDao
}