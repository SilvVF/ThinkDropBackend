package io.silv.models

@kotlinx.serialization.Serializable
data class TaskList(
    val tasks: List<TaskItem>
)
