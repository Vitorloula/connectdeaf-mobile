package com.connectdeaf.network.dtos

data class State(
    val id: Int,
    val sigla: String,
    val nome: String
)

data class City(
    val id: Int,
    val nome: String
)
