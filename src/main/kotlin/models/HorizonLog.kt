package models

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.Id
import org.jetbrains.annotations.NotNull
import java.util.Date

@Entity
class HorizonLog {
    @Id
    @GeneratedValue
    var id: Long? = null

    @Column(length = 10, nullable = false)
    lateinit var date: Date
    @Column(length = 40, nullable = false)
    lateinit var machine: String
    @Column(length = 40, nullable = false)
    lateinit var machineID: String
    @Column(length = 100, nullable = false)
    lateinit var massage: String
    // TODO Change to enum
    /*
    Codes:
    ENABLED -> machine enabled
    STARTED -> machine started normally
    RUNNING -> machine running normally
    STOPPED -> machine stopped doing work
    DISABLED -> machine disabled
    FORCE_DISABLED -> machine disabled due to a non-optimal situation (starved usually)
     */
    @Column(length = 10, nullable = false)
    lateinit var operationCode: String

}