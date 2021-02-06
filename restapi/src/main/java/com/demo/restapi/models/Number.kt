package com.demo.restapi.models

import com.google.gson.annotations.SerializedName

class Number {
    @SerializedName("length")
    var length = 0

    @SerializedName("luhn")
    var isLuhn = false
}