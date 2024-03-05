package controllers

import io.quarkus.hibernate.reactive.panache.Panache
import io.quarkus.panache.common.Sort
import io.smallrye.mutiny.Uni
import jakarta.enterprise.context.ApplicationScoped
import jakarta.inject.Inject
import jakarta.validation.Valid
import jakarta.ws.rs.DELETE
import jakarta.ws.rs.GET
import jakarta.ws.rs.POST
import jakarta.ws.rs.Path
import jakarta.ws.rs.core.Response
import models.HorizonLog
import models.LogApi
import repos.LogRepo
import services.LogService
import services.WebsocketService

@ApplicationScoped
@Path("/logs")
class LogController {

    @Inject
    lateinit var logService: LogService
    @Inject
    lateinit var websocketService: WebsocketService

    @GET
    fun get(): Uni<List<HorizonLog>> {
        return logService.listAll()
    }

    @POST
    fun add(@Valid log: LogApi): Uni<Response> {
        val validLog = HorizonLog()
        validLog.date = log.date
        validLog.machine = log.machine
        validLog.machineID = log.machineID
        validLog.operationCode = log.operationCode
        validLog.message = log.message

        websocketService.broadcast(validLog, websocketService.readers)
        return Panache.withTransaction { logService.persist((validLog)) }
            .replaceWith(Response.ok(log).status(Response.Status.CREATED).build())
    }

    @DELETE
    @Path("{id}")
    fun delete(id: Long): Uni<Response> {
        return Panache.withTransaction { logService.deleteByID(id) }
            .map { deleted ->
                if (deleted) Response.ok().status(Response.Status.NO_CONTENT).build()
                else Response.ok().status(Response.Status.NOT_FOUND).build()
            }
    }
}