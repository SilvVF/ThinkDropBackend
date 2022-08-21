package io.silv.mongo

import io.silv.db
import io.silv.models.TaskItem
import io.silv.models.User
import org.litote.kmongo.eq
import org.litote.kmongo.findOne
import org.litote.kmongo.getCollection

val usersCollection = db.getCollection<User>()
val taskItemCollection = db.getCollection<TaskItem>()

fun checkIfUserExists(userId: String): Boolean =
    usersCollection.findOne(User::userId eq  userId) != null


fun validateCredential(userId: String, password: String): Boolean =
    usersCollection.findOne(User::userId eq  userId)?.password == password

fun fetchAllTasksForUser(userId: String): List<TaskItem> {
    val items = taskItemCollection.find(TaskItem::userId eq userId)
    return items.toList()
}

