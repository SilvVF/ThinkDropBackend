package io.silv.mongo

import io.silv.db
import io.silv.models.TaskItem
import io.silv.models.User
import io.silv.util.checkHashForPassword
import io.silv.util.getHashWithSalt
import org.litote.kmongo.*
import java.util.TimerTask

val usersCollection = db.getCollection<User>()
val taskItemCollection = db.getCollection<TaskItem>()

suspend fun registerUser(user: User): Boolean = usersCollection.insertOne(user).wasAcknowledged()

suspend fun checkIfUserExists(username: String): Boolean { //go through all the user documents and use the email to compare to email
    return usersCollection.findOne(User::username eq username) != null //user.email == email
}

suspend fun checkPasswordForEmail(username: String, password: String): Boolean {
    val actualPassword = usersCollection.findOne(User::username eq password)?.password ?: return false
    return checkHashForPassword(password, actualPassword)
}

suspend fun getTasksForUser(email: String): List<TaskItem> {
    return taskItemCollection.find(TaskItem::owners contains email).toList()
}

suspend fun saveTask(taskItem: TaskItem): Boolean {
    val noteExists = taskItemCollection.findOneById(id = taskItem.id) != null
    return if (noteExists) {
        taskItemCollection.updateOneById(taskItem.id, taskItem).wasAcknowledged()
    } else {
        taskItemCollection.insertOne(taskItem).wasAcknowledged()
    }
}

suspend fun deleteTaskForUser(email: String, taskID: String): Boolean {
    //get the note and check if user has permission to delete , -> is the same as and eq and contains same
    val task = taskItemCollection.findOne(TaskItem::id eq taskID, TaskItem::owners.contains(email))
    task?.let { task ->
        if (task.owners.size > 1) {
            //multiple owners only delete email
            val newOwners = task.owners.filter { it != email }
            val updatedNote = taskItemCollection.updateOne(TaskItem::id eq task.id, setValue(TaskItem::owners, newOwners))
            return updatedNote.wasAcknowledged()
        }
        //user is the sole owner of note delete the whole document
        return taskItemCollection.deleteOneById(taskID).wasAcknowledged()
    } ?: return false
}

suspend fun isOwnerOfTask(taskID: String, owner: String): Boolean {
    return taskItemCollection.findOneById(taskID)?.owners?.contains(owner) ?: return false
}

suspend fun addOwnerToTask(taskID: String, owner: String): Boolean {
    val owners = taskItemCollection.findOneById(taskID)?.owners ?: return false
    val newOwners = owners + owner
    return taskItemCollection.updateOneById(taskID, setValue(TaskItem::owners, newOwners)).wasAcknowledged()
}

