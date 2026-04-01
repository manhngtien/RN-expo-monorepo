package expo.modules.datasyncnativekotlin.domain.exception

import expo.modules.kotlin.exception.CodedException

/**
 * Base class for all native core exceptions in the data sync module.
 * Extends [CodedException] to provide a standardized error format for Expo modules.
 *
 * @property message The error message.
 * @property details Additional details related to the exception.
 */
open class BaseNativeCoreException(
    override val message: String,
    val details: Map<String, Any>? = null
) : CodedException(message)

/**
 * Exception thrown when the current Activity screen was not found.
 */
class ActivityNotFoundException : BaseNativeCoreException(
    message = "The current Activity screen was not found. Please restart the application."
)

/**
 * Exception thrown when this device does not support NFC hardware.
 */
class NfcNotSupportedException : BaseNativeCoreException(
    message = "This device does not support NFC hardware."
)

/**
 * Exception thrown when NFC functionality is disabled on the device.
 */
class NfcDisabledCoreException : BaseNativeCoreException(
    message = "NFC is turned off on the device."
)

/**
 * Exception thrown when an NFC reader operation times out.
 */
class NfcReaderTimeoutCoreException : BaseNativeCoreException(
    message = "NFC reader timeout."
)

/**
 * Exception thrown when a database record with the specified [id] cannot be found.
 *
 * @param id The identifier of the missing record.
 */
class DbRecordNotFoundCoreException(id: String) : BaseNativeCoreException(
    message = "No data found matching ID: $id"
)

/**
 * Exception thrown when a database operation fails due to a constraint violation.
 *
 * @param detail Specific information about the constraint violation.
 */
class DbConstraintViolationCoreException(detail: String) : BaseNativeCoreException(
    message = "Duplicate data or constraint violation: $detail"
)

/**
 * Exception thrown when the device's storage is full, preventing database operations.
 */
class DbStorageFullCoreException : BaseNativeCoreException(
    message = "The tablet's memory is full."
)

