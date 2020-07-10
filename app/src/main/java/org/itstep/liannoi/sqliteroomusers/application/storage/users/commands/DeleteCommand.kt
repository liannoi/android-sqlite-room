package org.itstep.liannoi.sqliteroomusers.application.storage.users.commands

class DeleteCommand(val userId: Int) {
    interface Handler {
        fun onUserDeletedSuccess()

        // TODO: Replace with custom Exception.
        fun onUserDeletedError(exception: String)
    }
}
