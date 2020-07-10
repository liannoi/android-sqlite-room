package org.itstep.liannoi.sqliteroomusers.infrastructure.persistence.configurations

import androidx.room.*
import io.reactivex.Maybe

@Dao
interface UserDao {
    @Query("SELECT * FROM Users WHERE userId = :id")
    fun getById(id: Int): Maybe<User>

    @Query("SELECT * FROM Users")
    fun getAll(): Maybe<List<User>>

    @Insert
    fun create(user: User)

    @Update
    fun update(user: User)

    @Delete
    fun delete(user: User)
}
