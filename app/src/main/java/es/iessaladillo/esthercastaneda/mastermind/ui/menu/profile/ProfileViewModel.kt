package es.iessaladillo.esthercastaneda.mastermind.ui.menu.profile

import android.app.Application
import androidx.lifecycle.ViewModel
import es.iessaladillo.esthercastaneda.mastermind.data.bbdd.UserDao

class ProfileViewModel(private val userDao: UserDao, private val application: Application): ViewModel() {
    val databaseUser = userDao.queryAllUsers()
}