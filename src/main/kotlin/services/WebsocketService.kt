package services

import io.quarkus.panache.common.Sort
import io.smallrye.mutiny.Multi
import jakarta.enterprise.context.ApplicationScoped
import jakarta.inject.Inject
import jakarta.websocket.SendResult
import jakarta.websocket.Session
import jakarta.ws.rs.core.Response
import models.HorizonLog
import repos.LogRepo
import java.util.concurrent.ConcurrentLinkedQueue

@ApplicationScoped
class WebsocketService {

    @Inject
    lateinit var logRepo: LogRepo
    var readers: ConcurrentLinkedQueue<Session> = ConcurrentLinkedQueue()


    fun broadcast(log: HorizonLog, sessions: ConcurrentLinkedQueue<Session>) {
        val fLog = "${log.date} ${log.machineID} ${log.machine} ${log.operationCode} ${log.message}"
        sessions.forEach {
            it.asyncRemote.sendObject(fLog, fun(it: SendResult) {
                if (it.exception != null) {
                    println("Unable to send message: ${it.exception}")
                }
            })
        }
    }
    fun initLogs(session: Session){
        var sessionList: ConcurrentLinkedQueue<Session> = ConcurrentLinkedQueue()
        sessionList.add(session)
        logRepo.listAll()
            .onItem().transformToMulti { list -> Multi.createFrom().iterable(list) }
            .onItem().invoke { log ->
                broadcast(log, sessionList) // Pass the WebSocket session here
            }
    }
}