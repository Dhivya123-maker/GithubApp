package com.example.githubapp

import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface ItemsDao {
    @Query("SELECT * FROM items_table ORDER BY Id DESC")
    fun getAll(): LiveData<List<Item>>


    @Insert
    suspend fun insert(item: Item)

    @Delete
    suspend fun delete(item: Item)

    @Update
    suspend fun update(item: Item)

    @Query("SELECT * FROM items_table")
    fun getAllRepos(): LiveData<List<Item>>


}
