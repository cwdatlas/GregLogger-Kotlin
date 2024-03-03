package repos

import io.quarkus.hibernate.reactive.panache.kotlin.PanacheRepository
import jakarta.enterprise.context.ApplicationScoped
import models.HorizonLog

@ApplicationScoped
class LogRepo: PanacheRepository<HorizonLog> {
}