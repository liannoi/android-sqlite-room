package org.itstep.liannoi.sqliteroomusers.application.storage.users.queries

import org.itstep.liannoi.sqliteroomusers.application.common.exceptions.NotFoundFetchedUserException
import org.itstep.liannoi.sqliteroomusers.infrastructure.persistence.configurations.User

class DetailQuery(val userId: Int) {
    interface Handler {
        fun onUserFetchedSuccess(user: User)

        // TODO: Replace with custom Exception.
        fun onUserFetchedError(exception: String)

        fun onUserFetchedEmpty(exception: NotFoundFetchedUserException)
    }
}
