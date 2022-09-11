package io.silv.routing

import io.ktor.http.HttpStatusCode.Companion.BadRequest
import io.ktor.http.HttpStatusCode.Companion.Conflict
import io.ktor.http.HttpStatusCode.Companion.OK
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import io.silv.models.TaskItem
import io.silv.models.response.ApiResponse
import io.silv.models.request.AddOwnerRequest
import io.silv.models.request.DeleteTaskRequest
import io.silv.mongo.*
fun Route.taskRoutes() {
    route("/getTasks") {
        authenticate {
            get {//get the user email attached to the authentication
                val username = call.principal<UserIdPrincipal>()?.name ?: return@get
                val tasks = getTasksForUser(username)
                call.respond(
                    status = OK,
                    message = tasks
                )
            }
        }
    }

    route("/addTask") {
        authenticate {
            post {
                val taskItem = try {
                    call.receive<TaskItem>()
                } catch (e: ContentTransformationException) {
                    call.respond(BadRequest)
                    return@post
                }
                if (saveTask(taskItem)) {
                    call.respond(OK)
                } else {
                    call.respond(Conflict)
                }
            }
        }
    }

    route("/deleteTask") {
        authenticate {
            post {
                val deleteRequest: Pair<DeleteTaskRequest, String> = try {
                    Pair(call.receive(), call.principal<UserIdPrincipal>()!!.name)
                } catch (e: ContentTransformationException) {
                    call.respond(BadRequest)
                    return@post
                }
                when (deleteTaskForUser(deleteRequest.second, deleteRequest.first.id)){
                    true -> call.respond(OK)
                    false -> call.respond(Conflict)
                }
            }
        }
    }

    route("/addOwnerToTask") {
        authenticate {
            post {
                val request = try {
                    call.receive<AddOwnerRequest>()
                } catch (e: Exception) {
                    call.respond(BadRequest)
                    return@post
                }
                if (!checkIfUserExists(request.owner)) {
                    call.respond(
                        status = Conflict,
                        message = ApiResponse(
                            successful = false,
                            message = "User does not exist"
                        )
                    )
                    return@post
                }
                when (isOwnerOfTask(request.taskID, request.owner)) {
                    true -> {
                        call.respond(
                            status = Conflict,
                            message = ApiResponse(
                                successful = false,
                                message = "User is Already an owner"
                            )
                        )
                        return@post
                    }
                    false -> {
                        if(!addOwnerToTask(request.taskID, request.owner)){
                            call.respond(
                                status = OK,
                                message = ApiResponse(
                                    successful = false,
                                    message = "An Unknown Error Occurred could not add owner"
                                )
                            )
                            return@post
                        }
                        call.respond(
                            status = OK,
                            message = ApiResponse(
                                successful = true,
                                message = "owner added successfully"
                            )
                        )
                        return@post
                    }
                }
            }
        }
    }
}