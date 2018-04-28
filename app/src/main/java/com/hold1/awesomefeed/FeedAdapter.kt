package com.hold1.awesomefeed

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import kotlinx.android.synthetic.main.feed_item.view.*

/**
 * Created by Cristian Holdunu on 24/04/2018.
 */
class FeedAdapter : RecyclerView.Adapter<FeedAdapter.GifHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GifHolder {
        return GifHolder(LayoutInflater.from(parent.context).inflate(R.layout.feed_item, parent, false) as ShrinkingContainer)
    }

    override fun getItemCount(): Int {
        return 30
    }

    override fun onBindViewHolder(holder: GifHolder, position: Int) {
        holder.itemView.gif.layoutParams.height = 200
    }

    override fun onViewRecycled(holder: GifHolder) {
        (holder.itemView as ShrinkingContainer).clean()
        super.onViewRecycled(holder)
    }

    class GifHolder(itemView: ShrinkingContainer) : RecyclerView.ViewHolder(itemView) {

    }
}