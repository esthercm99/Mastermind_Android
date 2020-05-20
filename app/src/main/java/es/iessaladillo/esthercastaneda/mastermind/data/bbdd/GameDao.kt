package es.iessaladillo.esthercastaneda.mastermind.data.bbdd

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query

@Dao
interface GameDao {

    @Insert
    fun insertGame(game: Game)

    @Query("SELECT * FROM Game")
    fun queryAllGames() : LiveData<List<Game>>
}