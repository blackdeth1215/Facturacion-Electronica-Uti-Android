package ec.edu.uti.facturacionelectronica.ui

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import ec.edu.uti.facturacionelectronica.R
import ec.edu.uti.facturacionelectronica.database.database

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
            "0987654321"
        )
        if (emisorId != -1L) {
            println("Emisor insertado con ID: $emisorId")
        } else {
            println("El emisor ya existe.")
        }
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }

    fun onLogin(view: View) {
        var username = findViewById<EditText>(R.id.extUser).text.toString()
        var password = findViewById<EditText>(R.id.extPass).text.toString()
        var warnings = findViewById<TextView>(R.id.txtwarnin).text.toString()
        if (username.isNotEmpty() || password.isNotEmpty()) {
            val isValid = dbHelper.validateUsuario(username, password)
            if(isValid){
                warnings = "Datos Correctos"
                findViewById<TextView>(R.id.txtwarnin).setTextColor(ContextCompat.getColor(this, R.color.green))
                findViewById<TextView>(R.id.txtwarnin).setText(warnings)
                dbHelper.saveLoginState(this, true) // Guardar estado de login
                val inicio = Intent(this, AdminActivity::class.java)
                startActivity(inicio)
                finish()
            }else{
                warnings = "Datos incorrectos"
                findViewById<TextView>(R.id.txtwarnin).setTextColor(ContextCompat.getColor(this, R.color.yellow))
                findViewById<TextView>(R.id.txtwarnin).setText(warnings)
            }
        }else{
            warnings = "Ingrese sus credenciales correctamente"
            findViewById<TextView>(R.id.txtwarnin).setTextColor(ContextCompat.getColor(this, R.color.orange))
            findViewById<TextView>(R.id.txtwarnin).setText(warnings)
        }
    }
}