package com.example.phonenumberlocator.pnlOverloads

operator fun String.times(x: Int): String {
    val stringBuilder = StringBuilder()
    for (i in 1..x) {
        stringBuilder.append(this)
    }
    return stringBuilder.toString()
}
