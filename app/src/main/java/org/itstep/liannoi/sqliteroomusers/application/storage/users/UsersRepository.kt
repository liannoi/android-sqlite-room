package org.itstep.liannoi.sqliteroomusers.application.storage.users

import android.content.Context
import io.reactivex.Completable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import org.itstep.liannoi.sqliteroomusers.application.common.exceptions.NotFoundFetchedUserException
import org.itstep.liannoi.sqliteroomusers.application.storage.users.commands.CreateCommand
import org.itstep.liannoi.sqliteroomusers.application.storage.users.commands.DeleteCommand
import org.itstep.liannoi.sqliteroomusers.application.storage.users.commands.UpdateCommand
import org.itstep.liannoi.sqliteroomusers.application.storage.users.queries.DetailQuery
import org.itstep.liannoi.sqliteroomusers.application.storage.users.queries.ListQuery
import org.itstep.liannoi.sqliteroomusers.infrastructure.persistence.UsersDatabase
import org.itstep.liannoi.sqliteroomusers.infrastructure.persistence.configurations.User
import org.itstep.liannoi.sqliteroomusers.infrastructure.persistence.configurations.UserDao

class UsersRepository constructor(private val context: Context) {
    private val disposable: CompositeDisposable = CompositeDisposable()

    private val dao: UserDao
        get() = UsersDatabase.getInstance(context).userDao()

    fun create(command: CreateCommand, handler: CreateCommand.Handler) {
        Completable.fromAction { dao.create(User(command.firstName, command.lastName)) }
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                { handler.onUserCreatedSuccess() },
                { handler.onUserCreatedError(it.message.toString()) })
            .follow()
    }

    fun getAll(query: ListQuery, handler: ListQuery.Handler) {
        dao.getAll()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                { handler.onUsersFetchedSuccess(it) },
                { handler.onUsersFetchedError(it.message.toString()) })
            .follow()
    }

    fun getById(query: DetailQuery, handler: DetailQuery.Handler) {
        dao.getById(query.userId)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                { handler.onUserFetchedSuccess(it) },
                { handler.onUserFetchedError(it.message.toString()) },
                { handler.onUserFetchedEmpty(NotFoundFetchedUserException()) })
            .follow()
    }

    fun delete(command: DeleteCommand, handler: DeleteCommand.Handler) {
        dao.getById(command.userId)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe {
                Completable.fromAction { dao.delete(it) }
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(
                        { handler.onUserDeletedSuccess() },
                        { handler.onUserDeletedError(it.message.toString()) })
                    .follow()
            }
            .follow()
    }

    fun update(command: UpdateCommand, handler: UpdateCommand.Handler) {
        dao.getById(command.userId)
            .map {
                it.firstName = command.firstName
                it.lastName = command.lastName
                it
            }
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe {
                Completable.fromAction { dao.update(it) }
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(
                        { handler.onUserUpdatedSuccess() },
                        { handler.onUserUpdatedError(it.message.toString()) })
                    .follow()
            }
            .follow()
    }

    ///////////////////////////////////////////////////////////////////////////
    // Dispose
    ///////////////////////////////////////////////////////////////////////////

    fun stop() {
        disposable.clear()
    }

    fun destroy() {
        disposable.dispose()
    }

    private fun Disposable.follow() {
        disposable.add(this)
    }
}
