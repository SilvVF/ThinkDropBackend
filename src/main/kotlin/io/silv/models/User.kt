package io.silv.models

import org.bson.codecs.pojo.annotations.BsonId
import java.util.UUID

@kotlinx.serialization.Serializable
data class User(
    val username: String,
    val password: String,
    @BsonId
    val userId: String = UUID.randomUUID().toString()
)
