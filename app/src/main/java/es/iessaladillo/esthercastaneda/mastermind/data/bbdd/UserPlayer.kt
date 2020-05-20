package es.iessaladillo.esthercastaneda.mastermind.data.bbdd

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "userplayer",
    indices = [
        Index(
            name = "USERS_INDEX_NAME_UNIQUE",
            value = ["nameUser"],
            unique = true
        )
    ]
)
data class UserPlayer(
    @PrimaryKey(autoGenerate = true) val idUser: Long,
    @ColumnInfo(name = "nameUser", defaultValue = "Player")
    var nameUser: String
)