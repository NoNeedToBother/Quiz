package ru.kpfu.itis.paramonov.common_android.di

interface FeatureContainer {

    fun <T> getFeature(key: Class<*>): T

    fun releaseFeature(key: Class<*>)

    fun commonApi(): CommonApi
}