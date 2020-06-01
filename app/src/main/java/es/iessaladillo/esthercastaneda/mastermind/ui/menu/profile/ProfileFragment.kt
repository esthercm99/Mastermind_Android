package es.iessaladillo.esthercastaneda.mastermind.ui.menu.profile

import android.app.AlertDialog
import android.content.SharedPreferences
import android.os.Bundle
import android.widget.EditText
import android.widget.Toast
import androidx.core.content.edit
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.preference.PreferenceManager
import es.iessaladillo.esthercastaneda.mastermind.R
import es.iessaladillo.esthercastaneda.mastermind.data.bbdd.DatabaseUser
import es.iessaladillo.esthercastaneda.mastermind.data.bbdd.UserPlayer
import kotlinx.android.synthetic.main.main_activity.*
import kotlinx.android.synthetic.main.profile_fragment.*
import kotlin.concurrent.thread

class ProfileFragment : Fragment(R.layout.profile_fragment) {

    private val navController: NavController by lazy {
        NavHostFragment.findNavController(navHostFragment)
    }
    private val settings: SharedPreferences by lazy {
        PreferenceManager.getDefaultSharedPreferences(context)
    }
    private val viewModel: ProfileViewModel by viewModels {
        ProfileViewModelFactory(DatabaseUser.getInstance(requireContext()).userDao, requireActivity().application)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        setupViews()
    }

    private fun setupViews() {
        setupUserInfo()
        setupButtons()
    }

    private fun setupButtons() {
        btnHome.setOnClickListener { navController.navigateUp() }

        btnAdd.setOnClickListener { addUser() }
        btnChangeUser.setOnClickListener {
            settings.edit {
                putInt(getString(R.string.key_optionUser), 0)
            }
            navController.navigate(R.id.action_profileFragment_to_manageFragment)
        }
        btnEdit.setOnClickListener {
            settings.edit {
                putInt(getString(R.string.key_optionUser), 1)
            }
            navController.navigate(R.id.action_profileFragment_to_manageFragment)
        }
        btnRemove.setOnClickListener {
            settings.edit {
                putInt(getString(R.string.key_optionUser), 2)
            }
            navController.navigate(R.id.action_profileFragment_to_manageFragment)
        }
    }
    private fun setupUserInfo() {
        val id = settings.getLong(getString(R.string.key_currentIdUser), -1)

        if(id != -1L) {
            thread {
                    val user = viewModel.usersdb.getInfoUserGame(id)

                    nameUser.text = String.format(getString(R.string.namePlayer, user.namePlayer))
                    lblNumGames.text = String.format(getString(R.string.n_games), user.numGame)
                    lblNumGamesWin.text = String.format(getString(R.string.n_games_win), user.numGameWin)
                    lblNumGamesLose.text = String.format(getString(R.string.n_games_lose), user.numGameLose)
            }.join()
        } else {
            addUser()
        }
    }
    private fun addUser() {
        val inflater = layoutInflater
        val builder = AlertDialog.Builder(requireContext())
        val dialogLayout = inflater.inflate(R.layout.alert_dialog_edittext, null)
        val editText  = dialogLayout.findViewById<EditText>(R.id.editText)
        var message = ""

        builder.setTitle(getString(R.string.title_write_name))
                .setView(dialogLayout)
                .setCancelable(false)

        if (settings.getLong(getString(R.string.key_currentIdUser), -1L) != -1L) {
            builder.setNegativeButton(getString(R.string.btn_cancel)){ _, _-> }
        }

       builder.setPositiveButton(getString(R.string.btn_add)) { _, _ ->
           val namePlayer = editText.text.toString()

           if (namePlayer.trim().isNotEmpty()) {
               thread {
                   if(viewModel.usersdb.queryCountNameUser(namePlayer) == 0) {
                       viewModel.usersdb.insertUser(UserPlayer(0, namePlayer))
                       message = String.format(getString(R.string.msg_inserted), namePlayer)
                   } else {
                       message = getString(R.string.msg_player_exist)
                   }
               }.join()

               if (settings.getLong(getString(R.string.key_currentIdUser), -1L) == -1L) { firstUser() }

           } else {
               message = getString(R.string.msg_empty_name)
           }

           Toast.makeText(context, message, Toast.LENGTH_LONG).show()
       }.show()
    }
    private fun firstUser() {
        settings.edit {
            putInt(getString(R.string.key_optionUser), 0)
        }
        navController.navigate(R.id.action_profileFragment_to_manageFragment)
    }
}
