package ec.uti.edu.utifact.database.databadeentity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "detallefactura")
data class DetFacturabd(
    @PrimaryKey val id: Int,
    val factura: Int,
    val producto: Int,
    val cantidad: Int,
    val precio: Double
)