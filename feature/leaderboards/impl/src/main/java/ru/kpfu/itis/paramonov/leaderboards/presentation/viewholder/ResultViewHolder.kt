package ru.kpfu.itis.paramonov.leaderboards.presentation.viewholder

import android.animation.ObjectAnimator
import android.view.View
import android.view.animation.Animation
import android.widget.ImageView
import androidx.annotation.AnimRes
import androidx.core.view.ViewCompat
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import ru.kpfu.itis.paramonov.core.resources.ResourceManager
import ru.kpfu.itis.paramonov.core.utils.normalizeEnumName
import ru.kpfu.itis.paramonov.core.utils.gone
import ru.kpfu.itis.paramonov.core.utils.show
import ru.kpfu.itis.paramonov.leaderboards.R
import ru.kpfu.itis.paramonov.leaderboards.databinding.ItemResultBinding
import ru.kpfu.itis.paramonov.leaderboards.presentation.model.ResultUiModel

class ResultViewHolder(
    val binding: ItemResultBinding,
    private val resourceManager: ResourceManager,
    onResultClicked: (String, ImageView) -> Unit
): RecyclerView.ViewHolder(binding.root) {

    private var item: ResultUiModel? = null

    private var expanded = false

    init {
        binding.root.setOnClickListener {
            expanded = !expanded
            if (expanded) {
                showTimeAndRatio()
                showSettings()
            } else {
                hideTimeAndRatio()
                hideSettings()
            }
        }

        binding.ivProfilePicture.setOnClickListener {
            item?.let {
                onResultClicked.invoke(it.user.id, binding.ivProfilePicture)
            }
        }
        binding.tvUsername.setOnClickListener {
            item?.let {
                onResultClicked.invoke(it.user.id, binding.ivProfilePicture)
            }
        }
    }

    private fun hideTimeAndRatio() {
        binding.llTimeRatio.clearAnimation()
        with(binding.llTimeRatio) {
            playAnimation(this, R.anim.fade_anim, -24f, 500) {
                this.gone()
            }
        }
    }

    private fun hideSettings() {
        binding.llSettings.clearAnimation()
        with(binding.llSettings) {
            playAnimation(this, R.anim.fade_anim, -12f, 250) {
                this.gone()
            }
        }
    }

    private fun showTimeAndRatio() {
        with(binding.llTimeRatio) {
            show()
            playAnimation(this, R.anim.show_anim_slower, 22f, 500)
        }
    }

    private fun showSettings() {
        with(binding.llSettings) {
            show()
            playAnimation(this, R.anim.show_anim_faster, 10f, 250)
        }
    }

    private fun playAnimation(view: View, @AnimRes alphaAnimId: Int, translationY: Float,
                              moveDuration: Long, onAnimEnd: (() -> Unit)? = null) {
        with(view) {
            startAnimation(resourceManager.loadAnimation(alphaAnimId).apply {
                setAnimationListener(object : Animation.AnimationListener {
                    override fun onAnimationStart(p0: Animation?) {}

                    override fun onAnimationEnd(p0: Animation?) {
                        onAnimEnd?.invoke()
                    }

                    override fun onAnimationRepeat(p0: Animation?) {}
                })
            })
            ObjectAnimator.ofFloat(
                this,
                "translationY",
                resourceManager.pxToDp(translationY)
            ).apply {
                duration = moveDuration
                start()
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
            ViewCompat.setTransitionName(ivProfilePicture, "result_profile_picture_${adapterPosition}")
            val min = item.time / 60
            val sec = item.time % 60
            if (min != 0) tvTime.text = resourceManager.getString(
                R.string.time_with_min, min, sec
            ) else tvTime.text = resourceManager.getString(R.string.time, sec)
            tvRatio.text = resourceManager.getString(
                R.string.res_ratio, item.correct, item.total
            )
            tvDifficulty.text = resourceManager.getString(
                R.string.res_difficulty, item.difficulty.name.normalizeEnumName()
            )
            tvGameMode.text = resourceManager.getString(
                R.string.res_game_mode, item.gameMode.name.normalizeEnumName()
            )
            tvCategory.text = resourceManager.getString(
                R.string.res_category, item.category.name.normalizeEnumName()
            )
        }
    }

    private fun loadImage(url: String) {
        Glide.with(binding.root)
            .load(url)
            .error(ru.kpfu.itis.paramonov.ui.R.drawable.default_pfp)
            .centerCrop()
            .into(binding.ivProfilePicture)
    }
}