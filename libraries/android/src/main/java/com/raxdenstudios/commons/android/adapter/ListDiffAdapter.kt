package com.raxdenstudios.commons.android.adapter

import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView

@Deprecated(
    message = "Will be removed in the next major version. " +
        "Use androidx.recyclerview.widget.ListAdapter with a DiffUtil.ItemCallback or Jetpack Compose lists.",
    level = DeprecationLevel.WARNING,
)
@Suppress("UnnecessaryAbstractClass")
abstract class ListDiffAdapter<T : Any, VH : RecyclerView.ViewHolder>(
    areItemsTheSame: (oldItem: T, newItem: T) -> Boolean,
    areContentsTheSame: (oldItem: T, newItem: T) -> Boolean = { oldItem, newItem -> oldItem == newItem },
) : ListAdapter<T, VH>(
    object : DiffUtil.ItemCallback<T>() {

        override fun areItemsTheSame(
            oldItem: T,
            newItem: T,
        ) = areItemsTheSame(oldItem, newItem)

        override fun areContentsTheSame(
            oldItem: T,
            newItem: T,
        ): Boolean = areContentsTheSame(oldItem, newItem)
    },
)
