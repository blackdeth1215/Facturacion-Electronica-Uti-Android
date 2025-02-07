package ec.uti.edu.utifact.database.databadeentity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "clientes")
data class Clientebd(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val nombresClient: String,
    val direccionClient: String,
    val correoClient: String,
    val cedulaClient: String,
    val telefonoClient: String
)
