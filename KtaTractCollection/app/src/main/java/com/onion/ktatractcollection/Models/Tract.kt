package com.onion.ktatractcollection.Models

import java.util.*

class Tract(
    val id: UUID = UUID.randomUUID(),
    val author: String = "",
    val discoveryDate: Date = Date(),
    val comment: String) {}