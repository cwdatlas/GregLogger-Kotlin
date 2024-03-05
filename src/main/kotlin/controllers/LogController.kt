package controllers

import io.quarkus.hibernate.reactive.panache.Panache
import io.smallrye.mutiny.Uni
import jakarta.enterprise.context.ApplicationScoped
import jakarta.inject.Inject
import jakarta.validation.Valid
import jakarta.ws.rs.GET
import jakarta.ws.rs.POST
import jakarta.ws.rs.Path
import jakarta.ws.rs.core.Response
import models.HorizonLog
import models.LogApi
import services.LogService
import services.WebsocketService

/**
 * @author Aidan Scott
 * LogController houses REST endpoints to add and get log data.
 * This api is meant to be accessed by an open computers or computercraft (minecraft mods) lua script.
 * /logs is the endpoint that is used across the webservices this program provides.
 */
@ApplicationScoped
@Path("/logs")
class LogController {

    // injecting singletons, useful services
    @Inject
    lateinit var logService: LogService

    @Inject
    lateinit var websocketService: WebsocketService

    /**
     * get() gets all logs and orders them in relation to the date
     **/
    @GET
    fun get(): Uni<List<HorizonLog>> {
        return logService.listAll()
    }

    /**
     * add() adds a log using practices I learned in my Advanced Software engineering course
     */
    @POST
    fun add(@Valid log: LogApi): Uni<Response> {
        val validLog = HorizonLog()
        // copying over data which will be saved
        validLog.date = log.date
        validLog.machine = log.machine
        validLog.machineID = log.machineID
        validLog.operationCode = log.operationCode
        validLog.message = log.message

        // Calling the websocket to update all clients that there is another log to view
        websocketService.broadcast(validLog, websocketService.readers)
        /* Returning the result of the interaction in a reactive way. If the input data is correct, it will be repeated back
        * If the input data is invalid, the reason will be returned.
        * If the Date is invalid, the date object will be returned. Date format must be in "YYYY-MM-DD" format
        */
        return Panache.withTransaction { logService.persist((validLog)) }
            .replaceWith(Response.ok(log).status(Response.Status.CREATED).build())
    }
}