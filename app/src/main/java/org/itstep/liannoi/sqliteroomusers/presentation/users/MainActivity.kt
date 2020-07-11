package org.itstep.liannoi.sqliteroomusers.presentation.users

import android.content.Intent
import android.os.Bundle
import com.jakewharton.rxbinding4.view.clicks
import com.trello.rxlifecycle4.android.lifecycle.kotlin.bindToLifecycle
import kotlinx.android.synthetic.main.activity_main.*
import org.itstep.liannoi.sqliteroomusers.R
import org.itstep.liannoi.sqliteroomusers.application.common.exceptions.NotFoundFetchedUserException
import org.itstep.liannoi.sqliteroomusers.application.storage.users.UsersRepository
import org.itstep.liannoi.sqliteroomusers.application.storage.users.commands.CreateCommand
import org.itstep.liannoi.sqliteroomusers.application.storage.users.commands.DeleteCommand
import org.itstep.liannoi.sqliteroomusers.application.storage.users.commands.UpdateCommand
import org.itstep.liannoi.sqliteroomusers.application.storage.users.queries.DetailQuery
import org.itstep.liannoi.sqliteroomusers.application.storage.users.queries.ListQuery
import org.itstep.liannoi.sqliteroomusers.infrastructure.persistence.configurations.User
import org.itstep.liannoi.sqliteroomusers.presentation.AbstractActivity
import org.itstep.liannoi.sqliteroomusers.presentation.users.list.UsersListActivity
import java.util.*

class MainActivity : AbstractActivity(),
    CreateCommand.Handler,
    ListQuery.Handler,
    DetailQuery.Handler,
    DeleteCommand.Handler,
    UpdateCommand.Handler {

    private val repository: UsersRepository = UsersRepository(this)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        catchErrors()
        subscribeGetById()
        subscribeGetAll()
        subscribeCreate()
        subscribeUpdate()
        subscribeDelete()
    }

    ///////////////////////////////////////////////////////////////////////////
    // CreateCommand.Handler
    ///////////////////////////////////////////////////////////////////////////

    override fun onUserCreatedSuccess() {
        showToast("New user created")
    }

    override fun onUserCreatedError(exception: String) {
        processException(exception, "onUserCreatedError: ")
    }

    ///////////////////////////////////////////////////////////////////////////
    // ListQuery.Handler
    ///////////////////////////////////////////////////////////////////////////

    override fun onUsersFetchedSuccess(users: List<User>) {
        showToast("There are ${users.size} users in the database")
    }

    override fun onUsersFetchedError(exception: String) {
        processException(exception, "onUsersFetchedError: ")
    }

    ///////////////////////////////////////////////////////////////////////////
    // DetailQuery.Handler
    ///////////////////////////////////////////////////////////////////////////

    override fun onUserFetchedSuccess(user: User) {
        showToast(user.toString())
    }

    override fun onUserFetchedError(exception: String) {
        processException(exception, "onUserFetchedError: ")
    }

    override fun onUserFetchedEmpty(exception: NotFoundFetchedUserException) {
        exception.message?.let { processException(it, "onUserFetchedEmpty: ") }
    }

    ///////////////////////////////////////////////////////////////////////////
    // DeleteCommand.Handler
    ///////////////////////////////////////////////////////////////////////////

    override fun onUserDeletedSuccess() {
        showToast("User successfully deleted")
    }

    override fun onUserDeletedError(exception: String) {
        processException(exception, "onUserDeletedError: ")
    }

    ///////////////////////////////////////////////////////////////////////////
    // UpdateCommand.Handler
    ///////////////////////////////////////////////////////////////////////////

    override fun onUserUpdatedSuccess() {
        showToast("User updated successfully")
    }

    override fun onUserUpdatedError(exception: String) {
        processException(exception, "onUserUpdatedError: ")
    }

    ///////////////////////////////////////////////////////////////////////////
    // Subscriptions
    ///////////////////////////////////////////////////////////////////////////

    private fun subscribeUpdate() {
        update_user_button.clicks()
            .map { update_user_input.text.toString().toInt() }
            .bindToLifecycle(this)
            .subscribe {
                repository.update(UpdateCommand(it, "Test Update", "Test Update"), this)
            }
    }

    private fun subscribeDelete() {
        delete_user_button.clicks()
            .map { delete_user_input.text.toString().toInt() }
            .bindToLifecycle(this)
            .subscribe {
                repository.delete(DeleteCommand(it), this)
            }
    }

    private fun subscribeCreate() {
        create_user_button.clicks()
            .bindToLifecycle(this)
            .subscribe {
                repository.create(CreateCommand(UUID.randomUUID().toString(), "Test"), this)
            }
    }

    private fun subscribeGetAll() {
        get_all_users_button.clicks()
            .bindToLifecycle(this)
            .subscribe { startActivity(Intent(this, UsersListActivity::class.java)) }
    }

    private fun subscribeGetById() {
        get_by_id_user_button.clicks()
            .map { get_by_id_user_input.text.toString().toInt() }
            .bindToLifecycle(this)
            .subscribe {
                repository.getById(DetailQuery(it), this)
            }
    }

    ///////////////////////////////////////////////////////////////////////////
    // Dispose
    ///////////////////////////////////////////////////////////////////////////

    override fun onStop() {
        super.onStop()
        repository.stop()
    }

    override fun onDestroy() {
        super.onDestroy()
        repository.destroy()
    }
}
