package es.iessaladillo.esthercastaneda.mastermind.ui.menu.profile

import android.app.AlertDialog
import android.content.SharedPreferences
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.EditText
import android.widget.TextView
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
import java.util.regex.Matcher
import java.util.regex.Pattern
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

    /*
        Configuración de las vistas.
     */
    private fun setupViews() {
        setupUserInfo()
        setupButtons()
    }

    /*
        Configuración de botones.
     */
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

    /*
        Coloca el nombre del jugador con el
        nº de partidas ganadas, perdidas y tatales.
     */
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

    /*
        Se añade un jugador.
     */
    private fun addUser() {
        val inflater = layoutInflater
        val builder = AlertDialog.Builder(requireContext())
        val dialogLayout = inflater.inflate(R.layout.alert_dialog_edittext, null)
        val editText  = dialogLayout.findViewById<EditText>(R.id.editText)
        val btnEditAdd  = dialogLayout.findViewById<TextView>(R.id.btnAddEdit)
        val btnCancel  = dialogLayout.findViewById<TextView>(R.id.btnCancel)

        btnEditAdd.text = getString(R.string.btn_add)

        builder.setTitle(getString(R.string.title_write_name))
                .setMessage(getString(R.string.msg_requiredName))
                .setView(dialogLayout)
                .setCancelable(false)

        if (settings.getLong(getString(R.string.key_currentIdUser), -1L) != -1L) {
            btnCancel.visibility = View.VISIBLE
        } else {
            btnCancel.visibility = View.INVISIBLE
        }

        editText.addTextChangedListener(object : TextWatcher {

            override fun afterTextChanged(p0: Editable?) {
                val pattern: Pattern = Pattern.compile("\\w{3,10}", Pattern.CASE_INSENSITIVE)
                val matcher: Matcher = pattern.matcher(p0.toString())

                if (matcher.matches()) {
                    btnEditAdd.visibility = View.VISIBLE
                } else {
                    btnEditAdd.visibility = View.INVISIBLE
                }
            }

            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
        })

        val dialog = builder.show()

        btnEditAdd.setOnClickListener {
            var message = ""
            val namePlayer = editText.text.toString()

            thread {
                if(viewModel.usersdb.queryCountNameUser(namePlayer) == 0) {
                    viewModel.usersdb.insertUser(UserPlayer(0, namePlayer))
                    message = String.format(getString(R.string.msg_inserted), namePlayer)
                } else {
                    message = getString(R.string.msg_player_exist)
                }
            }.join()

            if (settings.getLong(getString(R.string.key_currentIdUser), -1L) == -1L) { firstUser() }

            Toast.makeText(context, message, Toast.LENGTH_LONG).show()
            dialog.dismiss()
        }

        btnCancel.setOnClickListener { dialog.dismiss() }
    }

    /*
        Te lleva a seleccionar el primer jugador creado.
     */
    private fun firstUser() {
        settings.edit {
            putInt(getString(R.string.key_optionUser), 0)
        }
        navController.navigate(R.id.action_profileFragment_to_manageFragment)
    }
}
