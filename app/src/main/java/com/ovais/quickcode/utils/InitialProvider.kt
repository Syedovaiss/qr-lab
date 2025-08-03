package com.ovais.quickcode.utils

fun interface InitialProvider {
    operator fun invoke(name: String): String
}

class DefaultInitialProvider : InitialProvider {
    override fun invoke(name: String): String {
        return name.trim().split(" ")
            .take(2)
            .mapNotNull { it.firstOrNull()?.uppercase() }
            .joinToString("")
    }
}