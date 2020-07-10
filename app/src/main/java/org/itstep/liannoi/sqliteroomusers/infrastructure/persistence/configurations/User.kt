package org.itstep.liannoi.sqliteroomusers.infrastructure.persistence.configurations

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "Users")
data class User(
    @ColumnInfo(name = "FirstName")
    val firstName: String,
    @ColumnInfo(name = "LastName")
    val lastName: String
) {
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "UserId")
    var userId: Int = 0
}
