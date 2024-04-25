package ru.kpfu.itis.paramonov.feature_questions.presentation.questions.utils

import android.view.View
import androidx.viewpager2.widget.ViewPager2
import javax.inject.Inject
import kotlin.math.abs
import kotlin.math.pow

class QuestionViewPagerTransformer @Inject constructor(): ViewPager2.PageTransformer {

    override fun transformPage(page: View, position: Float) {
        page.apply {
            when {
                position < -1 -> alpha = 0f
                position <= 0 -> {
                    alpha = 1 - position.pow(2)
                    rotation = position * 60f
                    translationX = 0f
                    scaleX = 1f
                    scaleY = 1f
                }
                position <= 1 -> {
                    alpha = 1 - position
                    translationX = width * -position
                    val scaleFactor = (MIN_SCALE + (1 - MIN_SCALE) * (1 - abs(position)))
                    rotation = 0f
                    scaleX = scaleFactor
                    scaleY = scaleFactor
                }
                else -> alpha = 0f
            }
        }
    }

    companion object {
        private const val MIN_SCALE = 0.8f
    }

}