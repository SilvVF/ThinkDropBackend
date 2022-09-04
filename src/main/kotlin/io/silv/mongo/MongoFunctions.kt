package io.silv.mongo

import io.silv.db
import io.silv.models.TaskItem
import io.silv.models.User
import io.silv.util.checkHashForPassword
import io.silv.util.getHashWithSalt
import org.litote.kmongo.eq
import org.litote.kmongo.findOne
import org.litote.kmongo.getCollection

val usersCollection = db.getCollection<User>()
val taskItemCollection = db.getCollection<TaskItem>()

fun checkIfUserExists(userId: String): Boolean =
    usersCollection.findOne(User::userId eq  userId) != null

fun findUserByEmail(email: String) = usersCollection.findOne(User::username eq email)

fun validateCredential(userId: String, password: String): Boolean =
    usersCollection.findOne(User::userId eq  userId)?.password == password

fun fetchAllTasksForUser(userId: String): List<TaskItem> {
    val items = taskItemCollection.find(TaskItem::userId eq userId)
    return items.toList()
}

fun checkIfPasswordIsCorrect(email: String, password: String): Boolean {
    val user = findUserByEmail(email) ?: return false
    return checkHashForPassword(password, user.password)
}

