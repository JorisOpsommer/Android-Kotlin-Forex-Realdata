package com.joris.clientapp.models

/**
 * Interface is made to make this app future proof, when crypto or other stocks get added, they will use this interface.
 */
interface StockInterface {
    val nameSymbol: String
    val nameCompany: String
    val currentPrice: Double
    val primaryExchange: String
    val high: Double
    val low: Double
    val volume: Double
    val avgVolume: Double
}