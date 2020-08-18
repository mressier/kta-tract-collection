package com.onion.ktatractcollection.Models

import java.util.*

class Tract(
    val id: UUID = UUID.randomUUID(),
    var author: String = "",
    var discoveryDate: Date = Date(),
    var comment: String = "") {}