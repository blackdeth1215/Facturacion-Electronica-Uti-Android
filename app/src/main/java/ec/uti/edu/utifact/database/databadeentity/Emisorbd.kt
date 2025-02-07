package ec.uti.edu.utifact.database.databadeentity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "emisor")
data class Emisorbd (
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val ruc: String,
    val razonSocial: String,
    val direccion: String,
    val telefono: String,
    val correo: String,
    val sucursal: String,
    val puntoEmision: String
)