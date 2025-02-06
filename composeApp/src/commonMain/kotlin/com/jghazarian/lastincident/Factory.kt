package com.jghazarian.lastincident

import kotlinx.serialization.json.Json

//TODO: replace with injected json object or change how it's being used. Currently used by datastore
val json = Json { ignoreUnknownKeys = true }