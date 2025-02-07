package ec.uti.edu.utifact.ui

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
import ec.uti.edu.utifact.R
import ec.uti.edu.utifact.database.AppDatabase
import ec.uti.edu.utifact.database.databadeentity.Clientebd
import ec.uti.edu.utifact.database.databadeentity.Emisorbd
import ec.uti.edu.utifact.database.databadeentity.Productobd
import ec.uti.edu.utifact.database.databadeentity.Userbd
import ec.uti.edu.utifact.databasebd
import ec.uti.edu.utifact.entity.User
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.math.BigInteger
import java.security.MessageDigest
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale

class LoginActivity : AppCompatActivity() {
    val dbHelper = databasebd(this)
    private lateinit var db: AppDatabase
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_login)


        // Insertar un usuario
        /*val userId = dbHelper.insertUsuarioIfNotExists("admin", "admin123", 1)
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
        }*/
        // Inicializar Room
        db = AppDatabase.getDatabase(this)

        // Insertar datos iniciales en una corrutina
        CoroutineScope(Dispatchers.IO).launch {
            if(db.userDao().findByUser("admin") == null){
                val password = "admin123".toMD5()
                println("password: $password")
                if (db.userDao().insert(Userbd(user = "admin", password = password, fechaCreacion =obtenerFechaActual(), fechaCaducidad = obtenerFechaMas() , rol = 1)) != -1L) {
                    println("Usuario admin insertado.")
                }
            } else {
                println("El usuario admin ya existe.")
            }
            if(db.userDao().findByUser("user") == null){
                val password = "user".toMD5()
                println("password: $password")
                if (db.userDao().insert(Userbd(user = "user", password = password,fechaCreacion =obtenerFechaActual(), fechaCaducidad = obtenerFechaMas() , rol = 2)) != -1L) {
                    println("Usuario user insertado.")
                }
            } else {
                println("El usuario user ya existe.")
            }

            if(db.emisorDao().findById(1) == null){

                if (db.emisorDao().insert(Emisorbd(
                        razonSocial="Mi Empresa",
                        direccion="Av. Principal",
                        telefono="0987654321",
                        correo="empresa@ejemplo.com",
                        ruc= "1234567890",
                        sucursal="1",
                        puntoEmision="001"
                    )) != -1L) {
                    println("emisor emisorbd insertado.")
                }
            } else {
                println("El EMISOR emisorbd ya existe.")
            }

            if(db.clienteDao().findByCedula("1234567890")==null){
                if (db.clienteDao().insert(Clientebd(
                        nombresClient="Juan Perez",
                        direccionClient="Calle 123",
                        correoClient="juan.perez@example.com",
                        cedulaClient="1234567890",
                        telefonoClient="0999999999")) != -1L) {
                    println("Cliente clientebd insertado.")
                }
            }else {
                println("El clientebd ya existe.")
            }

            val producto = db.productoDao().findByCode("producto1")
            if (producto==null) { // Si no hay productos con el código "producto1"
                for (i in 1..10) {
                    val result = db.productoDao().insert(
                        Productobd(
                            codeProduct = "producto$i",
                            nameProduct = "Prueba Producto $i",
                            proveedor = "provedor${i}p",
                            stock = 50,
                            precio = 20.05
                        )
                    )
                    if (result != -1L) {
                        println("Producto producto$i insertado.")
                    } else {
                        println("Error al insertar producto producto$i.")
                    }
                }
            } else {
                println("El productobd user ya existe: $producto")
            }

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
        val password = findViewById<EditText>(R.id.extPass).text.toString().toMD5()
        val warningsView = findViewById<TextView>(R.id.txtwarnin)

        if (username.isNotEmpty() && password.isNotEmpty()) {

            CoroutineScope(Dispatchers.IO).launch {
                val user = db.userDao().validateUsuario(username, password)
                withContext(Dispatchers.Main) {
                    if (user != null) {
                        warningsView.text = "Datos Correctos"
                        warningsView.setTextColor(ContextCompat.getColor(this@LoginActivity, R.color.green))

                        val intent = when (user.rol) {
                            1 -> {
                                println("id del usuario: ${user.id}")
                                val intent = Intent(this@LoginActivity, AdminActivity::class.java)
                                startActivity(intent)
                            }
                            2 -> {
                                println("id del usuario: ${user.id}")
                                val intent = Intent(this@LoginActivity, UserActivity::class.java)
                                startActivity(intent)
                            }else ->{
                                Toast.makeText(this@LoginActivity, "Rol no válido", Toast.LENGTH_SHORT).show()
                            }
                        }
                        finish()
                    } else {
                        warningsView.text = "Datos incorrectos"
                        warningsView.setTextColor(ContextCompat.getColor(this@LoginActivity, R.color.blue))
                    }
                }
            }
        } else {
            warningsView.text = "Ingrese sus credenciales correctamente"
            warningsView.setTextColor(ContextCompat.getColor(this, R.color.orange))

        }
    }
    fun obtenerFechaActual(): String {
        val formato = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
        return formato.format(Date()) // Obtiene la fecha actual
    }
    fun obtenerFechaMas(): String {
        val formato = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()) // Formato de fecha
        val calendario = Calendar.getInstance() // Obtiene la fecha actual
        calendario.add(Calendar.YEAR, 5) // Suma 10 años a la fecha actual
        return formato.format(calendario.time) // Devuelve la fecha como String
    }

    fun String.toMD5(): String {
        val md = MessageDigest.getInstance("MD5")
        val digest = md.digest(this.toByteArray())
        val bigInt = BigInteger(1, digest)
        return bigInt.toString(16).padStart(32, '0')
    }
}