package com.unicorpdev.ktatract.database

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
    fun toUUID(uuid: String?): UUID? { return uuid?.let { UUID.fromString(it) } }

    @TypeConverter
    fun fromUUID(uuid: UUID?): String? { return uuid?.toString() }
}