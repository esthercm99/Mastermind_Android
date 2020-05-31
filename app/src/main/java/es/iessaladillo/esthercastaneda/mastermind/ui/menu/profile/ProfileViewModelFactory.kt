package es.iessaladillo.esthercastaneda.mastermind.ui.menu.profile

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import es.iessaladillo.esthercastaneda.mastermind.data.bbdd.UserDao

class ProfileViewModelFactory (private val userDao: UserDao, private val application: Application) : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T = ProfileViewModel(userDao, application) as T
}