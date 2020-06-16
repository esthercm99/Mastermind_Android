package es.iessaladillo.esthercastaneda.mastermind

import android.os.Bundle
import android.view.View
import android.view.View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
import android.view.Window
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp

class MainActivity : AppCompatActivity() {

    private val INTERVAL = 2000
    private var timeFristClick: Long = 0

    private val navController: NavController by lazy {
        findNavController(R.id.navHostFragment)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        requestWindowFeature(Window.FEATURE_NO_TITLE)
        window.decorView.apply {
            systemUiVisibility = View.SYSTEM_UI_FLAG_IMMERSIVE or View.SYSTEM_UI_FLAG_FULLSCREEN or SYSTEM_UI_FLAG_HIDE_NAVIGATION
        }

        setContentView(R.layout.main_activity)
        supportActionBar?.setDisplayHomeAsUpEnabled(false)

    }

    override fun onWindowFocusChanged(hasFocus: Boolean) {
        super.onWindowFocusChanged(hasFocus)
        if (hasFocus) {
            hideSystemUI()
        }
    }

    private fun hideSystemUI() {
        val decorView = window.decorView
        decorView.systemUiVisibility = (View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                or View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                or View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                or SYSTEM_UI_FLAG_HIDE_NAVIGATION
                or View.SYSTEM_UI_FLAG_FULLSCREEN)
    }

    override fun onSupportNavigateUp(): Boolean {
        return navController.navigateUp(AppBarConfiguration(navController.graph)) || super.onSupportNavigateUp()
    }

    /*
        Si queremos pulsar el botón "atrás" del panel de navegación, este al pulsar 2 veces
        se finalizará la aplicación.
    */
    override fun onBackPressed() {

        if (timeFristClick + INTERVAL > System.currentTimeMillis()) {
            finish()
            return
        } else {
            Toast.makeText(this, getString(R.string.press_to_exit), Toast.LENGTH_SHORT).show()
        }
        timeFristClick = System.currentTimeMillis()
    }
}
