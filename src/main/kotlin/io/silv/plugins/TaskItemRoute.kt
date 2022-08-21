package io.silv.plugins

import io.ktor.server.application.*
import io.ktor.server.routing.*

fun Route.getTaskItemRoute() {
    route("/getAllTasks") {
        get {

        }
    }
}

fun Route.postTaskItemRoute() {
    route("/postTaskItem") {
        post {

        }
    }
}