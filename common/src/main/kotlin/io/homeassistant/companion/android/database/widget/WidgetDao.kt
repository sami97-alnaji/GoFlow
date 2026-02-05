package com.goflow.app.database.widget

import kotlinx.coroutines.flow.Flow

interface WidgetDao<T : WidgetEntity<T>> {
    suspend fun add(entity: T)
    suspend fun delete(id: Int)
    suspend fun deleteAll(ids: IntArray)
    fun getWidgetCountFlow(): Flow<Int>
}
