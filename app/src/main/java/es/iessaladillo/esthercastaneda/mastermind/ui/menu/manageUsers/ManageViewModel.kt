package es.iessaladillo.esthercastaneda.mastermind.ui.menu.manageUsers

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import es.iessaladillo.esthercastaneda.mastermind.data.bbdd.DatabaseUser
import es.iessaladillo.esthercastaneda.mastermind.data.bbdd.UserDao
import es.iessaladillo.esthercastaneda.mastermind.data.bbdd.UserPlayer
import kotlinx.android.synthetic.main.manage_fragment.*

class ManageViewModel(private val userDao: UserDao, private val application: Application) : ViewModel() {
    var list: LiveData<List<UserPlayer>> = userDao.queryAllUsers()
}
