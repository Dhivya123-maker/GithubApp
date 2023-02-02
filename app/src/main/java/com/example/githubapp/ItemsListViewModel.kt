package com.example.githubapp

import androidx.lifecycle.*
import kotlinx.coroutines.launch


class ItemsListViewModel(private val dao: ItemsDao) : ViewModel() {

    val items: LiveData<List<Item>> = dao.getAll()


    fun removeItem(item: Item) {
        viewModelScope.launch { dao.delete(item) }

    }


    fun addItem(newItem: Item) {
        val itemName = newItem.name
        val itemDesc = newItem.description

        val newItem = Item(itemName, itemDesc)
        viewModelScope.launch { dao.insert(newItem) }

    }



}