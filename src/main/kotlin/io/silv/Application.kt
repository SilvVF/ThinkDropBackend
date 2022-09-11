package io.silv

import com.mongodb.client.MongoDatabase
import io.ktor.serialization.kotlinx.json.*
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.engine.*
import io.ktor.server.netty.*
import io.ktor.server.plugins.contentnegotiation.*
import io.ktor.server.plugins.defaultheaders.*
import io.ktor.server.routing.*
import io.silv.mongo.checkPasswordForEmail
import io.silv.routing.*
import io.silv.util.ThinkDropTestDB
import kotlinx.serialization.json.Json
import org.litote.kmongo.*


val client = KMongo.createClient()
val db: MongoDatabase = client.getDatabase(ThinkDropTestDB)

fun main() {
    embeddedServer(Netty, port = 8181, host = "0.0.0.0") {
        configureMonitoring()
        install(DefaultHeaders)

        install(ContentNegotiation) {
            json(
                Json { prettyPrint = true }
            )
        }
        install(Authentication) { //needs to come before any feature that uses authentication
            configureAuth()
        }
        install(Routing) {
            taskRoutes()
            logInRoute()
            registerRoute()
        }
    }.start(wait = true)
}


private fun AuthenticationConfig.configureAuth() {
    basic {  //with oauth open that
        realm = "TaskServer" //name of server that will pop up in the browser
        validate { credentials -> //need to check name and password and authenticate
            val username = credentials.name
            val password = credentials.password
            if (checkPasswordForEmail(username, password)) {
                UserIdPrincipal(username) //will be attached to the request can filter who is making the request and get their data
            } else null
        }
    }
}