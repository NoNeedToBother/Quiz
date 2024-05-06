package ru.kpfu.itis.paramonov.common_android.di

import ru.kpfu.itis.paramonov.common.api.CommonApi
import ru.kpfu.itis.paramonov.common.api.ContextApi

interface FeatureContainer {

    fun <T> getFeature(key: Class<*>): T

    fun releaseFeature(key: Class<*>)

    fun commonApi(): CommonApi

    fun contextApi(): ContextApi
}