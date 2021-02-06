package com.demo.cardinfofinder.utils

import android.content.Context
import android.text.Editable
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import com.demo.cardinfofinder.R

fun EditText.hideKeyboard(
) {
    val imm = context.getSystemService(Context.INPUT_METHOD_SERVICE) as
            InputMethodManager
    imm.hideSoftInputFromWindow(this.windowToken, 0)
}


fun getCartLogo(s: Editable): Int {

    when (s.substring(0, 1).toInt()) {
        6 -> {
            return R.drawable.ic_discover
        }
        5 -> {
            return R.drawable.ic_mastercard
        }
        4 -> {
            return R.drawable.ic_visa
        }
        3 -> {
            return R.drawable.ic_amex
        }
        else -> {
            return R.drawable.ic_warning_card
        }
    }
}

fun setCardNumber(s: Editable) {
    val space = ' '

    var pos = 0
    while (true) {
        if (pos >= s.length) break
        if (space == s[pos] && ((pos + 1) % 5 != 0 || pos + 1 == s.length)) {
            s.delete(pos, pos + 1)
        } else {
            pos++
        }
    }
    // Insert char where needed.
    pos = 4
    while (true) {
        if (pos >= s.length) break
        val c = s[pos]
        // Only if its a digit where there should be a space we insert a space
        if ("0123456789".indexOf(c) >= 0) {
            s.insert(pos, "" + space)
        }
        pos += 5
    }
}
