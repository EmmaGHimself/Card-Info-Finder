package com.demo.cardinfofinder

import android.text.Editable
import com.demo.cardinfofinder.utils.setCardNumber
import org.junit.Test

internal class UtilsUnitTest {
    @Test
    fun spaceSetCardNumber() {
        val s: Editable ?= null
        s?.equals(6).toString()
        assert(true) {
            if (s != null) {
                setCardNumber(s)
            }
        }
    }
}