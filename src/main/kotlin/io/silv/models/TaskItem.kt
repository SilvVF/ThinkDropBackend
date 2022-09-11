package io.silv.models

import org.bson.codecs.pojo.annotations.BsonId
import org.bson.types.ObjectId

@kotlinx.serialization.Serializable
data class TaskItem(
    val userId: String,
    val title: String,
    val content: String,
    val color: String,
    val category: String,
    val owners: List<String>,
    val creationDate: Long,
    @BsonId
    val id: String = ObjectId().toString()
)

