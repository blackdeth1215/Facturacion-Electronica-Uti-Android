package ec.uti.edu.utifact.database.databadeentity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "productos")
data class Productobd(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val codeProduct: String,
    val nameProduct: String,
    val proveedor: String,
    val stock: Int,
    val precio: Double
)
