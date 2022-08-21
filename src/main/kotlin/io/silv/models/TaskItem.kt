package io.silv.models

@kotlinx.serialization.Serializable
data class TaskItem(
    val title: String,
    val content: String,
    val userId: String,
    val dateCreated: Long,
    val completed: Boolean,
    val priority: Int,
    val taskEndDate: Long
)
