package controllers

import io.quarkus.hibernate.reactive.panache.Panache
import jakarta.enterprise.context.ApplicationScoped
import jakarta.inject.Inject
import jakarta.websocket.*
import jakarta.websocket.server.PathParam
import jakarta.websocket.server.ServerEndpoint
import jakarta.ws.rs.WebApplicationException
import jakarta.ws.rs.core.Response
import models.HorizonLog
import repos.LogRepo
import services.WebsocketService
import java.util.concurrent.ConcurrentLinkedQueue
import java.util.logging.Logger


@ServerEndpoint("/logs")
@ApplicationScoped
class WebsocketController {
    @Inject
    lateinit var websocketService: WebsocketService
    private val LOGGER: Logger = Logger.getLogger(this::class.toString())

    /**
     * onOpen starts a loop that sends the entire contents of the database to the connected webclient
     */
    @OnOpen
    fun onOpen(session: Session) {
        websocketService.readers.add(session)
        LOGGER.info("Reader connected to websocket at session: $session")
        // Sends all logs to the user that are stored in the database
        websocketService.initLogs(session)
    }

    @OnClose
    fun onClose(session: Session) {
        websocketService.readers.remove(session)
        LOGGER.info("Reader disconnected from websocket with session: $session")
    }

    @OnError
    fun onError(session: Session, @PathParam("logertype") loggerType: String, throwable: Throwable) {
        LOGGER.info("$loggerType disconnected from websocket due to error")
        session.close()
        }
    }