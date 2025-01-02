package ec.uti.edu.utifact.entity

data class Producto (
    val id: Int,
    val codigo: String,
    val nombre: String,
    val proveedor: String,
    val stock: Int,
    val precio: Double
)