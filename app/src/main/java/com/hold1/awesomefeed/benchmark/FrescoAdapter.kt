package com.hold1.awesomefeed.benchmark

import android.net.Uri
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.facebook.drawee.backends.pipeline.Fresco
import com.hold1.awesomefeed.R
import kotlinx.android.synthetic.main.fresco_item.view.*

/**
 * Created by Cristian Holdunu on 29/08/2018.
 */
class FrescoAdapter(val items: Array<String>) : RecyclerView.Adapter<FrescoAdapter.FrescoViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FrescoViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.fresco_item, parent, false)
        return FrescoViewHolder(view)
    }

    override fun getItemCount(): Int {
        return items.size
    }

    override fun onBindViewHolder(holder: FrescoViewHolder, position: Int) {
        holder.bindImage(items[position])
    }

    class FrescoViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bindImage(url: String) {
            val controller = Fresco.newDraweeControllerBuilder()
                    .setUri(Uri.parse(url))
                    .setAutoPlayAnimations(true)
                    .build()
            itemView.gifView.controller = controller
            
        }
    }
}