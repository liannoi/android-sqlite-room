package org.itstep.liannoi.sqliteroomusers.application.storage.users.queries

import org.itstep.liannoi.sqliteroomusers.infrastructure.persistence.configurations.User

class ListQuery {
    interface Handler {
        fun onUsersFetchedSuccess(users: List<User>)

        // TODO: Replace with custom Exception.
        fun onUsersFetchedError(exception: String)
    }
}
