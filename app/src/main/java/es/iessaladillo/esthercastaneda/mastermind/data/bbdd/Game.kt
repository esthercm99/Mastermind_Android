package es.iessaladillo.esthercastaneda.mastermind.data.bbdd

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.ForeignKey.CASCADE
import androidx.room.ForeignKey.RESTRICT
import androidx.room.PrimaryKey

@Entity(
    tableName = "Game"
)
data class Game (
    @PrimaryKey(autoGenerate = true) val idGame: Long,
    @ColumnInfo(name = "idUser")
    var idUser: Long,
    @ColumnInfo(name = "numRound")
    var numRound: Int,
    @ColumnInfo(name = "difficulty")
    var difficulty: String,
    @ColumnInfo(name = "gameMode")
    var gameMode: String,
    @ColumnInfo(name = "result")
    var result: String
)