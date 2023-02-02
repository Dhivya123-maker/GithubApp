package com.example.githubapp
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "items_table")
data class Item(
    @ColumnInfo val name: String,
    @ColumnInfo var description: String? = null,
    @PrimaryKey(autoGenerate = true) val Id: Int = 0
)
