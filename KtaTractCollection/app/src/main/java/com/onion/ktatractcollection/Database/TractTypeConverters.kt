package com.onion.ktatractcollection.Database

import android.net.Uri
import androidx.room.TypeConverter
import java.util.*

class TractTypeConverters {

    @TypeConverter
    fun fromDate(date: Date?): Long? { return date?.time }

    @TypeConverter
    fun toDate(time: Long?): Date? {
        return time?.let { Date(it) }
    }

    @TypeConverter
    fun toUUID(uuid: String?): UUID? { return UUID.fromString(uuid) }

    @TypeConverter
    fun fromUUID(uuid: UUID?): String? { return uuid?.toString() }
}