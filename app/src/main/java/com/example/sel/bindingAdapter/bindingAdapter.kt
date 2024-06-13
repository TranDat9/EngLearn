package com.example.sel.bindingAdapter

import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.sel.base.RecyclerViewLoadMoreScroll
import com.example.sel.interfaces.ItemBaseModel


@BindingAdapter(value = ["itemModels"])
fun bindItemModels(recyclerView: RecyclerView, itemModels: List<ItemBaseModel>?) {
    val adapter = getOrCreateAdapter(recyclerView = recyclerView)
        adapter.updateItems(itemModels)
}

@BindingAdapter(value = ["itemLoadMoreModels", "loadMore"], requireAll = false)
fun bindItemLoadMoreModels(recyclerView: RecyclerView, itemLoadMoreModels: List<ItemBaseModel>?, loadMore: RecyclerViewLoadMoreScroll? = null) {
    val adapter = getOrCreateAdapter(recyclerView = recyclerView, loadMore = loadMore)
        adapter.updateItemsDiffUtil(itemLoadMoreModels)
}
private fun getOrCreateAdapter(recyclerView: RecyclerView, loadMore: RecyclerViewLoadMoreScroll? = null): BindableRecyclerViewAdapter {
    val bindableAdapter = if (recyclerView.adapter != null && recyclerView.adapter is BindableRecyclerViewAdapter) {
        recyclerView.adapter as BindableRecyclerViewAdapter
    } else {
        val bindableRecyclerAdapter = BindableRecyclerViewAdapter()
        recyclerView.adapter = bindableRecyclerAdapter
        bindableRecyclerAdapter
    }
    loadMore?.let{
        recyclerView.addOnScrollListener(loadMore)
    }         
    return bindableAdapter
}