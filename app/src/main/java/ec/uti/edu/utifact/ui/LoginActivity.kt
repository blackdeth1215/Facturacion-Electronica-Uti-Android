package ec.uti.edu.utifact.ui

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.EditText
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import ec.uti.edu.utifact.R
import ec.uti.edu.utifact.database

class LoginActivity : AppCompatActivity() {
    val dbHelper = database(this)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_login)


        // Insertar un usuario
        val userId = dbHelper.insertUsuarioIfNotExists("admin", "admin123", 1)
        if (userId != -1L) {
            println("Usuario insertado con ID: $userId")
        } else {
            println("El usuario ya existe.")
        }
        val userId1 = dbHelper.insertUsuarioIfNotExists("user", "user", 2)
        if (userId1 != -1L) {
            println("Usuario insertado con ID: $userId")
        } else {
            println("El usuario ya existe.")
        }

        // Insertar un cliente
        val clientId = dbHelper.insertClienteIfNotExists(
            "Juan Perez",
            "Calle 123",
            "juan.perez@example.com",
            "1234567890",
            "0999999999"
        )
        if (clientId != -1L) {
            println("Cliente insertado con ID: $clientId")
        } else {
            println("El cliente ya existe.")
        }

        // Insertar un emisor
        val emisorId = dbHelper.insertEmisorIfNotExists(
            "Mi Empresa",
            "Av. Principal",
            "0987654321",
            "empresa@ejemplo.com",
            "1234567890"
        )
        if (emisorId != -1L) {
            println("Emisor insertado con ID: $emisorId")
        } else {
            println("El emisor ya existe.")
        }
        // Insertar un PRODUCTO
        val productId = dbHelper.insertProductofNotExists(
            "producto1",
            "Prueba Producto 1",
            "0987654321",
            50,
            20.05
        )
        if (productId != -1L) {
            println("Producto insertado con ID: $productId")
        } else {
            println("El Producto ya existe.")
        }

        //vista general
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }

    fun onLogin(view: View) {
        val username = findViewById<EditText>(R.id.extUser).text.toString()
        val password = findViewById<EditText>(R.id.extPass).text.toString()
        val warningsView = findViewById<TextView>(R.id.txtwarnin)

        if (username.isNotEmpty() && password.isNotEmpty()) {
            val userData = dbHelper.validateUsuario(username, password)
            if (userData != null) {
                warningsView.text = "Datos Correctos"
                warningsView.setTextColor(ContextCompat.getColor(this, R.color.green))

                // Guardar el estado de login y los datos del usuario
                dbHelper.saveLoginState(this, true, userData)

                val userRole = userData[3]
                println("id del usuario: ${userData[0]}")
                val intent = when (userRole) {
                    "1" ->{
                        Intent(this, AdminActivity::class.java) // Redirigir al administrador
                    }
                    "2" -> {
                        Intent(this, UserActivity::class.java) // Redirigir al usuario
                    }
                    else -> {
                        warningsView.text = "Rol no válido"
                        warningsView.setTextColor(ContextCompat.getColor(this, R.color.red))
                        return
                    }
                }
                startActivity(intent)
                finish()
            } else {
                warningsView.text = "Datos incorrectos"
                warningsView.setTextColor(ContextCompat.getColor(this, R.color.yellow))
            }
        } else {
            warningsView.text = "Ingrese sus credenciales correctamente"
            warningsView.setTextColor(ContextCompat.getColor(this, R.color.orange))
        }
    }
}