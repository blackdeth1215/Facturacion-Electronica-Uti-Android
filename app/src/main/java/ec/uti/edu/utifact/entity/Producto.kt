package ec.uti.edu.utifact.entity

data class Producto (
    val id: Int,
    val nameProduct: String,
    val precioUnitario: Double,
    var stock: Int,
    var cantidad: Int = 1
)