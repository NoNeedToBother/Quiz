package ru.kpfu.itis.paramonov.feature_users.presentation.viewholder

import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import ru.kpfu.itis.paramonov.common.model.presentation.UserModel
import ru.kpfu.itis.paramonov.feature_users.databinding.ItemFriendRequestBinding

class RequestViewHolder(
    private val binding: ItemFriendRequestBinding,
    private val onRequestAccepted: (String) -> Unit,
    private val onRequestDenied: (String) -> Unit,
    private val onRequestResolved: (Int) -> Unit
): RecyclerView.ViewHolder(binding.root) {

    private var item: UserModel? = null

    init {
        with(binding) {
            ivAccept.setOnClickListener {
                item?.let { user ->
                    onRequestAccepted.invoke(user.id)
                }
                onRequestResolved.invoke(adapterPosition)
            }
            ivDeny.setOnClickListener {
                item?.let {  user ->
                    onRequestDenied.invoke(user.id)
                }
                onRequestResolved.invoke(adapterPosition)
            }
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