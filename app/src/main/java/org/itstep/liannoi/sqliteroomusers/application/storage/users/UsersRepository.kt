package org.itstep.liannoi.sqliteroomusers.application.storage.users

import android.content.Context
import io.reactivex.Completable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import org.itstep.liannoi.sqliteroomusers.application.storage.users.commands.CreateCommand
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
                { handler.onUserCreatedError(it.message) })
            .follow()
    }

    fun getAll(query: ListQuery, handler: ListQuery.Handler) {
        dao.getAll()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({ handler.onUsersFetchedSuccess(it) }, { handler.onUsersFetchedError() })
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
