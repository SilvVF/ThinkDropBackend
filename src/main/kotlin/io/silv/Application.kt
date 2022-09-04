package io.silv

import com.mongodb.client.MongoDatabase
import io.ktor.serialization.kotlinx.json.*
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.engine.*
import io.ktor.server.netty.*
import io.ktor.server.plugins.contentnegotiation.*
import io.ktor.server.routing.*
import io.silv.mongo.validateCredential
import io.silv.routing.*
import io.silv.util.ThinkDropTestDB
import io.silv.util.basic_auth
import org.litote.kmongo.*


val client = KMongo.createClient()
val db: MongoDatabase = client.getDatabase(ThinkDropTestDB)

fun main() {
    embeddedServer(Netty, port = 8181, host = "0.0.0.0") {
        configureMonitoring()
        install(ContentNegotiation) {
            json()
        }
        install(Authentication) {
            basic(basic_auth) {
                validate {userPasswordCredential ->
                    if(validateCredential(userPasswordCredential.name, userPasswordCredential.password)) {
                        UserIdPrincipal(userPasswordCredential.name)
                    } else null
                }
            }
        }
        install(Routing) {
            configureTaskRouting()
            logInRoute()
        }
    }.start(wait = true)
}
