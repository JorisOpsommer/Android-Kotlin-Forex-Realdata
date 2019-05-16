package com.joris.clientapp.models

import com.squareup.moshi.Json

/**
 * When we get the JSON query of one stock it looks like the following:
 * {"quote":{"symbol":"AAPL","companyName":"Apple}}
 * We have a quote object which contains the information of our stock. Therefor we use 2 classes to have a class in a class.
 * We acces the root quote object in this class.
 */
class CompanyStock(
    @field:Json(name = "quote")  val datastock : DataStock
){}


/**
 * Uses the stockInterface, when we want to expand this app. We could potentially add cryptocurrencies or other stocks (=not FOREX).
 * They will difference from Datastock but will have some of the same attributes
 * This class gives us more information about one Datastock class. We have quote object and multiple attributes for example volume.
 * This class gets the attributes in our qoute (=stock).
 */
class DataStock(
    @field:Json(name = "symbol") override val nameSymbol: String,
    @field:Json(name = "companyName") override val nameCompany: String,
    @field:Json(name = "primaryExchange") override val primaryExchange: String,
    @field:Json(name = "latestVolume") override val volume: Double,
    @field:Json(name = "avgTotalVolume") override val avgVolume: Double,
    @field:Json(name = "latestPrice") override val currentPrice: Double,
    @field:Json(name = "high") override val high: Double,
    @field:Json(name = "low") override val low: Double

) :StockInterface {}



