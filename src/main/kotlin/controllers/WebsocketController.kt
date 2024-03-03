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
import java.util.concurrent.ConcurrentLinkedQueue
import java.util.logging.Logger


@ServerEndpoint("/logs/{logertype}")
@ApplicationScoped
class WebsocketController {

    @Inject
    lateinit var logRepo: LogRepo
    private var readers: ConcurrentLinkedQueue<Session> = ConcurrentLinkedQueue()
    private var writers: ConcurrentLinkedQueue<Session> = ConcurrentLinkedQueue()
    private val LOGGER: Logger = Logger.getLogger(this::class.toString())

    @OnOpen
    fun onOpen(session: Session, @PathParam("logertype") loggerType: String){
        if(loggerType == "reader") {
            readers.add(session)
            LOGGER.info("$loggerType connected to websocket")
        }
        else if(loggerType == "writer"){
            writers.add(session)
            LOGGER.info("$loggerType connected to websocket")
        }
        else{
            session.close(CloseReason(CloseReason.CloseCodes.VIOLATED_POLICY, "Logger type must be either 'reader' or 'writer'"))
        }
    }

    @OnClose
    fun onClose(session: Session, @PathParam("logertype") loggerType: String){
        if(loggerType == "reader") {
            readers.remove(session)
            LOGGER.info("$loggerType disconnected from websocket")
        }
        else if(loggerType == "writer"){
            writers.remove(session)
            LOGGER.info("$loggerType disconnected from websocket")
        }
        else{
            session.close(CloseReason(CloseReason.CloseCodes.VIOLATED_POLICY, "Logger type must be either 'reader' or 'writer'"))
        }
    }

    @OnError
    fun onError(session: Session, @PathParam("logertype") loggerType: String){
        if(loggerType == "reader") {
            readers.remove(session)
            LOGGER.info("$loggerType disconnected from websocket due to error")
        }
        else if(loggerType == "writer"){
            writers.remove(session)
            LOGGER.info("$loggerType disconnected from websocket due to error")
        }
        else{
            session.close(CloseReason(CloseReason.CloseCodes.VIOLATED_POLICY, "Logger type must be either 'reader' or 'writer'"))
        }
    }

    @OnMessage
    fun onMessage(session: Session, log: HorizonLog, @PathParam("logertype") loggerType: String){
        if(loggerType == "writer"){
            if (log == null || log.id != null) {
                throw WebApplicationException("Id was invalidly set on request.", 422)
            }
            LOGGER.info("Log sent by session: $session")
            session.asyncRemote.sendObject(
                Panache.withTransaction { logRepo.persist(log) }
                .replaceWith{ Response.ok(log).status(Response.Status.CREATED).build()})


        }

    }
    private fun broadcast(logs: HorizonLog, sessions: ConcurrentLinkedQueue<Session>) {
        sessions.forEach {
            it.asyncRemote.sendObject(logs, fun(it: SendResult) {
                if (it.exception != null) {
                    println("Unable to send message: ${it.exception}")
                }
            })
        }
    }
}