package controllers

import io.quarkus.hibernate.reactive.panache.Panache
import io.quarkus.panache.common.Sort
import io.smallrye.mutiny.Uni
import jakarta.enterprise.context.ApplicationScoped
import jakarta.inject.Inject
import jakarta.ws.rs.DELETE
import jakarta.ws.rs.GET
import jakarta.ws.rs.POST
import jakarta.ws.rs.Path
import jakarta.ws.rs.WebApplicationException
import jakarta.ws.rs.core.Response
import models.HorizonLog
import repos.LogRepo

@ApplicationScoped
@Path("/logs/")
class LogController {

    @Inject
    lateinit var logs: LogRepo

    @GET
    fun get(): Uni<List<HorizonLog>> {
        return logs.listAll(Sort.by("date"))
    }

    @POST
    fun add(log: HorizonLog): Uni<Response> {
        if (log == null || log.id != null)
            throw WebApplicationException("Id was invalidly set on request.", 422)
        return Panache.withTransaction { logs.persist((log)) }
            .replaceWith(Response.ok(log).status(Response.Status.CREATED).build())
    }

    @DELETE
    @Path("{id}")
    fun delete(id: Long): Uni<Response> {
        return Panache.withTransaction { logs.deleteById(id) }
            .map { deleted ->
                if (deleted) Response.ok().status(Response.Status.NO_CONTENT).build()
                else Response.ok().status(Response.Status.NOT_FOUND).build()
            }
    }
}