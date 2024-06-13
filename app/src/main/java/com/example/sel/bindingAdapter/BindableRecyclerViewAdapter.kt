package com.example.sel.bindingAdapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.databinding.library.baseAdapters.BR
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.DiffUtil
import com.example.sel.interfaces.ItemBaseModel

class BindableRecyclerViewAdapter : RecyclerView.Adapter<BindableViewHolder>() {

    var itemBaseModels: List<ItemBaseModel> = emptyList()
    private val viewTypeToLayoutId: MutableMap<Int, Int> = mutableMapOf()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BindableViewHolder {
        val binding: ViewDataBinding = DataBindingUtil.inflate(
            LayoutInflater.from(parent.context),
            viewTypeToLayoutId[viewType] ?: 0,
            parent,
            false)
        return BindableViewHolder(binding)
    }

    override fun getItemViewType(position: Int): Int {
        val item = itemBaseModels[position]
        if (!viewTypeToLayoutId.containsKey(item.viewType)) {
            viewTypeToLayoutId[item.viewType] = item.layoutId
        }
        return item.viewType
    }

    override fun getItemCount(): Int = itemBaseModels.size

    override fun onBindViewHolder(holder: BindableViewHolder, position: Int) {
        holder.bind(itemBaseModels[position])
    }

    fun updateItemsDiffUtil(items: List<ItemBaseModel>?) {
        val diffResult = DiffUtil.calculateDiff(DiffUtilCallback(itemBaseModels, items ?: emptyList()), false)
        itemBaseModels = items.orEmpty().toMutableList()
        diffResult.dispatchUpdatesTo(this)
    }

    @SuppressLint("NotifyDataSetChanged")
    fun updateItems(items:List<ItemBaseModel>?){
        itemBaseModels = items?: emptyList()
        notifyDataSetChanged()
    }
}

class BindableViewHolder(private val binding: ViewDataBinding) : RecyclerView.ViewHolder(binding.root) {

    fun bind(itemBaseModel: ItemBaseModel) {
        binding.setVariable(BR.itemBaseModel, itemBaseModel)
    }
}

class DiffUtilCallback(
    val old: List<ItemBaseModel>,
    val new: List<ItemBaseModel>
) : DiffUtil.Callback() {
    override fun getOldListSize(): Int {
        return old.size
    }

    override fun getNewListSize(): Int {
        return new.size
    }

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        val oldItem = old[oldItemPosition]
        val newItem = new[newItemPosition]
        return newItem.areItemsTheSame(oldItem)
    }

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        val oldItem = old[oldItemPosition]
        val newItem = new[newItemPosition]
        return newItem.areContentsTheSame(oldItem)
    }
}


