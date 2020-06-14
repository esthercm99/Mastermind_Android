package es.iessaladillo.esthercastaneda.mastermind.ui.menu.manageUsers

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
import androidx.core.widget.addTextChangedListener
import androidx.lifecycle.observe
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.preference.PreferenceManager
import androidx.recyclerview.widget.LinearLayoutManager
import es.iessaladillo.esthercastaneda.mastermind.R
import es.iessaladillo.esthercastaneda.mastermind.data.bbdd.DatabaseUser
import es.iessaladillo.esthercastaneda.mastermind.data.bbdd.UserPlayer
import kotlinx.android.synthetic.main.main_activity.*
import kotlinx.android.synthetic.main.manage_fragment.*
import kotlinx.android.synthetic.main.manage_fragment.lstUsers
import kotlin.concurrent.thread

class ManageFragment : Fragment(R.layout.manage_fragment) {

    private var manageAdapter = ManageAdapter()
    private val navController: NavController by lazy {
        NavHostFragment.findNavController(navHostFragment)
    }
    private val settings: SharedPreferences by lazy {
        PreferenceManager.getDefaultSharedPreferences(context)
    }
    private val viewModel: ManageViewModel by viewModels {
        ManageViewModelFactory(DatabaseUser.getInstance(requireContext()).userDao, requireActivity().application)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        setupAdapter()
        setupRecyclerView()
        submitList()
        setupViews()
    }

    private fun submitList() {
        viewModel.list.observe(viewLifecycleOwner) {
            lstUsers.post {
                manageAdapter.submitListUsers(it)
            }
        }
    }
    private fun setupAdapter() {
        manageAdapter = ManageAdapter().also {
            it.onItemClickListener = { position -> manageUser(position) }
        }
    }
    private fun setupRecyclerView() {
        lstUsers.run {
            setHasFixedSize(true)
            layoutManager = LinearLayoutManager(context)
            adapter = manageAdapter
        }
    }

    private fun setupViews() {

        if (settings.getLong(getString(R.string.key_currentIdUser), -1L) == -1L) {
            btnBack.visibility = View.INVISIBLE
        } else {
            btnBack.visibility = View.VISIBLE
        }


        when(settings.getInt(getString(R.string.key_optionUser), 0)) {
            0 -> {
                namePlayer.text = getString(R.string.title_change_player)
            }
            1 -> {
                namePlayer.text = getString(R.string.title_edit_player)
            }
            2 -> {
                namePlayer.text = getString(R.string.title_delete_player)
            }
        }
        btnBack.setOnClickListener { navController.navigateUp() }
    }

    private fun manageUser(position: Int) {
        val userPlayer = viewModel.list.value?.get(position)!!

        when(settings.getInt(getString(R.string.key_optionUser), 0)) {
            0 -> {
                changeUser(userPlayer)
            }
            1 -> {
                editUser(userPlayer)
            }
            2 -> {
                askRemove(userPlayer)
            }
        }
    }
    private fun askRemove(userPlayer: UserPlayer) {
        AlertDialog.Builder(context).setTitle(getString(R.string.title_delete_player))
            .setMessage(String.format(getString(R.string.msg_askRemove), userPlayer.nameUser))
            .setCancelable(false)
            .setPositiveButton(getString(R.string.txtYes)) { _, _ -> removeUser(userPlayer) }
            .setNegativeButton(getString(R.string.txtNo)){ _, _ -> }
            .show()
    }
    private fun removeUser(userPlayer: UserPlayer) {
        var message = ""
        val nameDeleted = userPlayer.nameUser

        thread {
            val db = viewModel.usersdb
            val numUsers = db.queryCountUsers()

            if(numUsers <= 1) {
                message = getString(R.string.msg_only_player)
            } else {
                db.deleteUser(userPlayer)
                message = String.format(getString(R.string.msg_removed_player), nameDeleted)

                if(userPlayer.idUser == settings.getLong(getString(R.string.key_currentIdUser), -1L)) {
                    namePlayer.text = getString(R.string.title_change_player)
                    settings.edit {
                        putInt(getString(R.string.key_optionUser), 0)
                    }
                    btnBack.visibility = View.INVISIBLE
                }
            }
        }.join()

        Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
    }
    private fun editUser(userPlayer: UserPlayer) {
        val inflater = layoutInflater
        val builder = AlertDialog.Builder(requireContext())
        val dialogLayout = inflater.inflate(R.layout.alert_dialog_edittext, null)
        val editText  = dialogLayout.findViewById<EditText>(R.id.editText)
        val btnEditAdd  = dialogLayout.findViewById<TextView>(R.id.btnAddEdit)
        val btnCancel  = dialogLayout.findViewById<TextView>(R.id.btnCancel)

        btnEditAdd.text = getString(R.string.btn_edit)

        builder.setTitle(getString(R.string.title_write_name))
            .setView(dialogLayout)

        if (settings.getLong(getString(R.string.key_currentIdUser), -1L) != -1L) {
            builder.setCancelable(true)
        }

        editText.addTextChangedListener(object : TextWatcher {

            override fun afterTextChanged(p0: Editable?) {
                if (p0.toString().trim().isNotEmpty()) {
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

            val oldName = userPlayer.nameUser
            userPlayer.nameUser = namePlayer

            thread {
                val userDao = viewModel.usersdb

                if(userDao.queryCountNameUser(namePlayer) == 0) {
                    userDao.updateUser(userPlayer)
                    message = String.format(getString(R.string.msg_edited), oldName, namePlayer)
                } else {
                    message = getString(R.string.msg_player_exist)
                }

            }.join()

            if (settings.getLong(getString(R.string.key_currentIdUser), -1L) == userPlayer.idUser) {
                settings.edit {
                    putString(getString(R.string.prefPlayerName_key), userPlayer.nameUser)
                }
            }

            Toast.makeText(context, message, Toast.LENGTH_LONG).show()
            dialog.dismiss()
        }

        btnCancel.setOnClickListener { dialog.dismiss() }

    }
    private fun changeUser(userPlayer: UserPlayer) {
        settings.edit {
            putLong(getString(R.string.key_currentIdUser), userPlayer.idUser)
            putString(getString(R.string.prefPlayerName_key), userPlayer.nameUser)
        }
        navController.navigateUp()
    }
}
