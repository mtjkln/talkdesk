package com.mritunjay.talkdesk.data.util

import kotlin.random.Random

object DataUtil {
    fun getRandomNumberForRange(lowerBound: Int, upperBound: Int) =
        Random.nextInt(lowerBound, upperBound)
}