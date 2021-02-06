package com.demo.restapi.models

import com.google.gson.annotations.SerializedName

class CardInfoDetails {
    var number: Number? = null
    var scheme: String? = null

    @SerializedName("prepaid")
    var isPrepaid = false
    var type: String? = null
    var brand: String? = null
    var bank: Bank? = null
    var country: Country? = null
}