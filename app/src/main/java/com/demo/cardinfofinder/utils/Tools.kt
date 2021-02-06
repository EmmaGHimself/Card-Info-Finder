package com.demo.cardinfofinder.utils

object Tools {
    fun insertPeriodically(text: String, insert: String, period: Int): String {
        val builder = StringBuilder(text.length + insert.length * (text.length / period) + 1)
        var index = 0
        var prefix = ""
        while (index < text.length) {
            builder.append(prefix)
            prefix = insert
            builder.append(text.substring(index, (index + period).coerceAtMost(text.length)))
            index += period
        }
        return builder.toString()
    }
}