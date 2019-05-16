package com.joris.clientapp.models

import com.squareup.moshi.Json

/**
 * This class is used to display stocks our namelistadapter. We only need 2 attributes, name and symbol of a stock.
 * The reason we have a name and namecompany is because of the API we use. Some calls return a symbol + name. Some calls return name + Companyname.
 * This is done because there is difference in List to show and in a stock info object (=quote)
 * We want to treat them as the same object to make this, again, future proof. We want to be able to display Crypto, Forex, SPX, NDX all in the same list.
 * One of the two (name or namecompany) attributes will always be empty. Solution to this is to check with an if and use the not null value.
 * When converting the Namelist item to for example a Companystock item we use the symbol send a getrequest to get the Quote object.
 *
 */
class NameList (

   @field:Json(name = "symbol") val nameSymbol: String,
   @field:Json(name = "name") val nameCompany: String,
   @field:Json(name = "companyName") val name: String

){}
