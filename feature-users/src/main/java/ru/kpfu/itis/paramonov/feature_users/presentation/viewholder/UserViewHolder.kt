package ru.kpfu.itis.paramonov.feature_users.presentation.viewholder

import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import ru.kpfu.itis.paramonov.common.model.presentation.UserModel
import ru.kpfu.itis.paramonov.feature_users.databinding.ItemUserBinding

class UserViewHolder(
    private val binding: ItemUserBinding,
    private val onUserClicked: (UserModel) -> Unit
): RecyclerView.ViewHolder(binding.root) {

    private var item: UserModel? = null

    init {
        binding.root.setOnClickListener {
            item?.let { user -> onUserClicked.invoke(user) }
        }
    }

    fun bindItem(item: UserModel) {
        this.item = item

        with(binding) {
            loadImage(item.profilePictureUrl)
            tvId.text = item.id
            tvUsername.text = item.username
        }
    }

    private fun loadImage(url: String) {
        Glide.with(binding.root)
            .load(url)
            .placeholder(ru.kpfu.itis.paramonov.common_android.R.drawable.default_pfp)
            .error(ru.kpfu.itis.paramonov.common_android.R.drawable.default_pfp)
            .centerCrop()
            .into(binding.ivProfilePicture)
    }
}