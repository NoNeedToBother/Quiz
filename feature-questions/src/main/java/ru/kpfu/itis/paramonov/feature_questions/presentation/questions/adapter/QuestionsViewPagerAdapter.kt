package ru.kpfu.itis.paramonov.feature_questions.presentation.questions.adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter
import ru.kpfu.itis.paramonov.feature_questions.presentation.questions.fragments.QuestionFragment
import ru.kpfu.itis.paramonov.feature_questions.presentation.questions.model.QuestionDataUiModel

class QuestionsViewPagerAdapter(
    fragmentManager: FragmentManager,
    lifecycle: Lifecycle,
    private val questionList: List<QuestionDataUiModel>
): FragmentStateAdapter(fragmentManager, lifecycle) {

    override fun getItemCount(): Int {
        return questionList.size
    }

    override fun createFragment(position: Int): Fragment {
        return QuestionFragment.newInstance(position)
    }
}