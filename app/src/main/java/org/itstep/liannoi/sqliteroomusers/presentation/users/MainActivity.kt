package org.itstep.liannoi.sqliteroomusers.presentation.users

import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import org.itstep.liannoi.sqliteroomusers.R
import org.itstep.liannoi.sqliteroomusers.application.storage.users.UsersRepository
import org.itstep.liannoi.sqliteroomusers.application.storage.users.commands.CreateCommand
import org.itstep.liannoi.sqliteroomusers.application.storage.users.queries.ListQuery
import org.itstep.liannoi.sqliteroomusers.infrastructure.persistence.configurations.User

class MainActivity : AppCompatActivity(), CreateCommand.Handler, ListQuery.Handler {
    private val repository: UsersRepository = UsersRepository(this)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        repository.create(CreateCommand("Hello", "World"), this)
    }

    override fun onUserCreatedSuccess() {
        Toast.makeText(this, "CREATED", Toast.LENGTH_SHORT).show()
        repository.getAll(ListQuery(), this)
    }

    override fun onUserCreatedError(message: String?) {
        Log.d("onUserCreatedError: ", message.toString())
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }

    override fun onUsersFetchedSuccess(users: List<User>) {
        //Toast.makeText(this, "Users: ${users.size}", Toast.LENGTH_SHORT).show()
    }

    override fun onUsersFetchedError() {
        TODO("Not yet implemented")
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
