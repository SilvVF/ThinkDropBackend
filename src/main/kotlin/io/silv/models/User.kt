package io.silv.models

import org.bson.codecs.pojo.annotations.BsonId
import org.bson.types.ObjectId
import java.util.UUID

@kotlinx.serialization.Serializable
data class User(
    val username: String,
    val password: String,
    val dateOfBirth: Long,
    val name: String,
    @BsonId
    val userId: String = ObjectId().toString()
)
