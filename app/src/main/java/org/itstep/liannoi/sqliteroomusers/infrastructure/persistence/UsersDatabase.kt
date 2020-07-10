package org.itstep.liannoi.sqliteroomusers.infrastructure.persistence

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import org.itstep.liannoi.sqliteroomusers.infrastructure.persistence.configurations.User
import org.itstep.liannoi.sqliteroomusers.infrastructure.persistence.configurations.UserDao

@Database(entities = [User::class], version = 1, exportSchema = false)
abstract class UsersDatabase : RoomDatabase() {
    abstract fun userDao(): UserDao

    /**
     * The thread-safe Singleton is implemented by Google. It is here, and not in "Manager", to
     * comply with the first SOLID principle (the principle of sole responsibility).
     */
    companion object {
        @Volatile
        private var INSTANCE: UsersDatabase? = null

        fun getInstance(context: Context): UsersDatabase =
            INSTANCE ?: synchronized(this) {
                INSTANCE ?: buildDatabase(context).also { INSTANCE = it }
            }

        private fun buildDatabase(context: Context) =
            Room.databaseBuilder(
                context.applicationContext,
                UsersDatabase::class.java,
                InfrastructureDefaults.DATABASE_NAME
            ).build()
    }
}
