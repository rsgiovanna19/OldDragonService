package com.example.myapplication.data.local

import kotlinx.coroutines.flow.Flow

class `PersonagemRepository`(private val dao: PersonagemDao) {
    suspend fun insert(entity: PersonagemEntity): Long = dao.insert(entity)
    suspend fun update(entity: PersonagemEntity) = dao.update(entity)
    suspend fun delete(entity: PersonagemEntity) = dao.delete(entity)
    fun getAll(): Flow<List<PersonagemEntity>> = dao.getAll()
    suspend fun getById(id: Long): PersonagemEntity? = dao.getById(id)
}
