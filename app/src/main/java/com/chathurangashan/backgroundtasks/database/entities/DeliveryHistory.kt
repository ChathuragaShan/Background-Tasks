package com.chathurangashan.backgroundtasks.database.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class DeliveryHistory (
    @PrimaryKey(autoGenerate = true) val id: Long = 0L,
    @ColumnInfo(name = "delivery_address") val address: String,
    @ColumnInfo(name = "receiver_nic") val nic: String,
    @ColumnInfo(name = "delivery_type") val type: String,
    @ColumnInfo(name = "is_sync") val sync: Boolean = false
)