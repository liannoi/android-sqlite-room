package org.itstep.liannoi.sqliteroomusers.application.common.exceptions

class NotFoundFetchedUserException(message: String = "The user at your request was not found in the database.") :
    Exception(message)
