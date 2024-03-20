package ru.kpfu.itis.paramonov.common.mapper

interface ModelMapper<M1, M2> {
    fun map(model: M1): M2
}