package ru.kpfu.itis.paramonov.questions.presentation.settings.adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter
import ru.kpfu.itis.paramonov.questions.presentation.settings.fragments.QuestionSettingsFragment
import ru.kpfu.itis.paramonov.questions.presentation.settings.fragments.TrainingQuestionSettingsFragment

class QuestionSettingsViewPagerAdapter(
    fragmentManager: FragmentManager,
    lifecycle: Lifecycle,
): FragmentStateAdapter(fragmentManager, lifecycle) {
    override fun getItemCount(): Int = TAB_QUESTION_SETTINGS_COUNT

    override fun createFragment(position: Int): Fragment {
        return when(position) {
            STANDARD_QUESTIONS_SETTINGS_POS -> QuestionSettingsFragment()
            TRAINING_QUESTION_SETTINGS_POS -> TrainingQuestionSettingsFragment()
            else -> throw RuntimeException("Unsupported fragment")
        }
    }

    companion object {
        private const val TAB_QUESTION_SETTINGS_COUNT = 2
        private const val STANDARD_QUESTIONS_SETTINGS_POS = 0
        private const val TRAINING_QUESTION_SETTINGS_POS = 1
    }
}