package controllers

import jakarta.enterprise.context.ApplicationScoped
import jakarta.inject.Inject
import jakarta.websocket.OnClose
import jakarta.websocket.OnError
import jakarta.websocket.OnOpen
import jakarta.websocket.Session
import jakarta.websocket.server.ServerEndpoint
import services.WebsocketService
import java.util.logging.Logger

/**
 * @author Aidan Scott
 * WebsocketController provides a websocket for constant updates on the incoming logs
 */
@ServerEndpoint("/logs")
@ApplicationScoped
class WebsocketController {
    // Injected class
    @Inject
    lateinit var websocketService: WebsocketService

    // Logger
    private val logger: Logger = Logger.getLogger(this::class.toString())

    /**
     * onOpen logs that a session has connected to the websocket and adds them to the connected sessions queue.
     */
    @OnOpen
    fun onOpen(session: Session) {
        websocketService.readers.add(session)
        logger.info("Reader connected to websocket at session: $session")
    }

    /**
     * onClose() logs that a session has disconnected from the websocket and removes them from the connected sessions queue.
     */
    @OnClose
    fun onClose(session: Session) {
        websocketService.readers.remove(session)
        logger.info("Reader disconnected from websocket with session: $session")
    }

    /**
     * onError logs any errors that occur during websocket use, then closes the connection.
     */
    @OnError
    fun onError(session: Session, throwable: Throwable) {
        logger.info("session ${session.id} disconnected from websocket due to error: $throwable")
        session.close()
    }
}