package services

import io.quarkus.hibernate.reactive.panache.common.WithTransaction
import io.quarkus.panache.common.Sort
import io.smallrye.mutiny.Multi
import io.smallrye.mutiny.Uni
import jakarta.enterprise.context.ApplicationScoped
import jakarta.inject.Inject
import jakarta.websocket.SendResult
import jakarta.websocket.Session
import models.HorizonLog
import repos.LogRepo
import java.util.concurrent.ConcurrentLinkedQueue
import java.util.logging.Logger

@ApplicationScoped
class WebsocketService {

    @Inject
    lateinit var logRepo: LogRepo
    var readers: ConcurrentLinkedQueue<Session> = ConcurrentLinkedQueue()
    private val LOGGER: Logger = Logger.getLogger(this::class.toString())

    fun broadcast(log: HorizonLog, sessions: ConcurrentLinkedQueue<Session>){
        val fLog = "${log.date} ${log.machineID} ${log.machine} ${log.operationCode} ${log.message}"
        LOGGER.info("log sending to readers: $fLog")
        sessions.forEach {
            it.asyncRemote.sendObject(fLog, fun(it: SendResult) {
                if (it.exception != null) {
                    println("Unable to send message: ${it.exception}")
                }
            })
        }
    }
    @WithTransaction
    fun initLogs(session: Session): Uni<Unit> {
        val sessionList: ConcurrentLinkedQueue<Session> = ConcurrentLinkedQueue()
        sessionList.add(session)
        return logRepo.listAll()
            .onItem().transformToMulti { list -> Multi.createFrom().iterable(list) }
            .onItem().invoke { log ->
                broadcast(log, sessionList)
            }
            .collect().asList() // Convert Multi back to Uni<List>
            .onItem().transform {
                // Perform any final actions if necessary
                Unit // Kotlin's way of saying 'void'
            }
    }
}