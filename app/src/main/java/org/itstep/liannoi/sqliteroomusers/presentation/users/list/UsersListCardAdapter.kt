package org.itstep.liannoi.sqliteroomusers.presentation.users.list

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.adapter_card_users_list.view.*
import org.itstep.liannoi.sqliteroomusers.R
import org.itstep.liannoi.sqliteroomusers.infrastructure.persistence.configurations.User

class UsersListCardAdapter(private val users: List<User>) :
    RecyclerView.Adapter<UsersListCardAdapter.ViewHolder>() {

    override fun onCreateViewHolder(container: ViewGroup, viewType: Int): ViewHolder {
        val view: View = LayoutInflater.from(container.context)
            .inflate(R.layout.adapter_card_users_list, container, false)

        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return users.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val model: User = users[position]
        holder.fullNameText.text = "[${model.userId}] ${model.firstName} ${model.lastName}"
    }

    class ViewHolder(container: View) : RecyclerView.ViewHolder(container) {
        val fullNameText: TextView = container.full_name_text
    }
}
