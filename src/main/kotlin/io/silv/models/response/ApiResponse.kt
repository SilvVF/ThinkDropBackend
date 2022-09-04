package io.silv.models.response

@kotlinx.serialization.Serializable
data class ApiResponse(
    val successful: Boolean,
    val message: String,
)
