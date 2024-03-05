package models

import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.NotNull
import jakarta.validation.constraints.Size
import org.jboss.resteasy.reactive.DateFormat
import java.util.*

const val codes =
    "ENABLED -> machine enabled \n" +
            "STARTED -> machine started normally \n" +
            "RUNNING -> machine running normally \n" +
            "STOPPED -> machine stopped doing work \n" +
            "DISABLED -> machine disabled \n" +
            "FORCE_DISABLED -> machine disabled due to a non-optimal situation (starved usually)"
class LogApi {
    @NotNull
    @NotBlank(message = "Date must be included with a format of 'yyyy-dd-mm'")
    @Size(max = 16, message = "Date must be less than 16 characters")
    @Size(min = 10, message = "Date must be larger than 9 characters")
    @setparam:DateFormat(pattern = "yyyy-DD-MM")
    lateinit var date: Date

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