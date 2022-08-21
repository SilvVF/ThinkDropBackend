package io.silv.models

import org.bson.codecs.pojo.annotations.BsonId
import java.util.UUID

@kotlinx.serialization.Serializable
data class TaskItem(
    val title: String,
    val content: String,
    @BsonId
    val taskId: String = UUID.randomUUID().toString(),
    val userId: String,
    val dateCreated: Long,
    val completed: Boolean,
    val priority: Int,
    val taskEndDate: Long
)
