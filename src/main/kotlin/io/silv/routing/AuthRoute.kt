package io.silv.routing

import io.ktor.http.HttpStatusCode.Companion.BadRequest
import io.ktor.http.HttpStatusCode.Companion.OK
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import io.silv.models.request.AccountRequest
import io.silv.models.response.ApiResponse
import io.silv.mongo.checkPasswordForEmail

fun Route.logInRoute() {
    route("/login") {
        post {
            val request = try {
                call.receive<AccountRequest>() //parse JSON request
            } catch (e: ContentTransformationException) {
                call.respond(BadRequest)
                return@post
            }
            val isPasswordCorrect = checkPasswordForEmail(request.username, request.password)
            if (isPasswordCorrect){
                call.respond(
                    status = OK,
                    message = ApiResponse(
                        successful = true,
                        message = "You are now logged in"
                    )
                )
            }else {
                call.respond(
                    status = OK,
                    message = ApiResponse(
                        successful = false,
                        message = "The E-mail or password was incorrect"
                    )
                )
            }
        }
    }
}