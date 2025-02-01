package ru.kpfu.itis.paramonov.profiles.presentation.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import ru.kpfu.itis.paramonov.core.model.presentation.UserModel
import ru.kpfu.itis.paramonov.profiles.databinding.ItemFriendRequestBinding
import ru.kpfu.itis.paramonov.profiles.presentation.adapter.diffutil.RequestDiffUtilCallback
import ru.kpfu.itis.paramonov.profiles.presentation.viewholder.RequestViewHolder

class RequestAdapter(
    diffUtilCallback: RequestDiffUtilCallback,
    private val onRequestAccepted: (String) -> Unit,
    private val onRequestDenied: (String) -> Unit,
    private val onRequestResolved: (Int) -> Unit
): ListAdapter<UserModel, RequestViewHolder>(diffUtilCallback) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RequestViewHolder {
        return RequestViewHolder(
            binding = ItemFriendRequestBinding.inflate(
                LayoutInflater.from(parent.context), parent, false
            ),
            onRequestAccepted = onRequestAccepted,
            onRequestDenied = onRequestDenied,
            onRequestResolved = onRequestResolved
        )
    }

    override fun onBindViewHolder(holder: RequestViewHolder, position: Int) {
        holder.bindItem(getItem(position))
    }
}