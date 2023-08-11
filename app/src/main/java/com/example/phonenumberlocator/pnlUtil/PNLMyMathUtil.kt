/*
 * Copyright 2013 Google Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.example.phonenumberlocator.pnlUtil

import kotlin.math.asin
import kotlin.math.cos
import kotlin.math.sin
import kotlin.math.sqrt

internal object PNLMyMathUtil {

    private fun fHav(x: Double): Double {
        val sinHalf = sin(x * 0.5)
        return sinHalf * sinHalf
    }

    fun fArcHav(x: Double): Double {
        return 2 * asin(sqrt(x))
    }

    fun fHavDistance(lat1: Double, lat2: Double, dLng: Double): Double {
        return fHav(lat1 - lat2) + fHav(dLng) * cos(lat1) * cos(lat2)
    }
}