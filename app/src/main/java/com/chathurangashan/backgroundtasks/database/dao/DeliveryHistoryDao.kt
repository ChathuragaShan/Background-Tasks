package com.chathurangashan.backgroundtasks.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.chathurangashan.backgroundtasks.database.entities.DeliveryHistory

@Dao
interface DeliveryHistoryDao {

    @Query("SELECT * FROM deliveryHistory")
    suspend fun getAll(): List<DeliveryHistory>

    @Query("SELECT * FROM deliveryHistory WHERE is_sync = false")
    suspend fun getAllUnSync(): List<DeliveryHistory>

    @Insert
    suspend fun insert(history: DeliveryHistory)

    @Query("UPDATE deliveryHistory SET is_sync = true WHERE is_sync = false")
    suspend fun updateSyncStatus()
}