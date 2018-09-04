package com.hold1.awesomefeed.benchmark

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.bumptech.glide.Glide
import com.hold1.awesomefeed.R
import kotlinx.android.synthetic.main.glide_item.view.*

/**
 * Created by Cristian Holdunu on 29/08/2018.
 */
class GlideAdapter(val items: Array<String>) : RecyclerView.Adapter<GlideAdapter.GlideViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GlideViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.glide_item, parent, false)
        return GlideViewHolder(view)
    }

    override fun getItemCount(): Int {
        return items.size
    }

    override fun onBindViewHolder(holder: GlideViewHolder, position: Int) {
        holder.bindImage(items[position])
    }

    class GlideViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bindImage(url: String) {
           Glide.with(itemView.context).load(url)
                   .into(itemView.gifView)
        }
    }
}