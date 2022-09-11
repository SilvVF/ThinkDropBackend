package io.silv.models.request
import io.silv.models.TaskItem

data class SignInRequest(
    val email: String,
    val password: String
)

data class CreateTaskRequest(
    val username: String,
    val password: String,
    val task: TaskItem
)

data class DeleteTaskRequest(
    val id: String
)

data class AddOwnerRequest(
    val taskID: String,
    val owner: String
)

data class AccountRequest(
    val username: String,
    val password: String,
    val dateOfBirth: Long,
    val name: String,
)

