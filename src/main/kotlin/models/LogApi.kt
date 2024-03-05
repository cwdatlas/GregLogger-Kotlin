package models

import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.NotNull
import jakarta.validation.constraints.Size
import org.jboss.resteasy.reactive.DateFormat
import java.time.LocalDate

// codes provide a constant set of feedback for the user
const val codes =
    "ENABLED -> machine enabled, " +
            "STARTED -> machine started normally, " +
            "RUNNING -> machine running normally, " +
            "STOPPED -> machine stopped doing work, " +
            "DISABLED -> machine disabled, " +
            "FORCE_DISABLED -> machine disabled due to a non-optimal situation (starved usually)"

/**
 * @author Aidan Scott
 * LogApi is the validation model for the HorizonLog entity model.
 * This is where all validations take place, and are where feedback is stored.
 * If you want to see more about what each value does, go to HorizonLog
 */
class LogApi {
    // Dates must be in this pattern if they are not, then the data will be sent back signaling that the data is wrong
    @setparam:DateFormat(pattern = "YYYY-MM-DD")
    lateinit var date: LocalDate

    @NotNull(message = "machine must be included")
    @NotBlank(message = "machine must not be blank")
    @Size(max = 40, message = "Machine length must be less than 40 characters")
    @Size(min = 3, message = "Machine length must be more than 3 characters")
    lateinit var machine: String

    @NotNull(message = "machineID must be included")
    @NotBlank(message = "machineID must not be blank")
    @Size(max = 40, message = "machineID length must be less than 40 characters")
    @Size(min = 3, message = "machineID name length must be more than 3 characters")
    lateinit var machineID: String

    @NotNull(message = "message must be included")
    @NotBlank(message = "message must not be blank")
    @Size(max = 100, message = "message length must be less than 100 characters")
    @Size(min = 1, message = "message length must be more than 0 characters")
    lateinit var message: String


    // TODO Change to enum
    @NotNull(message = "operationCode must be included. Codes: $codes")
    @NotBlank(message = "operationCode must not be blank. Codes: $codes")
    @Size(max = 20, message = "operationCode length must be less than 100 characters. Codes: $codes")
    @Size(min = 7, message = "operationCode length must be more than 0 characters. Codes: $codes")
    lateinit var operationCode: String
}