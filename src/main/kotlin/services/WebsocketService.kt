package services

import jakarta.enterprise.context.ApplicationScoped
import jakarta.inject.Inject
import jakarta.websocket.SendResult
import jakarta.websocket.Session
import models.HorizonLog
import repos.LogRepo
import java.util.concurrent.ConcurrentLinkedQueue
import java.util.logging.Logger

/**
 * @author Aidan Scott
 * WebsocketService provides common functionality to web socket actions like broadcasting messages to all clients
 */
@ApplicationScoped
class WebsocketService {

    // Injecting logRepo, this might need to be through LogService to be more correct
    @Inject
    lateinit var logRepo: LogRepo

    // Create a ConcurrentLinkedQueue<Session> to store all sessions in.
    var readers: ConcurrentLinkedQueue<Session> = ConcurrentLinkedQueue()

    // Create logger
    private val logger: Logger = Logger.getLogger(this::class.toString())

    /**
     * broadcast will take a queue of sessions and send a log to all of them
     * If more than one log should be sent, call broadcast for each log.
     */
    fun broadcast(log: HorizonLog, sessions: ConcurrentLinkedQueue<Session>) {
        val fLog = "${log.date} ${log.machineID} ${log.machine} ${log.operationCode} ${log.message}"
        logger.info("log sending to readers: $fLog")
        // Just want to say, I really like the .forEach method on iterable objects
        sessions.forEach {
            it.asyncRemote.sendObject(fLog, fun(it: SendResult) {
                if (it.exception != null) {
                    println("Unable to send message: ${it.exception}")
                }
            })
        }
    }
}