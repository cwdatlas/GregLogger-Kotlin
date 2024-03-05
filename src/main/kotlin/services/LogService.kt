package services

import io.quarkus.panache.common.Sort
import io.smallrye.mutiny.Uni
import jakarta.enterprise.context.ApplicationScoped
import jakarta.inject.Inject
import models.HorizonLog
import repos.LogRepo

@ApplicationScoped
class LogService {
    @Inject
    lateinit var logRepo: LogRepo

    fun listAll(): Uni<List<HorizonLog>>{
        return logRepo.listAll(Sort.by("date"))
    }

    fun persist(log: HorizonLog): Uni<HorizonLog> {
        return logRepo.persist(log)
    }
    fun deleteByID(id: Long): Uni<Boolean> {
        return logRepo.deleteById(id)
    }
}