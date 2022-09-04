package io.silv.routing

import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import io.silv.models.response.ApiResponse
import io.silv.models.TaskList
import io.silv.mongo.checkIfUserExists
import io.silv.mongo.fetchAllTasksForUser
import kotlinx.serialization.json.Json

fun Route.configureTaskRouting() {
    route("/getAllTasks") {
        authenticate {
            get {
                val user = call.principal<UserIdPrincipal>()
                if(user == null) {
                    call.respond(
                        status = HttpStatusCode.Conflict,
                        message = ApiResponse(
                            message = "unknown error occurred",
                            successful = false
                        )
                    )
                    return@get
                }
                if(!checkIfUserExists(user.name)) {
                    call.respond(
                        status = HttpStatusCode.Conflict,
                        message = ApiResponse(
                            message = "user does not exist",
                            successful = false
                        )
                    )
                    return@get
                }
                val tasks = fetchAllTasksForUser(user.name)
                call.respond(
                    status = HttpStatusCode.OK,
                    message = ApiResponse(
                        message = Json.encodeToString(TaskList.serializer(), TaskList(tasks)),
                        successful = true
                    )
                )
            }
        }
    }


    route("/postTaskItem") {
        post {

        }
    }
}
