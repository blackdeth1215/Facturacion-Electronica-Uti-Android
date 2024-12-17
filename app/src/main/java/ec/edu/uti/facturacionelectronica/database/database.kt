package ec.edu.uti.facturacionelectronica.database

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class database (context: Context): SQLiteOpenHelper(
    context, DATABASE_NAME, null, DATABASE_VERSION){

    companion object {
        private const val DATABASE_NAME = "facturacion.db"
        private const val DATABASE_VERSION = 1

        // Nombre de las tablas
        const val TABLE_USERS = "usuarios"
        const val TABLE_CLIENTES = "clientes"
        const val TABLE_EMISOR = "emisor"

        // Columnas de la tabla Usuarios
        const val COLUMN_USER_ID = "id_users"
        const val COLUMN_USER_NAME = "user"
        const val COLUMN_USER_PASSWORD = "password"
        const val COLUMN_USER_ROLE = "rol"

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
        const val COLUMN_EMISOR_ADDRESS = "direccionEmpre"
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
                $COLUMN_EMISOR_NAME NVARCHAR(50),
                $COLUMN_EMISOR_ADDRESS NVARCHAR(60),
                $COLUMN_EMISOR_PHONE NVARCHAR(50)
            )
        """
        db.execSQL(createTableEmisor)
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.execSQL("DROP TABLE IF EXISTS $TABLE_USERS")
        db.execSQL("DROP TABLE IF EXISTS $TABLE_CLIENTES")
        db.execSQL("DROP TABLE IF EXISTS $TABLE_EMISOR")
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


    fun insertEmisor(nombresEmpre: String, direccionEmpre: String, telefonoEmpre: String): Long {
        val db = writableDatabase
        val values = ContentValues().apply {
            put(COLUMN_EMISOR_NAME, nombresEmpre)
            put(COLUMN_EMISOR_ADDRESS, direccionEmpre)
            put(COLUMN_EMISOR_PHONE, telefonoEmpre)
        }
        return db.insert(TABLE_EMISOR, null, values)
    }
    fun insertEmisorIfNotExists(nombresEmpre: String, direccionEmpre: String, telefonoEmpre: String): Long {
        if (!isEmisorExists(nombresEmpre, telefonoEmpre)) {
            return insertEmisor(nombresEmpre, direccionEmpre, telefonoEmpre)
        }
        return -1 // Retorna -1 si ya existe
    }


    // Método para validar credenciales de usuario
    fun validateUsuario(user: String, password: String): Boolean {
        val db = readableDatabase
        val query = "SELECT * FROM $TABLE_USERS WHERE $COLUMN_USER_NAME = ? AND $COLUMN_USER_PASSWORD = ?"
        val cursor = db.rawQuery(query, arrayOf(user, password))
        val isValid = cursor.count > 0
        cursor.close()
        return isValid
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

}