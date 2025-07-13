package com.ovais.qrlab.utils.usecase


fun interface SuspendUseCase<ReturnType> {
    suspend operator fun invoke(): ReturnType
}

fun interface UseCase<ReturnType> {
    operator fun invoke(): ReturnType
}


fun interface SuspendParameterizedUseCase<ParamType, ReturnType> {
    suspend operator fun invoke(param: ParamType): ReturnType
}

fun interface ParameterizedUseCase<ParamType, ReturnType> {
    operator fun invoke(param: ParamType): ReturnType
}



