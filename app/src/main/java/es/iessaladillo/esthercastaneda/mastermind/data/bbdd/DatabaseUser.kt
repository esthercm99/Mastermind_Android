package es.iessaladillo.esthercastaneda.mastermind.data.bbdd

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(
    entities = [UserPlayer::class, Game::class],
    version = 1,
    exportSchema = true
)
abstract class DatabaseUser : RoomDatabase() {

    abstract val userDao: UserDao
    abstract val gameDao: GameDao

    companion object {

        @Volatile
        private var INSTANCE: DatabaseUser? = null

        fun getInstance(context: Context): DatabaseUser {
            if (INSTANCE == null) {
                synchronized(this) {
                    if (INSTANCE == null) {
                        INSTANCE = Room.databaseBuilder(
                            context.applicationContext,
                            DatabaseUser::class.java,
                            "database_user"
                        ).build()
                    }
                }
            }
            return INSTANCE!!
        }
    }
}