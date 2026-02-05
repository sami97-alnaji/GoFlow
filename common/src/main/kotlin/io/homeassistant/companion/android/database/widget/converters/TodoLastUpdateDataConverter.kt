package com.goflow.app.database.widget.converters

import androidx.room.TypeConverter
import com.goflow.app.common.util.kotlinJsonMapper
import com.goflow.app.database.widget.TodoWidgetEntity

class TodoLastUpdateDataConverter {
    @TypeConverter
    fun fromJson(value: String?): TodoWidgetEntity.LastUpdateData? {
        return value?.let { kotlinJsonMapper.decodeFromString<TodoWidgetEntity.LastUpdateData>(it) }
    }

    @TypeConverter
    fun toJson(data: TodoWidgetEntity.LastUpdateData?): String? {
        return data?.let { kotlinJsonMapper.encodeToString(it) }
    }
}
