package models

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.Id
import java.util.*

/**
 * @author Aidan Scott
 * HorizonLog is the data storage entity
 */
@Entity
class HorizonLog {
    // Generated ID
    @Id
    @GeneratedValue
    var id: Long? = null

    // Date is the date when the log was generated
    @Column(length = 16, nullable = false, name = "date")
    lateinit var date: Date

    // Machine is the specific machine type that is the subject of the log
    @Column(length = 40, nullable = false, name = "machine")
    lateinit var machine: String

    // MachineID is the ID of the machine as there can be many of the same machine type
    @Column(length = 40, nullable = false, name = "machine_id")
    lateinit var machineID: String

    // message is the status message that must be included in the log
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