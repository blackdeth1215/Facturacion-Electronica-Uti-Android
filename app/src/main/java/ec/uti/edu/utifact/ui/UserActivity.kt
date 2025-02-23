package ec.uti.edu.utifact.ui

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.content.ContextCompat
import androidx.core.view.GravityCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import com.google.android.material.navigation.NavigationView
import ec.edu.uti.facturacionelectronica.fragments.FragmentFirst
import ec.edu.uti.facturacionelectronica.fragments.FragmentInformation
import ec.uti.edu.utifact.R
import ec.uti.edu.utifact.databasebd
import ec.uti.edu.utifact.fragments.FragmentClient
import ec.uti.edu.utifact.fragments.FragmentFacturar
import ec.uti.edu.utifact.fragments.FragmentProducto
import ec.uti.edu.utifact.fragments.FragmentReport

class UserActivity : AppCompatActivity() {
    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var drawerLayout: DrawerLayout
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_users)


        val toolbar = findViewById<Toolbar>(R.id.toolbar) // Toolbar
        setSupportActionBar(toolbar)

        drawerLayout = findViewById(R.id.main)
        val navigationView: NavigationView = findViewById(R.id.navigation_view)

        // Agregar el icono de hamburguesa y sincronizarlo con el DrawerLayout
        val toggle = ActionBarDrawerToggle(
            this, drawerLayout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close
        )
        drawerLayout.addDrawerListener(toggle)
        toggle.syncState()

        // Cargar el primer fragmento al inicio
        supportFragmentManager.beginTransaction()
            .replace(R.id.fragment_container, FragmentFirst())
            .commit()

        // Manejo de clics en las opciones del menú
        navigationView.setNavigationItemSelectedListener { item ->
            when (item.itemId) {
                R.id.nav_informacion -> {
                    // Reemplazar el fragmento al hacer clic
                    supportFragmentManager.beginTransaction()
                        .replace(R.id.fragment_container, FragmentInformation())
                        .addToBackStack(null) // Opcional: permite volver atrás
                        .commit()
                }
                R.id.nav_reportes -> {
                    supportFragmentManager.beginTransaction()
                        .replace(R.id.fragment_container, FragmentReport())
                        .addToBackStack(null)
                        .commit()
                }
                R.id.nav_clientes -> {
                    supportFragmentManager.beginTransaction()
                        .replace(R.id.fragment_container, FragmentClient())
                        .addToBackStack(null)
                        .commit()
                }
                R.id.nav_productos -> {
                    supportFragmentManager.beginTransaction()
                        .replace(R.id.fragment_container, FragmentProducto())
                        .addToBackStack(null)
                        .commit()
                }
                R.id.nav_facturacion -> {
                    // Reemplazar el fragmento
                    val fragment = FragmentFacturar()
                    supportFragmentManager.beginTransaction()
                        .replace(R.id.fragment_container, fragment)
                        .addToBackStack(null)
                        .commit()

                    true
                }
                R.id.nav_iniciar_sesion -> {
                    val login = Intent(this, LoginActivity::class.java)
                    startActivity(login)
                }
            }
            drawerLayout.closeDrawer(GravityCompat.START)
            true
        }

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        //colores forzados
        navigationView.setBackgroundColor(ContextCompat.getColor(this, R.color.purple))
        toolbar.setBackgroundColor(ContextCompat.getColor(this, R.color.purple))
    }
    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }
    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }
    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(R.id.nav_host_fragment_content_main)
        return navController.navigateUp(appBarConfiguration)
                || super.onSupportNavigateUp()
    }
    override fun onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START)
        } else {
            super.onBackPressed()
        }
    }
}