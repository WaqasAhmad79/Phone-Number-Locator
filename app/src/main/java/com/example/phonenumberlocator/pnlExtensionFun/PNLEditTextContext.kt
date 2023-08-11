package com.example.phonenumberlocator.pnlExtensionFun

import android.text.Editable
import android.text.Spannable
import android.text.SpannableString
import android.text.TextWatcher
import android.text.style.BackgroundColorSpan
import android.view.KeyEvent
import android.widget.EditText
import android.widget.TextView
import androidx.core.graphics.ColorUtils

val EditText.value: String get() = text.toString().trim()

fun EditText.onTextChangeListener(onTextChangedAction: (newText: String) -> Unit) = addTextChangedListener(object : TextWatcher {
    override fun afterTextChanged(s: Editable?) {
        onTextChangedAction(s.toString())
    }

    override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}

    override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {}
})

fun EditText.highlightText(highlightText: String, color: Int) {
    val content = text.toString()
    var indexOf = content.indexOf(highlightText, 0, true)
    val wordToSpan = SpannableString(text)
    var offset = 0

    while (offset < content.length && indexOf != -1) {
        indexOf = content.indexOf(highlightText, offset, true)

        if (indexOf == -1) {
            break
        } else {
            val spanBgColor = BackgroundColorSpan(ColorUtils.setAlphaComponent(color, 128))
            val spanFlag = Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
            wordToSpan.setSpan(spanBgColor, indexOf, indexOf + highlightText.length, spanFlag)
            setText(wordToSpan, TextView.BufferType.SPANNABLE)
        }

        offset = indexOf + 1
    }
}

fun EditText.addCharacter(char: Char) {
    dispatchKeyEvent(getKeyEvent(getCharKeyCode(char)))
}

fun getKeyEvent(keyCode: Int) = KeyEvent(0, 0, KeyEvent.ACTION_DOWN, keyCode, 0)

private fun getCharKeyCode(char: Char) = when (char) {
    '0' -> KeyEvent.KEYCODE_0
    '1' -> KeyEvent.KEYCODE_1
    '2' -> KeyEvent.KEYCODE_2
    '3' -> KeyEvent.KEYCODE_3
    '4' -> KeyEvent.KEYCODE_4
    '5' -> KeyEvent.KEYCODE_5
    '6' -> KeyEvent.KEYCODE_6
    '7' -> KeyEvent.KEYCODE_7
    '8' -> KeyEvent.KEYCODE_8
    '9' -> KeyEvent.KEYCODE_9
    '*' -> KeyEvent.KEYCODE_STAR
    '+' -> KeyEvent.KEYCODE_PLUS
    else -> KeyEvent.KEYCODE_POUND
}
