package ec.uti.edu.utifact.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.recyclerview.widget.RecyclerView
import ec.uti.edu.utifact.R
import ec.uti.edu.utifact.database.AppDatabase
import ec.uti.edu.utifact.database.databadeentity.DetFacturabd
import ec.uti.edu.utifact.database.databadeentity.Productobd
import ec.uti.edu.utifact.database.databaseDao.DetFacturaDao
import ec.uti.edu.utifact.entity.Producto
import kotlinx.coroutines.launch

class ProductoFacturaAdapter(
    productosBd: List<Productobd>,
    private val cantidad: Int,
    private val onDeleteClick: (Producto) -> Unit
) : RecyclerView.Adapter<ProductoFacturaAdapter.Producto1ViewHolder>() {

    // Lista interna de productos para manejar el adaptador
    private var productos: MutableList<Producto> = productosBd.map { it.toProducto() }.toMutableList()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Producto1ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_producto_factu, parent, false)
        return Producto1ViewHolder(view)
    }

    override fun onBindViewHolder(holder: Producto1ViewHolder, position: Int) {
        val producto = productos[position]
        holder.bind(producto)
        println("productos: $cantidad")
        holder.itemView.findViewById<ImageButton>(R.id.imgDeletePF).setOnClickListener {
            eliminarProducto(position)
        }
    }

    override fun getItemCount(): Int = productos.size

    // Función para eliminar un producto
    private fun eliminarProducto(position: Int) {
        if (position in 0 until productos.size) {
            productos.removeAt(position)
            notifyItemRemoved(position)
            notifyItemRangeChanged(position, productos.size)
        }
    }

    // Función para agregar o actualizar un producto
    fun agregarProducto(productoNuevo: Producto, cantidad: Int) {
        val index = productos.indexOfFirst { it.nameProduct == productoNuevo.nameProduct }

        if (index != -1) {
            // Producto ya existe, actualizar cantidad
            productos[index].cantidad += cantidad
            notifyItemChanged(index)
        } else {
            // Producto no existe, agregarlo a la lista con la cantidad especificada
            val productoConCantidad = productoNuevo.copy(cantidad = cantidad)
            productos.add(productoConCantidad)
            notifyItemInserted(productos.size - 1)
        }
    }

    // Funciones de conversión
    private fun Productobd.toProducto(): Producto {
        return Producto(
            id = this.id,
            nameProduct = this.nameProduct,
            precioUnitario = this.precio,
            stock = this.stock,
            cantidad = cantidad
        )
    }

    fun limpiarProductos() {
        productos.clear()
        notifyDataSetChanged()
    }

    fun getProductos(): List<Producto> {
        return productos
    }

    // ViewHolder para manejar cada ítem
    class Producto1ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val txtProducto: TextView = itemView.findViewById(R.id.txtProducto)
        private val txtStock: TextView = itemView.findViewById(R.id.txtStockF)
        private val txtPrecioUni: TextView = itemView.findViewById(R.id.txtPrecioUni)

        fun bind(producto: Producto) {
            txtProducto.text = producto.nameProduct
            txtStock.text = producto.cantidad.toString()
            txtPrecioUni.text = producto.precioUnitario.toString()
        }
    }

    // Método para guardar los productos en la base de datos
    fun guardarDetalleFactura(facturaId: Int, database: AppDatabase) {
        val detFacturaDao = database.detalleFacturaDao()

        productos.forEach { producto ->
            val detalle = DetFacturabd(
                id = 0, // Si la BD maneja autoincremento, este valor se ignora
                factura = facturaId,
                producto = producto.id,
                cantidad = producto.cantidad,
                precio = producto.precioUnitario
            )
            detFacturaDao.insert(detalle)
        }
    }
}