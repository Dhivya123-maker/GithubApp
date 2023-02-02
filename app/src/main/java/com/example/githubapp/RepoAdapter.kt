package com.example.githubapp

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class RepoAdapter(var context: Context, var repoModelList: List<RepoModel>) :
    RecyclerView.Adapter<RepoAdapter.ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view1 = LayoutInflater.from(parent.context).inflate(R.layout.item_list, parent, false)
        return ViewHolder(view1)
    }


    override fun onBindViewHolder(holder: ViewHolder, @SuppressLint("RecyclerView") position: Int) {
        holder.repo_name.setText(repoModelList[position].getRepoNames())
        holder.desc.setText(repoModelList[position].getDescs())
    }

    override fun getItemCount(): Int {
        return repoModelList.size
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var repo_name: TextView
        var desc: TextView
        var share: ImageView
        private lateinit var viewModel: ItemsListViewModel


        init {

            repo_name = itemView.findViewById(R.id.repo_name)
            desc = itemView.findViewById(R.id.description)
            share = itemView.findViewById(R.id.share_img)
            share.setOnClickListener {
                val position = adapterPosition
                val url: String = repoModelList[position].getUrls()
                val sendIntent = Intent()
                sendIntent.action = Intent.ACTION_SEND
                var shareMessage = """
                    Repository name : ${repo_name.text}
                      """.trimIndent()
                sendIntent.type = "text/plain"
                shareMessage = shareMessage + "Url : " + url
                sendIntent.putExtra(Intent.EXTRA_TEXT, shareMessage)
                Intent.createChooser(sendIntent, "Share via")
                context.startActivity(sendIntent)
            }
            itemView.setOnClickListener {
                val position = adapterPosition
                val url: String = repoModelList[position].getUrls()
                val browserIntent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
                context.startActivity(browserIntent)
            }
        }
    }
}