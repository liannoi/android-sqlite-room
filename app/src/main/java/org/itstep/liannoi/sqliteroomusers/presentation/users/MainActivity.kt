package org.itstep.liannoi.sqliteroomusers.presentation.users

import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Lifecycle
import com.jakewharton.rxbinding4.view.clicks
import com.trello.rxlifecycle4.android.lifecycle.kotlin.bindUntilEvent
import io.reactivex.plugins.RxJavaPlugins
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
import java.util.*

class MainActivity : AppCompatActivity(),
    CreateCommand.Handler,
    ListQuery.Handler,
    DetailQuery.Handler,
    DeleteCommand.Handler,
    UpdateCommand.Handler {
    private val repository: UsersRepository = UsersRepository(this)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        RxJavaPlugins.setErrorHandler(Throwable::printStackTrace)
        getAllUsers()

        get_by_id_user_button.clicks()
            .map { get_by_id_user_input.text.toString().toInt() }
            .bindUntilEvent(this, Lifecycle.Event.ON_STOP)
            .subscribe { getByIdUser(it) }

        get_all_users_button.clicks()
            .bindUntilEvent(this, Lifecycle.Event.ON_STOP)
            .subscribe { getAllUsers() }

        create_user_button.clicks()
            .bindUntilEvent(this, Lifecycle.Event.ON_STOP)
            .subscribe { createUser() }

        update_user_button.clicks()
            .map { update_user_input.text.toString().toInt() }
            .bindUntilEvent(this, Lifecycle.Event.ON_STOP)
            .subscribe { updateUser(it) }

        delete_user_button.clicks()
            .map { delete_user_input.text.toString().toInt() }
            .bindUntilEvent(this, Lifecycle.Event.ON_STOP)
            .subscribe { deleteUser(it) }
    }

    ///////////////////////////////////////////////////////////////////////////
    // CreateCommand.Handler
    ///////////////////////////////////////////////////////////////////////////

    override fun onUserCreatedSuccess() {
        Toast.makeText(this, "New user created", Toast.LENGTH_SHORT).show()
    }

    override fun onUserCreatedError(exception: String) {
        processException(exception, "onUserCreatedError: ")
    }

    ///////////////////////////////////////////////////////////////////////////
    // ListQuery.Handler
    ///////////////////////////////////////////////////////////////////////////

    override fun onUsersFetchedSuccess(users: List<User>) {
        Toast.makeText(
            this,
            "There are ${users.size} users in the database",
            Toast.LENGTH_SHORT
        ).show()
    }

    override fun onUsersFetchedError(exception: String) {
        processException(exception, "onUsersFetchedError: ")
    }

    ///////////////////////////////////////////////////////////////////////////
    // DetailQuery.Handler
    ///////////////////////////////////////////////////////////////////////////

    override fun onUserFetchedSuccess(user: User) {
        Toast.makeText(this, user.toString(), Toast.LENGTH_LONG).show()
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
        Toast.makeText(this, "User successfully deleted", Toast.LENGTH_SHORT).show()
    }

    override fun onUserDeletedError(exception: String) {
        processException(exception, "onUserDeletedError: ")
    }

    ///////////////////////////////////////////////////////////////////////////
    // UpdateCommand.Handler
    ///////////////////////////////////////////////////////////////////////////

    override fun onUserUpdatedSuccess() {
        Toast.makeText(this, "User updated successfully", Toast.LENGTH_SHORT).show()
    }

    override fun onUserUpdatedError(exception: String) {
        processException(exception, "onUserUpdatedError: ")
    }

    ///////////////////////////////////////////////////////////////////////////
    // Helpers
    ///////////////////////////////////////////////////////////////////////////

    private fun processException(exception: String, method: String) {
        exception.also {
            Log.d(method, it)
            Toast.makeText(this, it, Toast.LENGTH_LONG).show()
        }
    }

    private fun getByIdUser(it: Int) {
        repository.getById(DetailQuery(it), this)
    }

    private fun createUser() {
        repository.create(CreateCommand(UUID.randomUUID().toString(), "Test"), this)
    }

    private fun getAllUsers() {
        repository.getAll(ListQuery(), this)
    }

    private fun updateUser(id: Int) {
        repository.update(UpdateCommand(id, "Test Update", "Test Update"), this)
    }

    private fun deleteUser(id: Int) {
        repository.delete(DeleteCommand(id), this)
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
