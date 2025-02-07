package ec.uti.edu.utifact.fragments

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.button.MaterialButton
import ec.uti.edu.utifact.R
import ec.uti.edu.utifact.adapter.ClienteAdapter
import ec.uti.edu.utifact.adapter.ProductoAdapter
import ec.uti.edu.utifact.database.AppDatabase
import ec.uti.edu.utifact.database.databaseDao.ProductoDao
import ec.uti.edu.utifact.databasebd
import ec.uti.edu.utifact.ui.AdminActivity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class FragmentProducto: Fragment()  {
    private lateinit var recyclerView: RecyclerView
    private lateinit var productoadapter: ProductoAdapter
    private lateinit var productoDao: ProductoDao

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.activity_productos, container, false)
    }
    override fun onResume() {
        super.onResume()
        (activity as? AdminActivity)?.setFragmentTitle("Productos")
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        // Inicializar el RecyclerView
        recyclerView = view.findViewById(R.id.rcvProd)
        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        // Inicializar la base de datos
        val database = AppDatabase.getDatabase(requireContext())
        // Obtener datos de la base de datos
        productoDao = database.productoDao()

        //carga la lista de datos
        loadProducto()

        val button = view.findViewById<MaterialButton>(R.id.btnBusProd)
        button.setOnClickListener {
            onBuscarProducto(view)
        }
    }
    private fun onBuscarProducto(view: View) {
        Toast.makeText(requireContext(), "Buscar producto clickeado", Toast.LENGTH_SHORT).show()
    }
    private fun onAgregarProducto(view: View) {
        Toast.makeText(requireContext(), "Agregar producto intent", Toast.LENGTH_SHORT).show()
    }

    private fun loadProducto() {
        // Obtener datos desde Room (operación en segundo plano)
        lifecycleScope.launch {
            val productos = withContext(Dispatchers.IO) { productoDao.getAll() }
            productoadapter = ProductoAdapter(
                productos,
                onEditClick = { producto ->
                    // Lógica para editar producto
                    val newFragment = FragmentEdAcProducto()

                    // Enviar dato al otro fragmento
                    val bundle = Bundle()
                    bundle.putString("username", producto.codeProduct)
                    Log.d("FragmentEdAcUser", "envío de dato bundle ${bundle.toString()}.")

                    // Asociar el bundle al nuevo fragmento
                    newFragment.arguments = bundle

                    // Obtener el FragmentManager y comenzar una nueva transacción
                    val transaction = requireActivity().supportFragmentManager.beginTransaction()

                    // Reemplazar el contenido del contenedor de fragmentos con el nuevo fragmento
                    transaction.replace(R.id.fragment_container, newFragment)

                    // Agregar la transacción a la pila para que el usuario pueda regresar
                    transaction.addToBackStack(null)

                    // Confirmar la transacción
                    transaction.commit()
                },
                onDeleteClick = { producto ->
                    lifecycleScope.launch {
                        withContext(Dispatchers.IO) { productoDao.delete(producto) }
                        loadProducto() // Recargar usuarios después de eliminar
                        Toast.makeText(requireContext(), "Eliminado: ${producto.nameProduct}", Toast.LENGTH_SHORT).show()
                    }
                }
            )
            recyclerView.adapter = productoadapter
        }
    }
}