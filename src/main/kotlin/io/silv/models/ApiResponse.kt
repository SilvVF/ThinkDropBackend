package io.silv.models

@kotlinx.serialization.Serializable
data class ApiResponse(
    val successful: Boolean,
    val message: String,
)
