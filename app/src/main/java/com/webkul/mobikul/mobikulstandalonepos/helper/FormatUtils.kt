package com.webkul.mobikul.mobikulstandalonepos.helper

object FormatUtils {

    fun formatTimeToTwelveHours(hr: Int, mn: Int): String {
        var tempHr = hr;
        var type = "AM"
        if (tempHr >= 12) {
            type = "PM"
            tempHr -= 12
        }
        if (tempHr == 0) {
            tempHr = 12
        }
        val hour = addLeadingZeroes(tempHr, 2)
        val min = addLeadingZeroes(mn, 2)
        return "$hour:$min $type"
    }

    fun addLeadingZeroes(num: Int, width: Int): String {
        var newNum = "$num"
        while (newNum.length < width) {
            newNum = "0$newNum"
        }
        return newNum
    }
}