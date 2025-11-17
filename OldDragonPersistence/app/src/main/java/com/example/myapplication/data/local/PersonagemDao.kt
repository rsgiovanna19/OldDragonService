package com.example.myapplication.data.local

import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface `PersonagemDao` {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(personagem: PersonagemEntity): Long

    @Update
    suspend fun update(personagem: PersonagemEntity)

    @Delete
    suspend fun delete(personagem: PersonagemEntity)

    @Query("SELECT * FROM personagens ORDER BY createdAt DESC")
    fun getAll(): Flow<List<PersonagemEntity>>

    @Query("SELECT * FROM personagens WHERE id = :id LIMIT 1")
    suspend fun getById(id: Long): PersonagemEntity?
}
