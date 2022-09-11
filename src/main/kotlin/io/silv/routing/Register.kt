package io.silv.routing

import io.ktor.http.HttpStatusCode.Companion.BadRequest
import io.ktor.http.HttpStatusCode.Companion.OK
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import io.silv.models.User
import io.silv.models.request.AccountRequest
import io.silv.models.response.ApiResponse
import io.silv.mongo.checkIfUserExists
import io.silv.mongo.registerUser
import io.silv.util.getHashWithSalt

fun Route.registerRoute() {
    route("/register") {
        post {
            val request = try {
                call.receive<AccountRequest>()
            } catch (e: ContentTransformationException) {
                call.respond(BadRequest)
                return@post
            }
            if (checkIfUserExists(request.username)) {
                call.respond(
                    status = OK,
                    message = ApiResponse(
                        successful = false,
                        message = "User with the email already exists"
                    )
                )
                return@post
            }
            if (registerUser(
                    User(
                        request.username,
                        getHashWithSalt(request.password),
                        request.dateOfBirth,
                        request.name
                    )
                )
            ) {
                call.respond(
                    status = OK,
                    message = ApiResponse(
                        successful = true,
                        message = "Successfully Created Account"
                    )
                )
            } else {
                call.respond(
                    status = OK,
                    message = ApiResponse(
                        successful = false,
                        message = "Unknown error Occurred"
                    )
                )
            }
        }
    }
}
