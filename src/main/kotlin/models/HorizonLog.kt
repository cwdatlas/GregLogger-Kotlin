package models

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.Id
import java.util.Date

@Entity
class HorizonLog {
    @Id
    @GeneratedValue
    var id: Long? = null

    @Column(length = 16, nullable = false, name = "date")
    lateinit var date: Date
    @Column(length = 40, nullable = false, name = "machine")
    lateinit var machine: String
    @Column(length = 40, nullable = false, name = "machine_id")
    lateinit var machineID: String
    @Column(length = 100, nullable = false, name = "message")
    lateinit var message: String
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
    @Column(length = 20, nullable = false, name = "code")
    lateinit var operationCode: String

}