package com.unicorpdev.ktatract.shared.extensions

import java.text.DateFormat
import java.util.*

val Date.shortString: String
    get() = DateFormat.getDateInstance(DateFormat.SHORT).format(this)

val Date.longString: String
    get() = DateFormat.getDateInstance(DateFormat.LONG).format(this)