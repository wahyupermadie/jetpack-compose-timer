package com.godohembon.trytimerapp.utils

data class TimeCount(
    var label: String,
    var time: Int,
    var isSelected: Boolean
)
object Helper {

    fun getCountDownHint(): MutableList<TimeCount> {
        val time : MutableList<TimeCount> = mutableListOf()
        time.apply {
            add(TimeCount("5 Mins", 300, false))
            add(TimeCount("10 Mins", 600, false))
            add(TimeCount("15 Mins", 900, false))
            add(TimeCount("20 Mins", 1200, false))
            add(TimeCount("25 Mins", 1500, false))
            add(TimeCount("30 Mins", 1800, false))
        }

        return time
    }
}