package ru.kpfu.itis.paramonov.feature_leaderboards.presentation.adapter.settings

import android.widget.Filter

class DummyFilter: Filter() {

    override fun performFiltering(constraint: CharSequence?) = FilterResults()

    override fun publishResults(constraint: CharSequence?, results: FilterResults?) = Unit
}