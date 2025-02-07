package ec.uti.edu.utifact.ui

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.text.SpannableString
import android.text.style.ForegroundColorSpan
import android.view.Menu
import android.view.MenuItem
import android.widget.EditText
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.content.ContextCompat
import androidx.core.view.GravityCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.navigation.ui.AppBarConfiguration
import androidx.drawerlayout.widget.DrawerLayout
import androidx.lifecycle.lifecycleScope
import androidx.navigation.findNavController
import androidx.navigation.ui.navigateUp
import com.google.android.material.navigation.NavigationView
import ec.edu.uti.facturacionelectronica.fragments.FragmentFirst
import ec.edu.uti.facturacionelectronica.fragments.FragmentInformation
import ec.uti.edu.utifact.R
import ec.uti.edu.utifact.database.AppDatabase
import ec.uti.edu.utifact.databasebd
import ec.uti.edu.utifact.fragments.FragmentClient
import ec.uti.edu.utifact.fragments.FragmentEdAcCliente
import ec.uti.edu.utifact.fragments.FragmentEdAcProducto
import ec.uti.edu.utifact.fragments.FragmentEdAcUser
import ec.uti.edu.utifact.fragments.FragmentEmisor
import ec.uti.edu.utifact.fragments.FragmentFacturar
import ec.uti.edu.utifact.fragments.FragmentProducto
import ec.uti.edu.utifact.fragments.FragmentReport
import ec.uti.edu.utifact.fragments.FragmentUser
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class AdminActivity : AppCompatActivity() {
    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var drawerLayout: DrawerLayout
    private var fragmentTitle: String = ""
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_admin)


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
                    supportFragmentManager.beginTransaction()
                        .replace(R.id.fragment_container, FragmentInformation())
                        .addToBackStack(null)
                        .commit()
                }
                R.id.nav_usuarios->{
                    supportFragmentManager.beginTransaction()
                        .replace(R.id.fragment_container, FragmentUser())
                        .addToBackStack(null)
                        .commit()
                }
                R.id.nav_reportes -> {
                    supportFragmentManager.beginTransaction()
                        .replace(R.id.fragment_container, FragmentReport())
                        .addToBackStack(null)
                        .commit()
                }
                R.id.nav_productos ->{
                    supportFragmentManager.beginTransaction()
                        .replace(R.id.fragment_container, FragmentProducto())
                        .addToBackStack(null)
                        .commit()
                }
                R.id.nav_clientes -> {
                    supportFragmentManager.beginTransaction()
                        .replace(R.id.fragment_container, FragmentClient())
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
                R.id.nav_emisor ->{
                    supportFragmentManager.beginTransaction()
                        .replace(R.id.fragment_container, FragmentEmisor())
                        .addToBackStack(null)
                        .commit()
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

    override fun onPrepareOptionsMenu(menu: Menu): Boolean {
        val addItem = menu.findItem(R.id.action_add)

        for (i in 0 until menu.size()) {
            val menuItem = menu.getItem(i)
            val spannableTitle = SpannableString(menuItem.title)
            spannableTitle.setSpan(ForegroundColorSpan(Color.WHITE), 0, spannableTitle.length, 0)
            menuItem.title = spannableTitle
        }
        // Cambiar el título del ítem de acuerdo al fragmento actual
//        addItem.title = when (fragmentTitle) {
//            "Productos" -> "Agregar Producto"
//            "Clientes" -> "Agregar Cliente"
//            "Usuarios" -> "Agregar Usuario"
//            else -> "Agregar"
//        }
        when (fragmentTitle) {
            "Productos" -> {
                addItem.title = null // Elimina el texto
                addItem.setIcon(R.drawable.baseline_add_24) // Asigna el ícono para "Productos"
            }
            "Clientes" -> {
                addItem.title = null // Elimina el texto
                addItem.setIcon(R.drawable.baseline_add_24) // Asigna el ícono para "Clientes"
            }
            "Usuarios" -> {
                addItem.title = null // Elimina el texto
                addItem.setIcon(R.drawable.baseline_add_24) // Asigna el ícono para "Usuarios"
            }
            else -> {
                addItem.title = null
                addItem.setIcon(null) // Remueve el ícono si no es relevante
            }
        }

        return super.onPrepareOptionsMenu(menu)
    }
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_add -> {
                when (fragmentTitle) {
                    "Productos" -> {
                        // Acción para agregar producto
                        supportFragmentManager.beginTransaction()
                            .replace(R.id.fragment_container, FragmentEdAcProducto())
                            .addToBackStack(null)
                            .commit()
                    }
                    "Clientes" -> {
                        // Acción para agregar cliente
                        supportFragmentManager.beginTransaction()
                            .replace(R.id.fragment_container, FragmentEdAcCliente())
                            .addToBackStack(null)
                            .commit()
                    }
                    "Usuarios" -> {
                        // Acción para agregar usuario
                        supportFragmentManager.beginTransaction()
                            .replace(R.id.fragment_container, FragmentEdAcUser())
                            .addToBackStack(null)
                            .commit()
                    }
                    "Reportes de facturas"->{
                        // Acción para cambiar de facturas a usuarios
                        supportFragmentManager.beginTransaction()
                            .replace(R.id.fragment_container, FragmentEdAcUser())
                            .addToBackStack(null)
                            .commit()
                    }
                    "Reportes de Usuarios"->{
                        // Acción para cambiar de usuarios a facturas
                        supportFragmentManager.beginTransaction()
                            .replace(R.id.fragment_container, FragmentEdAcUser())
                            .addToBackStack(null)
                            .commit()
                    }
                    else -> {
                        Toast.makeText(this, "Agregar acción no definida", Toast.LENGTH_SHORT).show()
                    }
                }
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    fun setFragmentTitle(title: String) {
        supportActionBar?.title = title
        fragmentTitle = title
        invalidateOptionsMenu() // Refresca el menú
    }
}