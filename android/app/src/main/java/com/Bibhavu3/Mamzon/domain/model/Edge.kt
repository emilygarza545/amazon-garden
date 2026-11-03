package com.Bibhavu3.Mamzon.domain.model

enum class Edge {
    TOP,
    RIGHT,
    BOTTOM,
    LEFT;

    fun opposite(): Edge = entries[(ordinal + 2) % 4]

    fun turned(steps: Int): Edge = entries[(ordinal + steps) % 4]
}
