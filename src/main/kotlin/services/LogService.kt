package services

import io.quarkus.hibernate.reactive.panache.common.WithTransaction
import io.quarkus.panache.common.Sort
import io.smallrye.mutiny.Uni
import jakarta.enterprise.context.ApplicationScoped
import jakarta.inject.Inject
import models.HorizonLog
import repos.LogRepo

/**
 * @author Aidan Scott
 * LogService provides datasource access to Controllers
 * This service directly passes data currently
 * Look at PanacheRepository for more information as the present functions pass functions from PanacheRepository.
 */
@ApplicationScoped
class LogService {
    // Inject logRepo
    @Inject
    lateinit var logRepo: LogRepo

    // WithTransation allows for access to the database
    @WithTransaction
    fun listAll(): Uni<List<HorizonLog>> {
        return logRepo.listAll(Sort.by("date"))
    }

    @WithTransaction
    fun persist(log: HorizonLog): Uni<HorizonLog> {
        return logRepo.persist(log)
    }

    @WithTransaction
    fun deleteByID(id: Long): Uni<Boolean> {
        return logRepo.deleteById(id)
    }
}