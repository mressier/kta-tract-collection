package com.unicorpdev.ktatract.shared.tools.export

data class ObjectToExport(
    val filename: String,
    val content: Any,
    val relatedFilesPath: List<String>
)
