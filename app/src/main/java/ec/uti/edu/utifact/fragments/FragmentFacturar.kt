package ec.uti.edu.utifact.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.button.MaterialButton
import ec.uti.edu.utifact.R
import ec.uti.edu.utifact.adapter.ClienteAdapter
import ec.uti.edu.utifact.adapter.ProductoAdapter
import ec.uti.edu.utifact.adapter.ProductoFacturaAdapter
import ec.uti.edu.utifact.database.AppDatabase
import ec.uti.edu.utifact.database.databadeentity.Facturabd
import ec.uti.edu.utifact.database.databadeentity.Productobd
import ec.uti.edu.utifact.databasebd
import ec.uti.edu.utifact.entity.Producto
import ec.uti.edu.utifact.ui.AdminActivity
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class FragmentFacturar: Fragment() {
    private lateinit var recyclerView: RecyclerView
    private lateinit var productoFacturaAdapter: ProductoFacturaAdapter
    private lateinit var db : AppDatabase

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.activity_facturacion, container, false)
    }
    override fun onResume() {
        super.onResume()
        (activity as? AdminActivity)?.setFragmentTitle("Facturar")
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        // Inicializar el RecyclerView
        recyclerView = view.findViewById(R.id.rcvProdF)
        recyclerView.layoutManager = LinearLayoutManager(requireContext())

        // Obtener datos de la base de datos para cliente

        val btnBuscar = view.findViewById<Button>(R.id.btnBclF)
        btnBuscar.setOnClickListener { buscarCliente(view) }
        val btnProductobus = view.findViewById<Button>(R.id.btnProF)
        btnProductobus.setOnClickListener { buscarProducto(view) }
        val btnFacturar = view.findViewById<Button>(R.id.btnFacturar)
        btnFacturar.setOnClickListener { facturar(view) }
    }

    fun facturar(view: View) {
        // Obtener el ID del cliente desde el TextView
        val txtCedula = view.findViewById<TextView>(R.id.txtCedbusF)
        val cedulaCliente = txtCedula.text.toString().replace("Cedula: ", "").trim()

        // Verificar si se ha seleccionado un cliente
        if (cedulaCliente == "null none") {
            Toast.makeText(requireContext(), "Seleccione un cliente antes de facturar", Toast.LENGTH_SHORT).show()
            return
        }

        // Obtener el total con IVA desde el TextView
        val txtTotalConIva = view.findViewById<TextView>(R.id.txtTotalF)
        val totalConIva = txtTotalConIva.text.toString().replace("Total: ", "").trim().toDouble()

        // Obtener el cliente desde la base de datos
        val clienteid = db.clienteDao().findByCedula(cedulaCliente)
        if (clienteid != null) {
            val idCliente = clienteid.id

            // Obtener la fecha actual
            val fechaActual = obtenerFechaActual()

            // Obtener el DAO de factura
            val facturaDao = db.facturaDao()

            // Obtener el último ID de la factura
            val ultimoId = facturaDao.getLastId() ?: 0 // Si no hay facturas, comienza desde 0

            // Generar un nuevo ID único para la factura
            val nuevoId = ultimoId + 1

            //guardar los productos en detalle factura
            guardarFactura(nuevoId)
            // Crear la instancia de Facturabd
            val nuevaFactura = Facturabd(
                id = nuevoId,
                numero = nuevoId,
                cliente = idCliente,
                total = totalConIva,
                fecha = fechaActual
            )

            // Guardar la factura en la base de datos
            try {
                facturaDao.insert(nuevaFactura)
                Toast.makeText(requireContext(), "Factura guardada correctamente", Toast.LENGTH_SHORT).show()

                // Limpiar los campos después de facturar
                limpiarCampos(view)
            } catch (e: Exception) {
                Toast.makeText(requireContext(), "Error al guardar la factura: ${e.message}", Toast.LENGTH_SHORT).show()
            }
        } else {
            println("Cliente no encontrado")
            Toast.makeText(requireContext(), "Cliente no encontrado", Toast.LENGTH_SHORT).show()
        }
    }


    private fun guardarFactura(numeroFactura: Int) {
        val database = AppDatabase.getDatabase(requireContext()) // Obtiene la instancia de la base de datos
        productoFacturaAdapter.guardarDetalleFactura(numeroFactura, database) // Pasa la base de datos en lugar de productos
        Toast.makeText(requireContext(), "Factura guardada", Toast.LENGTH_SHORT).show()
    }

    fun limpiarCampos(view: View) {
        view.findViewById<TextView>(R.id.txtNombusF).text = ""
        view.findViewById<TextView>(R.id.txtCedbusF).text = ""
        view.findViewById<TextView>(R.id.txtSubToF).text = "Subtotal: 0.00"
        view.findViewById<TextView>(R.id.txtIvaF).text = "IVA (15%): 0.00"
        view.findViewById<TextView>(R.id.txtTotalF).text = "Total: 0.00"
        view.findViewById<EditText>(R.id.txtCedRbF).text.clear()
        view.findViewById<EditText>(R.id.txtProdRbF).text.clear()
        view.findViewById<EditText>(R.id.txtCantRbF).text.clear()
        productoFacturaAdapter.limpiarProductos()
    }

    fun buscarCliente(view: View){
        //inicia los textos de cedula
        var cedula = view.findViewById<EditText>(R.id.txtCedRbF).text.toString()
        // Inicializar la base de datos
        db=AppDatabase.getDatabase(requireContext())
        // Obtener datos de la base de datos para cliente
        val clienteDao = db.clienteDao()
        println( "Cedula busqueda por : $cedula")
        val clientes = clienteDao.findByCedula(cedula)
        println("busqueda exitosa")
        var txtnombre = view.findViewById<TextView>(R.id.txtNombusF)
        var txtcedula = view.findViewById<TextView>(R.id.txtCedbusF)
        if(clientes != null){
            txtnombre.text = "Nombre: ${clientes.nombresClient}"
            txtcedula.text = "Cedula: ${clientes.cedulaClient}"
        }else{
            txtnombre.text = "Cliente no encontrado"
            txtcedula.text = "null none"
        }
    }
    fun buscarProducto(view: View) {
        // Obtener el código del producto desde el EditText
        val codProd = view.findViewById<EditText>(R.id.txtProdRbF).text.toString()
        val CantProd = view.findViewById<EditText>(R.id.txtCantRbF).text.toString()

        // Inicializar la base de datos
        db = AppDatabase.getDatabase(requireContext())
        val productoDao = db.productoDao()

        println("Producto búsqueda por: $codProd")

        // Verificar si el campo de búsqueda está vacío o nulo
        if (codProd.isNullOrEmpty()) {
            // Si está vacío, obtener todos los productos
            val todosLosProductos = productoDao.getAll()
            println("Mostrando todos los productos: ${todosLosProductos.size}")

            // Inicializa el RecyclerView para productos
            recyclerView = view.findViewById(R.id.rcvProdF)
            recyclerView.layoutManager = LinearLayoutManager(requireContext())

            // Inicializa el adaptador si aún no lo has hecho
            if (!::productoFacturaAdapter.isInitialized) {
                productoFacturaAdapter = ProductoFacturaAdapter(mutableListOf(), CantProd.toInt()) { producto ->
                    // Acción al eliminar un producto
                    println("Eliminar producto: ${producto.nameProduct}")
                }
                recyclerView.adapter = productoFacturaAdapter
            }

            // Agregar todos los productos al adaptador
            for (productoBd in todosLosProductos) {
                // Convertir Productobd a Producto antes de agregarlo al adaptador
                val producto = productoBd.toProducto()
                productoFacturaAdapter.agregarProducto(producto, CantProd.toInt())
            }
        } else {
            // Si hay un código de producto, realizar la búsqueda específica
            val productosEncontrados = productoDao.findByCodeList(codProd)
            println("Búsqueda exitosa, productos encontrados: ${productosEncontrados.size}")

            // Inicializa el RecyclerView para productos
            recyclerView = view.findViewById(R.id.rcvProdF)
            recyclerView.layoutManager = LinearLayoutManager(requireContext())

            // Inicializa el adaptador si aún no lo has hecho
            if (!::productoFacturaAdapter.isInitialized) {
                productoFacturaAdapter = ProductoFacturaAdapter(mutableListOf(), CantProd.toInt()) { producto ->
                    // Acción al eliminar un producto
                    println("Eliminar producto: ${producto.nameProduct}")
                }
                recyclerView.adapter = productoFacturaAdapter
            }

            // Agregar productos encontrados al adaptador
            for (productoBd in productosEncontrados) {
                // Convertir Productobd a Producto antes de agregarlo al adaptador
                val producto = productoBd.toProducto()
                productoFacturaAdapter.agregarProducto(producto, CantProd.toInt())
            }
        }

        calcularIVA(view)
    }

    private fun Productobd.toProducto(): Producto {
        val CantProd = requireView().findViewById<EditText>(R.id.txtCantRbF).text.toString()
        return Producto(
            id = this.id,
            nameProduct = this.nameProduct,
            precioUnitario = this.precio,
            stock = this.stock,
            cantidad = CantProd.toInt()
        )
    }

    fun calcularIVA(view: View) {
        var total: Double = 0.0

        // Sumar el precio de todos los productos (precioUnitario * cantidad)
        for (producto in productoFacturaAdapter.getProductos()) {
            total += producto.precioUnitario * producto.cantidad
        }

        // Calcular el IVA (15%)
        val iva = total * 0.15
        val totalConIva = total + iva

        // Formatear los valores a dos decimales
        val totalFormateado = String.format("%.2f", total)
        val ivaFormateado = String.format("%.2f", iva)
        val totalConIvaFormateado = String.format("%.2f", totalConIva)

        // Obtener las vistas solo una vez
        val totalConIvaText = view.findViewById<TextView>(R.id.txtTotalF)
        val subtotalText = view.findViewById<TextView>(R.id.txtSubToF)
        val ivaText = view.findViewById<TextView>(R.id.txtIvaF)

        // Mostrar los resultados en las vistas correspondientes
        println("Total sin IVA: $totalFormateado")
        subtotalText.text = "Subtotal: $totalFormateado"
        ivaText.text = "IVA (15%): $ivaFormateado"
        println("Total con IVA: $totalConIva")
        totalConIvaText.text = "Total: $totalConIvaFormateado"
    }
    fun obtenerFechaActual(): String {
        val formato = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
        return formato.format(Date()) // Obtiene la fecha actual
    }

}