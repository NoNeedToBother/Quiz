package ru.kpfu.itis.paramonov.feature_users.presentation.adapter.diffutil

import androidx.recyclerview.widget.DiffUtil
import ru.kpfu.itis.paramonov.core.model.presentation.UserModel

class UserDiffUtilCallback: DiffUtil.ItemCallback<UserModel>() {
    override fun areItemsTheSame(oldItem: UserModel, newItem: UserModel): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: UserModel, newItem: UserModel): Boolean {
        return oldItem.username == newItem.username
    }
}