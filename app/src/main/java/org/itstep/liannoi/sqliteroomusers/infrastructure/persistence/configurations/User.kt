package org.itstep.liannoi.sqliteroomusers.infrastructure.persistence.configurations

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "Users")
data class User(
    @ColumnInfo(name = "FirstName")
    var firstName: String,
    @ColumnInfo(name = "LastName")
    var lastName: String
) {
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "UserId")
    var userId: Int = 0

    override fun toString(): String {
        return "User(firstName='$firstName', lastName='$lastName', userId=$userId)"
    }
}
