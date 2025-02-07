package ec.uti.edu.utifact.database.databadeentity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "factura")
data class Facturabd(
    @PrimaryKey val id: Int,
    val numero: Int,
    val cliente: Int,
    val total: Double,
    val fecha: String
)