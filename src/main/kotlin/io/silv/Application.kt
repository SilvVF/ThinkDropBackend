package io.silv

import io.ktor.serialization.kotlinx.json.*
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.engine.*
import io.ktor.server.netty.*
import io.ktor.server.plugins.contentnegotiation.*
import io.silv.mongo.checkIfUserExists
import io.silv.mongo.validateCredential
import io.silv.plugins.*
import org.litote.kmongo.*


val client = KMongo.createClient()
val db = client.getDatabase("ThinkTestUsers")

fun main() {
    embeddedServer(Netty, port = 8181, host = "0.0.0.0") {
        install(ContentNegotiation) {
            json()
        }
        install(Authentication) {
            basic {
                validate {userPasswordCredential ->
                    if(validateCredential(userPasswordCredential.name, userPasswordCredential.password)) {
                        UserIdPrincipal(userPasswordCredential.name)
                    } else null
                }
            }
        }
        configureMonitoring()
        configureHTTP()

        configureRouting()
    }.start(wait = true)
}
