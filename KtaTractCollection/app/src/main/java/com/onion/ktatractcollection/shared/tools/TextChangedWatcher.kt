package com.onion.ktatractcollection.shared.tools

import android.text.Editable
import android.text.TextWatcher

/**
 * Quick Implementation for a Text Watcher with only onTextChanged implementation
 *
 * Use like this:
 *
 * val watcher = TextChangedWatcher() { tract.author = it }
 * myTextField.addTextChangedListener(watcher)
 */
class TextChangedWatcher(val onTextChanged: (String) -> Unit): TextWatcher {

    override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

    override fun afterTextChanged(s: Editable?) {}

    override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
        onTextChanged(s.toString())
    }
}
