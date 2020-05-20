package es.iessaladillo.esthercastaneda.mastermind.ui.menu.manageUsers

import android.app.AlertDialog
import android.content.SharedPreferences
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.core.content.edit
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
        isListEmpty()
        when(settings.getInt(getString(R.string.key_optionUser), 0)) {
            0 -> {
                namePlayer.text = "Changing player by..."
            }
            1 -> {
                namePlayer.text = "Edit name player"
            }
            2 -> {
                namePlayer.text = "Delete player"
            }
        }
        btnBack.setOnClickListener { navController.navigateUp() }
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
        AlertDialog.Builder(context).setTitle(getString(R.string.quitGame))
            .setMessage(String.format("¿Quieres borrar a %s?", userPlayer.nameUser))
            .setCancelable(false)
            .setPositiveButton(getString(R.string.txtYes)) { _, _ -> removeUser(userPlayer) }
            .setNegativeButton(getString(R.string.txtNo)){ _, _ -> }
            .show()
    }

    private fun isListEmpty() {
        thread {
            if (DatabaseUser.getInstance(requireContext()).userDao.queryAllUsers().value?.size == 0) {
                settings.edit {
                    putInt(getString(R.string.key_optionUser), -1)
                }
                navController.navigateUp()
            }
        }
    }

    private fun removeUser(userPlayer: UserPlayer) {
        var isEmptyUsers = false
        Toast.makeText(context, String.format("Has eliminado a %s", userPlayer.nameUser), Toast.LENGTH_SHORT).show()

        thread {
            DatabaseUser.getInstance(requireContext()).userDao.deleteUser(userPlayer)
        }


        if(userPlayer.idUser == settings.getLong(getString(R.string.key_currentIdUser), -1L)) {
            namePlayer.text = "Changing player by..."
            settings.edit {
                putInt(getString(R.string.key_optionUser), 0)
            }
            btnBack.visibility = View.INVISIBLE
        }

    }

    private fun editUser(userPlayer: UserPlayer) {

    }

    private fun changeUser(userPlayer: UserPlayer) {
        settings.edit {
            putLong(getString(R.string.key_currentIdUser), userPlayer.idUser)
        }
        navController.navigateUp()
    }



}
