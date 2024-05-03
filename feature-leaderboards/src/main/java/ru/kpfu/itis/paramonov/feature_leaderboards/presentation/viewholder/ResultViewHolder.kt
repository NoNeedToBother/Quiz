package ru.kpfu.itis.paramonov.feature_leaderboards.presentation.viewholder

import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import ru.kpfu.itis.paramonov.common.resources.ResourceManager
import ru.kpfu.itis.paramonov.feature_leaderboards.R
import ru.kpfu.itis.paramonov.feature_leaderboards.databinding.ItemResultBinding
import ru.kpfu.itis.paramonov.feature_leaderboards.presentation.model.ResultUiModel

class ResultViewHolder(
    private val binding: ItemResultBinding,
    private val resourceManager: ResourceManager,
    onResultClicked: (String) -> Unit
): RecyclerView.ViewHolder(binding.root) {

    private var item: ResultUiModel? = null

    init {
        binding.root.setOnClickListener {
            item?.let {
                onResultClicked.invoke(it.user.id)
            }
        }
    }

    fun bindItem(item: ResultUiModel) {
        this.item = item

        with(binding) {
            tvUsername.text = item.user.username
            tvScore.text = resourceManager.getString(
                R.string.score_default, item.score
            )
            loadImage(item.user.profilePictureUrl)
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