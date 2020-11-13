package com.unicorpdev.ktatract.shared.extensions

inline fun <T, R : Comparable<R>> Iterable<T>.sortedBy(
    ascending: Boolean,
    crossinline selector: (T) -> R?
): List<T> {
    return if (ascending) { sortedBy(selector) } else { sortedByDescending(selector) }
}