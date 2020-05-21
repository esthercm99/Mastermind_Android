package es.iessaladillo.esthercastaneda.mastermind.data.bbdd

import androidx.lifecycle.LiveData
import androidx.room.*
import es.iessaladillo.esthercastaneda.mastermind.data.pojo.UserGame

@Dao
interface UserDao {
    @Insert
    fun insertUser(userPlayer: UserPlayer)

    @Update
    fun updateUser(userPlayer: UserPlayer)

    @Delete
    fun deleteUser(userPlayer: UserPlayer)

    @Query("SELECT * FROM UserPlayer")
    fun queryAllUsers() : LiveData<List<UserPlayer>>

    @Query("SELECT * FROM UserPlayer WHERE idUser LIKE :idUser")
    fun queryUser(idUser: Long) : UserPlayer

    @Query("SELECT * FROM UserPlayer WHERE nameUser LIKE :name")
    fun queryUser(name: String) : UserPlayer

    @Query("SELECT COUNT(*) FROM UserPlayer")
    fun queryCountUsers() : Int

    @Query("SELECT U.nameUser AS namePlayer, " +
            "COUNT(G.idGame) AS numGame, " +
            "COALESCE(SUM(CASE WHEN G.result like 'w' THEN 1 ELSE 0 END), 0) AS numGameWin, " +
            "COALESCE(SUM(CASE WHEN G.result like 'l' THEN 1 ELSE 0 END), 0) AS numGameLose " +
            "FROM UserPlayer AS U " +
            "LEFT JOIN Game AS G ON U.idUser = G.idUser " +
            "WHERE U.idUser = :idUser")
    fun getInfoUserGame(idUser: Long): UserGame
}