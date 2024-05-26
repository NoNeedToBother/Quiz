package ru.kpfu.itis.paramonov.feature_users.presentation.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.ListAdapter
import ru.kpfu.itis.paramonov.common.model.presentation.UserModel
import ru.kpfu.itis.paramonov.feature_users.databinding.ItemUserBinding
import ru.kpfu.itis.paramonov.feature_users.presentation.adapter.diffutil.UserDiffUtilCallback
import ru.kpfu.itis.paramonov.feature_users.presentation.viewholder.UserViewHolder

class UserAdapter(
    diffUtilCallback: UserDiffUtilCallback,
    private val onUserClicked: (UserModel, ImageView) -> Unit
): ListAdapter<UserModel, UserViewHolder>(diffUtilCallback) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserViewHolder {
        return UserViewHolder(
            binding = ItemUserBinding.inflate(
                LayoutInflater.from(parent.context), parent, false
            ),
            onUserClicked = onUserClicked
        )
    }

    override fun onBindViewHolder(holder: UserViewHolder, position: Int) {
        holder.bindItem(getItem(position))
    }

}