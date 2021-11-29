package com.nasywa.newsapplication.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.nasywa.newsapplication.databinding.NewsItemBinding
import com.nasywa.newsapplication.model.ArticlesItem


class NewsAdapter (var context : Context, var ListNews :List<ArticlesItem?>?): RecyclerView.Adapter<NewsAdapter.NewsViewHolder>() {
    inner class NewsViewHolder(var itemBinding: NewsItemBinding):RecyclerView.ViewHolder(itemBinding.root)



    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NewsAdapter.NewsViewHolder {
       val itemNewsBinding = NewsItemBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return NewsViewHolder(itemNewsBinding)
    }

    override fun onBindViewHolder(holder: NewsAdapter.NewsViewHolder, position: Int) {
        val news = ListNews!![position]
        holder.itemBinding.apply {
            tvTitleItems.text = news?.title
            tvDateItem.text = news?.publishedAt
            tvDurationItem.text = news?.author
        }
        Glide.with(context).load(news?.urlToImage).centerCrop().into(holder.itemBinding.ivItemNews)
        holder.itemView.setOnClickListener{}

    }

    override fun getItemCount(): Int = ListNews!!.size
}

interface OnClickCallback{
    fun onItemClicked(newsArticlesItem: ArticlesItem)
}