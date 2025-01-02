package ec.uti.edu.utifact

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import ec.uti.edu.utifact.entity.Cliente
import ec.uti.edu.utifact.entity.Producto
import ec.uti.edu.utifact.entity.User
import ec.uti.edu.utifact.ui.LoginActivity

public class database (context: Context): SQLiteOpenHelper(
    context, DATABASE_NAME, null, DATABASE_VERSION){

    companion object {
        private const val DATABASE_NAME = "facturacion.db"
        private const val DATABASE_VERSION = 1

        // Nombre de las tablas
        const val TABLE_USERS = "usuarios"
        const val TABLE_CLIENTES = "clientes"
        const val TABLE_EMISOR = "emisor"
        const val TABLE_PRODUCT = "productos"
        const val TABLE_FACTURA = "factura"
        const val TABLE_DETALLE_FACTURA = "detalle_factura"

        // Columnas de la tabla Factura
        const val COLUMN_FACTURA_ID = "id_factura"
        const val COLUMN_FACTURA_FECHA = "fecha"
        const val COLUMN_FACTURA_CLIENTE = "cliente"
        const val COLUMN_FACTURA_TOTAL = "total"

        // Columnas de la tabla Detalle Factura
        const val COLUMN_DETFACT_ID = "id_detalle"
        const val COLUMN_DETFACT_FACTURA = "id_factura"
        const val COLUMN_DETFACT_PRODUCTO = "id_producto"
        const val COLUMN_DETFACT_CANTIDAD = "cantidad"

        // Columnas de la tabla Usuarios
        const val COLUMN_USER_ID = "id_users"
        const val COLUMN_USER_NAME = "user"
        const val COLUMN_USER_PASSWORD = "password"
        const val COLUMN_USER_ROLE = "rol"

        // Columnas de la tabla Productos
        const val COLUMN_PRODUCT_ID = "id_product"
        const val COLUMN_PRODUCT_CODE = "codeProduct"
        const val COLUMN_PRODUCT_NAME = "nameProduct"
        const val COLUMN_PRODUCT_PROVE = "proveedor"
        const val COLUMN_PRODUCT_STOCK = "stock"
        const val COLUMN_PRODUCT_PRECIO = "precio1"

        // Columnas de la tabla Clientes
        const val COLUMN_CLIENT_ID = "id_client"
        const val COLUMN_CLIENT_NAME = "nombresClient"
        const val COLUMN_CLIENT_ADDRESS = "direccionClient"
        const val COLUMN_CLIENT_EMAIL = "correoClient"
        const val COLUMN_CLIENT_CEDULA = "cedulaClient"
        const val COLUMN_CLIENT_PHONE = "telefonoClient"

        // Columnas de la tabla Emisor
        const val COLUMN_EMISOR_ID = "id_emisor"
        const val COLUMN_EMISOR_NAME = "nombresEmpre"
        const val COLUMN_EMISOR_RUC = "rucEmpre"
        const val COLUMN_EMISOR_ADDRESS = "direccionEmpre"
        const val COLUMN_EMISOR_EMAIL = "emailEmpre"
        const val COLUMN_EMISOR_PHONE = "TelefonoEmpre"
    }

    override fun onCreate(db: SQLiteDatabase) {
        // Crear tabla Usuarios
        val createTableUsuarios = """
            CREATE TABLE $TABLE_USERS (
                $COLUMN_USER_ID INTEGER PRIMARY KEY AUTOINCREMENT,
                $COLUMN_USER_NAME NVARCHAR(50),
                $COLUMN_USER_PASSWORD NVARCHAR(50),
                $COLUMN_USER_ROLE INTEGER
            )
        """
        db.execSQL(createTableUsuarios)

        // Crear tabla Facturar
        val createTableFacturar = """
            CREATE TABLE $TABLE_FACTURA (
                $COLUMN_FACTURA_ID INTEGER PRIMARY KEY AUTOINCREMENT,
                $COLUMN_FACTURA_FECHA DATETIME DEFAULT CURRENT_TIMESTAMP,
                $COLUMN_FACTURA_CLIENTE NVARCHAR(50),
                $COLUMN_FACTURA_TOTAL DECIMAL(10,2)
            )
        """
        db.execSQL(createTableFacturar)


        // Crear tabla Detalle Factura
        val createTableFacturaDetalle = """
            CREATE TABLE $TABLE_DETALLE_FACTURA (
                $COLUMN_DETFACT_ID INTEGER PRIMARY KEY AUTOINCREMENT,
                $COLUMN_DETFACT_FACTURA INTEGER,
                $COLUMN_DETFACT_PRODUCTO NVARCHAR(65),
                $COLUMN_DETFACT_CANTIDAD NVARCHAR(150),
                $COLUMN_CLIENT_CEDULA NVARCHAR(13),
                $COLUMN_CLIENT_PHONE NVARCHAR(10)
            )
        """
        db.execSQL(createTableFacturaDetalle)

        // Crear tabla Clientes
        val createTableClientes = """
            CREATE TABLE $TABLE_CLIENTES (
                $COLUMN_CLIENT_ID INTEGER PRIMARY KEY AUTOINCREMENT,
                $COLUMN_CLIENT_NAME VARCHAR(60),
                $COLUMN_CLIENT_ADDRESS NVARCHAR(65),
                $COLUMN_CLIENT_EMAIL NVARCHAR(150),
                $COLUMN_CLIENT_CEDULA NVARCHAR(13),
                $COLUMN_CLIENT_PHONE NVARCHAR(10)
            )
        """
        db.execSQL(createTableClientes)

        // Crear tabla Emisor
        val createTableEmisor = """
            CREATE TABLE $TABLE_EMISOR (
                $COLUMN_EMISOR_ID INTEGER PRIMARY KEY AUTOINCREMENT,
                $COLUMN_EMISOR_RUC NVARCHAR(13),
                $COLUMN_EMISOR_NAME NVARCHAR(50),
                $COLUMN_EMISOR_ADDRESS NVARCHAR(60),
                $COLUMN_EMISOR_PHONE NVARCHAR(50),
                $COLUMN_EMISOR_EMAIL NVARCHAR(150),
                $COLUMN_EMISOR_PHONE NVARCHAR(10)
            )
        """
        db.execSQL(createTableEmisor)
        // Crear tabla Productos
        val createTableProductos = """
            CREATE TABLE $TABLE_PRODUCT (
                $COLUMN_PRODUCT_ID INTEGER PRIMARY KEY AUTOINCREMENT,
                $COLUMN_PRODUCT_CODE NVARCHAR(50),
                $COLUMN_PRODUCT_NAME NVARCHAR(60),
                $COLUMN_PRODUCT_PROVE NVARCHAR(50),
                $COLUMN_PRODUCT_STOCK INTEGER,
                $COLUMN_PRODUCT_PRECIO DECIMAL(10,2)
            )
        """
        db.execSQL(createTableProductos)
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.execSQL("DROP TABLE IF EXISTS $TABLE_USERS")
        db.execSQL("DROP TABLE IF EXISTS $TABLE_CLIENTES")
        db.execSQL("DROP TABLE IF EXISTS $TABLE_EMISOR")
        db.execSQL("DROP TABLE IF EXISTS $TABLE_PRODUCT")
        db.execSQL("DROP TABLE IF EXISTS $TABLE_FACTURA")
        db.execSQL("DROP TABLE IF EXISTS $TABLE_DETALLE_FACTURA")
        onCreate(db)
    }

    // Métodos para insertar datos en las tablas

    fun insertUsuario(user: String, password: String, rol: Int): Long {
        val db = writableDatabase
        val values = ContentValues().apply {
            put(COLUMN_USER_NAME, user)
            put(COLUMN_USER_PASSWORD, password)
            put(COLUMN_USER_ROLE, rol)
        }
        return db.insert(TABLE_USERS, null, values)
    }
    fun insertUsuarioIfNotExists(user: String, password: String, rol: Int): Long {
        if (!isUsuarioExists(user)) {
            return insertUsuario(user, password, rol)
        }
        return -1 // Retorna -1 si ya existe
    }

    fun insertProducto(codeProd: String, nameProd: String, provedProd: String, stock: Int, precio: Double): Long {
        val db = writableDatabase
        val values = ContentValues().apply {
            put(COLUMN_PRODUCT_CODE, codeProd)
            put(COLUMN_PRODUCT_NAME, nameProd)
            put(COLUMN_PRODUCT_PROVE, provedProd)
            put(COLUMN_PRODUCT_STOCK, stock)
            put(COLUMN_PRODUCT_PRECIO, precio)
        }
        return db.insert(TABLE_PRODUCT, null, values)
    }
    fun insertProductofNotExists(codeProd: String, nameProd: String, provedProd: String,stock: Int, precio: Double): Long {
        if (!isProductExists(codeProd)) {
            return insertProducto(codeProd, nameProd, provedProd, stock, precio)
        }
        return -1 // Retorna -1 si ya existe
    }


    fun insertCliente(nombres: String, direccion: String, correo: String, cedula: String, telefono: String): Long {
        val db = writableDatabase
        val values = ContentValues().apply {
            put(COLUMN_CLIENT_NAME, nombres)
            put(COLUMN_CLIENT_ADDRESS, direccion)
            put(COLUMN_CLIENT_EMAIL, correo)
            put(COLUMN_CLIENT_CEDULA, cedula)
            put(COLUMN_CLIENT_PHONE, telefono)
        }
        return db.insert(TABLE_CLIENTES, null, values)
    }
    fun insertClienteIfNotExists(nombres: String, direccion: String, correo: String, cedula: String, telefono: String): Long {
        if (!isClienteExists(correo, cedula)) {
            return insertCliente(nombres, direccion, correo, cedula, telefono)
        }
        return -1 // Retorna -1 si ya existe
    }


    fun insertEmisor(nombresEmpre: String, direccionEmpre: String, telefonoEmpre: String, emailEmpre: String,rucEmpre: String): Long {
        val db = writableDatabase
        val values = ContentValues().apply {
            put(COLUMN_EMISOR_NAME, nombresEmpre)
            put(COLUMN_EMISOR_ADDRESS, direccionEmpre)
            put(COLUMN_EMISOR_PHONE, telefonoEmpre)
            put(COLUMN_EMISOR_EMAIL, emailEmpre)
            put(COLUMN_EMISOR_RUC, rucEmpre)
        }
        return db.insert(TABLE_EMISOR, null, values)
    }
    fun insertEmisorIfNotExists(nombresEmpre: String, direccionEmpre: String, telefonoEmpre: String, emailEmpre: String,rucEmpre: String): Long {
        if (!isEmisorExists(nombresEmpre, telefonoEmpre)) {
            return insertEmisor(nombresEmpre, direccionEmpre, telefonoEmpre, emailEmpre,rucEmpre)
        }
        return -1 // Retorna -1 si ya existe
    }


    // Método para validar credenciales de usuario
    fun validateUsuario(user: String, password: String): Array<String>? {
        val db = readableDatabase
        val query = "SELECT * FROM $TABLE_USERS WHERE $COLUMN_USER_NAME = ? AND $COLUMN_USER_PASSWORD = ?"
        val cursor = db.rawQuery(query, arrayOf(user, password))

        var userData: Array<String>? = null

        if (cursor.moveToFirst()) {
            // Crear un array para almacenar los datos de las columnas
            userData = Array(cursor.columnCount) { index ->
                cursor.getString(index) // Obtiene el valor de cada columna
            }
        }

        cursor.close() // Liberar el cursor
        db.close() // Cerrar la conexión a la base de datos

        return userData // Devuelve los datos del usuario o null si no se encontró
    }
    fun saveSession(context: Context, userData: Array<String>) {
        val sharedPreferences = context.getSharedPreferences("user_session", Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()

        // Guardar los datos del usuario
        for (i in userData.indices) {
            editor.putString("data_$i", userData[i]) // Guarda cada campo con una clave única
        }

        editor.putBoolean("isLoggedIn", true) // Estado de sesión activa
        editor.apply()
    }
    fun getSession(context: Context): Array<String>? {
        val sharedPreferences = context.getSharedPreferences("user_session", Context.MODE_PRIVATE)

        if (sharedPreferences.getBoolean("isLoggedIn", false)) {
            val dataList = mutableListOf<String>()
            var index = 0
            while (sharedPreferences.contains("data_$index")) {
                dataList.add(sharedPreferences.getString("data_$index", "") ?: "")
                index++
            }
            return dataList.toTypedArray()
        }

        return null // No hay sesión activa
    }
    fun saveLoginState(context: Context, isLoggedIn: Boolean, userData: Array<String>? = null) {
        val sharedPref = context.getSharedPreferences("login_prefs", Context.MODE_PRIVATE)
        val editor = sharedPref.edit()

        // Guardar estado de login
        editor.putBoolean("is_logged_in", isLoggedIn)

        // Si el usuario se está logueando, guardar los datos
        if (isLoggedIn && userData != null) {
            for (i in userData.indices) {
                editor.putString("user_data_$i", userData[i])
            }
        }

        editor.apply()
    }
    fun getLoggedUserData(context: Context): Array<String>? {
        val sharedPref = context.getSharedPreferences("login_prefs", Context.MODE_PRIVATE)
        if (sharedPref.getBoolean("is_logged_in", false)) {
            val userData = mutableListOf<String>()
            var index = 0
            while (sharedPref.contains("user_data_$index")) {
                userData.add(sharedPref.getString("user_data_$index", "") ?: "")
                index++
            }
            return userData.toTypedArray()
        }
        return null
    }
    fun checkLoginState(context: Context) {
        val sharedPref = context.getSharedPreferences("login_prefs", Context.MODE_PRIVATE)
        val isLoggedIn = sharedPref.getBoolean("is_logged_in", false)
        if (!isLoggedIn) {
            val intent = Intent(context, LoginActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            context.startActivity(intent)
        }
    }

    // Método para verificar si un usuario, cliente, emisor ya existe en la base de datos
    fun isUsuarioExists(user: String): Boolean {
        val db = readableDatabase
        val query = "SELECT * FROM $TABLE_USERS WHERE $COLUMN_USER_NAME = ?"
        val cursor = db.rawQuery(query, arrayOf(user))
        val exists = cursor.count > 0
        cursor.close()
        return exists
    }
    fun isProductExists(codeProd: String): Boolean {
        val db = readableDatabase
        val query = "SELECT * FROM $TABLE_PRODUCT WHERE $COLUMN_PRODUCT_CODE = ?"
        val cursor = db.rawQuery(query, arrayOf(codeProd))
        val exists = cursor.count > 0
        cursor.close()
        return exists
    }
    fun isClienteExists(correo: String, cedula: String): Boolean {
        val db = readableDatabase
        val query = "SELECT * FROM $TABLE_CLIENTES WHERE $COLUMN_CLIENT_EMAIL = ? OR $COLUMN_CLIENT_CEDULA = ?"
        val cursor = db.rawQuery(query, arrayOf(correo, cedula))
        val exists = cursor.count > 0
        cursor.close()
        return exists
    }

    fun isEmisorExists(nombre: String, telefono: String): Boolean {
        val db = readableDatabase
        val query = "SELECT * FROM $TABLE_EMISOR WHERE $COLUMN_EMISOR_NAME = ? OR $COLUMN_EMISOR_PHONE = ?"
        val cursor = db.rawQuery(query, arrayOf(nombre, telefono))
        val exists = cursor.count > 0
        cursor.close()
        return exists
    }

    //guardar estado
    fun saveLoginState(context: Context, isLoggedIn: Boolean) {
        val sharedPref = context.getSharedPreferences("login_prefs", Context.MODE_PRIVATE)
        val editor = sharedPref.edit()
        editor.putBoolean("is_logged_in", isLoggedIn)
        editor.apply()
    }
    fun logout(context: Context) {
        val sharedPref = context.getSharedPreferences("login_prefs", Context.MODE_PRIVATE)
        val editor = sharedPref.edit()
        editor.putBoolean("is_logged_in", false)
        editor.apply()
    }
    //busqueda de clientes en clientes.kt data
    fun getClientes(): List<Cliente> {
        val clientes = mutableListOf<Cliente>()
        val db = this.readableDatabase
        val cursor = db.rawQuery("SELECT * FROM clientes where cedulaClient like '%%'", null)

        if (cursor.moveToFirst()) {
            do {
                val id = cursor.getInt(cursor.getColumnIndexOrThrow("id_client"))
                val nombre = cursor.getString(cursor.getColumnIndexOrThrow("nombresClient"))
                val direccion= cursor.getString(cursor.getColumnIndexOrThrow("direccionClient"))
                val correo= cursor.getString(3)
                val cedula= cursor.getString(4)
                val telefono= cursor.getString(5)
                clientes.add(Cliente(id, nombre, direccion, correo, cedula, telefono))
            } while (cursor.moveToNext())
        }

        println("Cliente agregado a cliente.kt: $clientes")
        cursor.close()
        db.close()
        return clientes
    }
    fun getProductos(): List<Producto> {
        val productos = mutableListOf<Producto>()
        val db = this.readableDatabase
        val cursor = db.rawQuery("SELECT * FROM productos where codeProduct like '%%'", null)

        if (cursor.moveToFirst()) {
            do {
                val id = cursor.getInt(0)
                val codeProd = cursor.getString(1)
                val nameProd= cursor.getString(2)
                val provedProd= cursor.getString(3)
                val stock= cursor.getInt(4)
                val precio= cursor.getDouble(5)
                productos.add(Producto(id, codeProd, nameProd, provedProd, stock, precio))
            } while (cursor.moveToNext())
        }

        println("Producto agregado a producto.kt: $productos")
        cursor.close()
        db.close()
        return productos
    }
    fun getUsuarios(): List<User> {
        val usuarios = mutableListOf<User>()
        val db = this.readableDatabase
        val cursor = db.rawQuery("SELECT * FROM usuarios", null)

        if (cursor.moveToFirst()) {
            println("Número de registros en la tabla usuarios: ${cursor.count}")
            do {
                val id = cursor.getInt(0)
                val codeProd = cursor.getString(1)
                val nameProd= cursor.getString(2)
                val provedProd= cursor.getInt(3)
                usuarios.add(User(id, codeProd, nameProd, provedProd))
                println("Usuario en base de datos: ${cursor.getInt(0)}, ${cursor.getString(1)}, ${cursor.getString(2)}")
            } while (cursor.moveToNext())
        }
        cursor.close()
        db.close()
        return usuarios
    }

}