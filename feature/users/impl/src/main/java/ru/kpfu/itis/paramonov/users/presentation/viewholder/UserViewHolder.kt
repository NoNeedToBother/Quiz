package ru.kpfu.itis.paramonov.users.presentation.viewholder

import android.widget.ImageView
import androidx.core.view.ViewCompat
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import ru.kpfu.itis.paramonov.core.model.presentation.UserModel
import ru.kpfu.itis.paramonov.users.databinding.ItemUserBinding

class UserViewHolder(
    val binding: ItemUserBinding,
    private val onUserClicked: (UserModel, ImageView) -> Unit
): RecyclerView.ViewHolder(binding.root) {

    private var item: UserModel? = null

    init {
        binding.root.setOnClickListener {
            item?.let { user -> onUserClicked.invoke(user, binding.ivProfilePicture) }
        }
    }

    fun bindItem(item: UserModel) {
        this.item = item

        with(binding) {
            loadImage(item.profilePictureUrl)
            ViewCompat.setTransitionName(ivProfilePicture, "user_profile_picture_${adapterPosition}")
            tvId.text = item.id
            tvUsername.text = item.username
        }
    }

    private fun loadImage(url: String) {
        Glide.with(binding.root)
            .load(url)
            .placeholder(ru.kpfu.itis.paramonov.ui.R.drawable.default_pfp)
            .error(ru.kpfu.itis.paramonov.ui.R.drawable.default_pfp)
            .centerCrop()
            .into(binding.ivProfilePicture)
    }
}