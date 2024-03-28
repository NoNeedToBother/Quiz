package ru.kpfu.itis.paramonov.common.di

import ru.kpfu.itis.paramonov.common.di.dependencies.CommonApi

interface FeatureContainer {

    fun <T> getFeature(key: Class<*>): T

    fun releaseFeature(key: Class<*>)

    fun commonApi(): CommonApi
}