package org.itstep.liannoi.sqliteroomusers.presentation.users.list

import android.os.Bundle
import kotlinx.android.synthetic.main.activity_users_list.*
import org.itstep.liannoi.sqliteroomusers.R
import org.itstep.liannoi.sqliteroomusers.application.storage.users.UsersRepository
import org.itstep.liannoi.sqliteroomusers.application.storage.users.queries.ListQuery
import org.itstep.liannoi.sqliteroomusers.infrastructure.persistence.configurations.User
import org.itstep.liannoi.sqliteroomusers.presentation.AbstractActivity

class UsersListActivity : AbstractActivity(),
    ListQuery.Handler {

    private val repository: UsersRepository = UsersRepository(this)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_users_list)

        catchErrors()
        users_recycler_view.layout()
        repository.getAll(ListQuery(), this)
    }

    ///////////////////////////////////////////////////////////////////////////
    //  ListQuery.Handler
    ///////////////////////////////////////////////////////////////////////////

    override fun onUsersFetchedSuccess(users: List<User>) {
        adaptRecycler(users)
        showToast("There are ${users.size} users in the database")
    }

    override fun onUsersFetchedError(exception: String) {
        processException(exception, "onUsersFetchedError: ")
    }

    ///////////////////////////////////////////////////////////////////////////
    // Helpers
    ///////////////////////////////////////////////////////////////////////////

    private fun adaptRecycler(users: List<User>) {
        users_recycler_view.adapter = UsersListCardAdapter(users)
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
