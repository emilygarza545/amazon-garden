package com.Bibhavu3.Mamzon.domain.model

data class HydrationReport(
    val hydrated: Set<Int>,
    val drowned: Set<Int>,
    val required: Int
) {
    val solved: Boolean = required > 0 && hydrated.size == required && drowned.isEmpty()
}
