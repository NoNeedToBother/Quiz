package ru.kpfu.itis.paramonov.feature_questions.presentation.questions.fragments

import android.graphics.drawable.Drawable
import android.graphics.drawable.LayerDrawable
import androidx.annotation.DrawableRes
import androidx.appcompat.content.res.AppCompatResources
import by.kirich1409.viewbindingdelegate.viewBinding
import ru.kpfu.itis.paramonov.core.ui.base.BaseFragment
import ru.kpfu.itis.paramonov.core.ui.di.FeatureUtils
import ru.kpfu.itis.paramonov.core.utils.collect
import ru.kpfu.itis.paramonov.feature_questions.R
import ru.kpfu.itis.paramonov.feature_questions.databinding.FragmentResultBinding
import ru.kpfu.itis.paramonov.feature_questions.di.FeatureQuestionsComponent
import ru.kpfu.itis.paramonov.feature_questions.di.FeatureQuestionsDependencies
import ru.kpfu.itis.paramonov.feature_questions.presentation.questions.model.ResultScoreUiModel
import ru.kpfu.itis.paramonov.feature_questions.presentation.questions.viewmodel.ResultViewModel
import javax.inject.Inject
import kotlin.ClassCastException

class ResultFragment: BaseFragment(R.layout.fragment_result) {

    private val binding: FragmentResultBinding by viewBinding(FragmentResultBinding::bind)

    @Inject
    lateinit var viewModel: ResultViewModel

    override fun inject() {
        FeatureUtils.getFeature<FeatureQuestionsComponent>(this, FeatureQuestionsDependencies::class.java)
            .resultComponentFactory()
            .create(this)
            .inject(this)
    }

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
            /*when(ex) {
                is NullPointerException, is ClassCastException -> showErrorBottomSheetDialog(
                    getString(ru.kpfu.itis.paramonov.common_android.R.string.empty),
                    getString(R.string.unsupported_arguments)
                )
                else -> showErrorBottomSheetDialog(
                    getString(ru.kpfu.itis.paramonov.common_android.R.string.empty),
                    getString(ru.kpfu.itis.paramonov.common_android.R.string.default_error_msg)
                )
            }*/
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

    override fun observeData() {
        viewModel.getResultScore()
        viewModel.maxScoreFlow.collect(lifecycleOwner = viewLifecycleOwner) {
            collectMaxScoreData(it)
        }
    }

    private fun collectMaxScoreData(maxScore: Double?) {
        maxScore?.let {
            val score = requireArguments().getDouble(ARGS_SCORE_KEY)
            val resultScore = getMaxResultScoreUiModel(score, maxScore)
            showResultScore(resultScore)
        }
    }

    private fun getDrawable(@DrawableRes resId: Int): Drawable? {
        return AppCompatResources.getDrawable(requireContext(), resId)
    }

    private fun showResultScore(resultScoreUiModel: ResultScoreUiModel) {
        val drawableScore = when(resultScoreUiModel) {
            ResultScoreUiModel.A -> getDrawable(R.drawable.a)
            ResultScoreUiModel.B -> getDrawable(R.drawable.b)
            ResultScoreUiModel.C -> getDrawable(R.drawable.c)
            ResultScoreUiModel.D -> getDrawable(R.drawable.d)
            ResultScoreUiModel.E -> getDrawable(R.drawable.e)
            ResultScoreUiModel.F -> getDrawable(R.drawable.f)
        }
        val gradation = when(resultScoreUiModel.gradation) {
            ResultScoreUiModel.GradationUiModel.PLUS -> getDrawable(R.drawable.plus)
            ResultScoreUiModel.GradationUiModel.MINUS -> getDrawable(R.drawable.minus)
            ResultScoreUiModel.GradationUiModel.DEFAULT -> null
        }
        drawableScore?.let { score ->
            val drawables = gradation?.let {  grad ->
                arrayOf(score, grad)
            } ?: arrayOf(score)
            val layerDrawable = LayerDrawable(drawables)
            binding.ivScore.setImageDrawable(layerDrawable)
        }
    }

    private fun getMaxResultScoreUiModel(score: Double, maxScore: Double): ResultScoreUiModel {
        val ratio = score / maxScore
        val model = when {
            ratio <= F_MAX_RATIO -> ResultScoreUiModel.F.apply {
                gradation = getGradation(0.0, F_MAX_RATIO, ratio)
            }
            ratio <= E_MAX_RATIO -> ResultScoreUiModel.E.apply {
                gradation = getGradation(F_MAX_RATIO, E_MAX_RATIO, ratio)
            }
            ratio <= D_MAX_RATIO -> ResultScoreUiModel.D.apply {
                gradation = getGradation(E_MAX_RATIO, D_MAX_RATIO, ratio)
            }
            ratio <= C_MAX_RATIO -> ResultScoreUiModel.C.apply {
                gradation = getGradation(D_MAX_RATIO, C_MAX_RATIO, ratio)
            }
            ratio <= B_MAX_RATIO -> ResultScoreUiModel.B.apply {
                gradation = getGradation(C_MAX_RATIO, B_MAX_RATIO, ratio)
            }
            else -> ResultScoreUiModel.A.apply {
                gradation = getGradation(B_MAX_RATIO, 1.0, ratio)
            }
        }
        return model
    }

    private fun getGradation(minValue: Double, maxValue: Double, value: Double): ResultScoreUiModel.GradationUiModel {
        val absoluteDelta = maxValue - minValue
        val delta = value - minValue
        return (delta / absoluteDelta).let {
            when {
                it <= MINUS_GRADATION_MAX_RATIO -> ResultScoreUiModel.GradationUiModel.MINUS
                it >= PLUS_GRADATION_MIN_RATION -> ResultScoreUiModel.GradationUiModel.PLUS
                else -> ResultScoreUiModel.GradationUiModel.DEFAULT
            }
        }
    }

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

        private const val F_MAX_RATIO = 0.3
        private const val E_MAX_RATIO = 0.4
        private const val D_MAX_RATIO = 0.5
        private const val C_MAX_RATIO = 0.625
        private const val B_MAX_RATIO = 0.75

        private const val MINUS_GRADATION_MAX_RATIO = 0.25
        private const val PLUS_GRADATION_MIN_RATION = 0.75
    }
}