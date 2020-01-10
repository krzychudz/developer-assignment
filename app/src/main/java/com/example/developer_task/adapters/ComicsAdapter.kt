package com.example.developer_task.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.developer_task.R
import com.example.developer_task.adapters.viewholders.BaseViewHolder
import com.example.developer_task.adapters.viewholders.ComicsViewHolder
import com.example.developer_task.adapters.viewholders.PaginationProgressBarViewHolder
import com.example.developer_task.models.ComicsModel
import com.example.developer_task.models.ThumbnailModel
import com.example.developer_task.viewmodels.comics.ComicsViewModel

class ComicsAdapter(private val context: Context) :
    RecyclerView.Adapter<BaseViewHolder>() {

    private var adapterData: ArrayList<ComicsViewModel> = ArrayList()

    private val VIEW_TYPE_LOADING = 0
    private val VIEW_TYPE_NORMAL = 1
    var isProgressBarVisible = false

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseViewHolder {
        return when (viewType) {
            VIEW_TYPE_LOADING -> {
                PaginationProgressBarViewHolder(
                    LayoutInflater.from(parent.context)
                        .inflate(R.layout.item_pagination_progress, parent, false)
                )
            }
            VIEW_TYPE_NORMAL -> {
                ComicsViewHolder(
                    LayoutInflater.from(parent.context)
                        .inflate(R.layout.item_comics, parent, false)
                )
            }
            else -> {
                ComicsViewHolder(
                    LayoutInflater.from(parent.context)
                        .inflate(R.layout.item_comics, parent, false)
                )
            }
        }
    }

    override fun getItemViewType(position: Int): Int {
        return if (isProgressBarVisible) {
            if (position == adapterData.size - 1)
                VIEW_TYPE_LOADING
            else
                VIEW_TYPE_NORMAL
        } else
            VIEW_TYPE_NORMAL
    }

    override fun getItemCount(): Int {
        return adapterData.size
    }

    override fun onBindViewHolder(holder: BaseViewHolder, position: Int) {
        if (holder is ComicsViewHolder) {
            holder.comicsTitle.text = adapterData[position].title
            holder.comicsDescription.text = adapterData[position].description
            Glide.with(context).load(adapterData[position].thumbnailUrl).into(holder.comicsImage)
        }
    }

    fun addAdapterData(adapterNewData: List<ComicsViewModel>) {
        adapterData.addAll(adapterNewData)
        notifyDataSetChanged()
    }

    fun setAdapterData(adapterNewData: List<ComicsViewModel>) {
        adapterData.clear()
        adapterData.addAll(adapterNewData)
        notifyDataSetChanged()
    }


    fun addProgressView() {
        isProgressBarVisible = true
        adapterData.add(ComicsViewModel(ComicsModel("", "", ThumbnailModel("",""))))
        notifyItemInserted(adapterData.size - 1)
    }

    fun removeProgressView() {
        isProgressBarVisible = false
        val position = adapterData.size - 1
        if (position >= 0) {
            adapterData.removeAt(position)
            notifyDataSetChanged()
        }
    }
}