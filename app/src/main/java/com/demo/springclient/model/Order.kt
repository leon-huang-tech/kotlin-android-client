package com.demo.springclient.model

data class Order(
    val id: Long,
    val userId: Long,
    val product: String,
    val amount: Double,
    val status: String
)