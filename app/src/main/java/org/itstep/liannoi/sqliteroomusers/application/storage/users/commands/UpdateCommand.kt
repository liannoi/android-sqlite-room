package org.itstep.liannoi.sqliteroomusers.application.storage.users.commands

class UpdateCommand(
    val userId: Int,
    val firstName: String,
    val lastName: String
) {
    interface Handler {
        fun onUserUpdatedSuccess()

        // TODO: Replace with custom Exception.
        fun onUserUpdatedError(exception: String)
    }
}
