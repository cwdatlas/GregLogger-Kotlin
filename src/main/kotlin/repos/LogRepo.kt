package repos

import io.quarkus.hibernate.reactive.panache.kotlin.PanacheRepository
import jakarta.enterprise.context.ApplicationScoped
import models.HorizonLog

/**
 * @author Aidan Scott
 * LogRepo communicates with the datasource and granting access to it
 * Only allows access to writing, storing and deleting HorizonLog data
 */
@ApplicationScoped
class LogRepo : PanacheRepository<HorizonLog>