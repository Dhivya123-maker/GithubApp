package com.example.githubapp

import android.content.Context
import android.content.Intent
import android.graphics.Paint
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.githubapp.Item
import com.example.githubapp.databinding.ItemListBinding
import com.example.githubapp.databinding.ShowListBinding
import com.squareup.picasso.Picasso

class ItemsAdapter() :
    ListAdapter<Item, ItemsAdapter.ItemViewHolder>(DiffItemCallBack()) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder =
        ItemViewHolder.inflateFrom(parent)

    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
        val item = getItem(position)
        holder.dataBind(item)
    }


    class ItemViewHolder(private val binding: ShowListBinding) :
        RecyclerView.ViewHolder(binding.root) {

        companion object {
            fun inflateFrom(parent: ViewGroup): ItemViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = ShowListBinding.inflate(layoutInflater, parent, false)
                return ItemViewHolder(binding)
            }


        }

        fun dataBind(
            item: Item,
        ) {

            binding.item = item
            binding.repoName.text = item.name
            binding.description.text = item.description
            binding.shareImg.visibility= View.GONE


        }
    }
}
