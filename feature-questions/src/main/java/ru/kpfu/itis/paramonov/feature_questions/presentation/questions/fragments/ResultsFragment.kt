package ru.kpfu.itis.paramonov.feature_questions.presentation.questions.fragments

import by.kirich1409.viewbindingdelegate.viewBinding
import ru.kpfu.itis.paramonov.common_android.ui.base.BaseFragment
import ru.kpfu.itis.paramonov.feature_questions.R
import ru.kpfu.itis.paramonov.feature_questions.databinding.FragmentResultsBinding
import kotlin.ClassCastException

class ResultsFragment: BaseFragment(R.layout.fragment_results) {

    private val binding: FragmentResultsBinding by viewBinding(FragmentResultsBinding::bind)

    override fun inject() {}

    override fun initView() {
        showData()
    }

    private fun showData() {
        try {
            val arguments = requireArguments()
            val difficulty = arguments.getString(ARGS_DIFFICULTY_KEY) as String
            val category = arguments.getString(ARGS_CATEGORY_KEY) as String
            val gameMode = arguments.getString(ARGS_GAME_MODE_KEY) as String
            val time = arguments.getInt(ARGS_TIME_KEY, INT_DEFAULT_VALUE)
            val correct = arguments.getInt(ARGS_CORRECT_AMOUNT_KEY, INT_DEFAULT_VALUE)
            val total = arguments.getInt(ARGS_TOTAL_AMOUNT_KEY, INT_DEFAULT_VALUE)
            val score = arguments.getDouble(ARGS_SCORE_KEY, DOUBLE_DEFAULT_VALUE)

            if (time == INT_DEFAULT_VALUE || correct == INT_DEFAULT_VALUE ||
                total == INT_DEFAULT_VALUE || score == DOUBLE_DEFAULT_VALUE) {
                throw NullPointerException()
            }
            showInfoOnScreen(difficulty, category, gameMode, time, correct, total, score)
        } catch (ex: Throwable) {
            when(ex) {
                is NullPointerException, is ClassCastException -> showErrorBottomSheetDialog(
                    getString(ru.kpfu.itis.paramonov.common_android.R.string.empty),
                    getString(R.string.unsupported_arguments)
                )
                else -> showErrorBottomSheetDialog(
                    getString(ru.kpfu.itis.paramonov.common_android.R.string.empty),
                    getString(ru.kpfu.itis.paramonov.common_android.R.string.default_error_msg)
                )
            }
        }
    }

    private fun showInfoOnScreen(difficulty: String, category: String, gameMode: String,
                                 time: Int, correct: Int, total: Int, score: Double) {
        with(binding) {
            tvDifficulty.text = getString(R.string.res_difficulty, difficulty)
            tvCategory.text = getString(R.string.res_category, category)
            tvGameMode.text = getString(R.string.res_game_mode, gameMode)
            tvTime.text = getString(R.string.res_time, time)
            tvRatio.text = getString(R.string.res_ratio, correct, total)
            tvScore.text = getString(R.string.res_score, score)
        }
    }

    override fun observeData() {}

    companion object {
        const val ARGS_DIFFICULTY_KEY = "difficulty"
        const val ARGS_CATEGORY_KEY = "category"
        const val ARGS_GAME_MODE_KEY = "game_mode"
        const val ARGS_TIME_KEY = "time"
        const val ARGS_CORRECT_AMOUNT_KEY = "correct"
        const val ARGS_TOTAL_AMOUNT_KEY = "total"
        const val ARGS_SCORE_KEY = "score"

        private const val INT_DEFAULT_VALUE = -1
        private const val DOUBLE_DEFAULT_VALUE = -1.0
    }
}