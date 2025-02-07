package ec.uti.edu.utifact.fragments

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import ec.uti.edu.utifact.R
import ec.uti.edu.utifact.database.AppDatabase
import ec.uti.edu.utifact.database.databadeentity.Clientebd
import ec.uti.edu.utifact.database.databadeentity.Productobd
import ec.uti.edu.utifact.ui.AdminActivity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class FragmentEdAcProducto: Fragment() {
    private lateinit var db: AppDatabase

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.activity_productos_dat, container, false)

        return view
    }

    override fun onResume() {
        super.onResume()
        (activity as? AdminActivity)?.setFragmentTitle("Editar Producto")
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        // Recibir info del otro fragmento
        val datoRecibido = arguments?.getString("username")
        // Inicializar la base de datos segun el dato si es 0 no trae los datos
        db = AppDatabase.getDatabase(requireContext())
        val usuario = db.productoDao().findByCode(datoRecibido.toString())

        if(datoRecibido.isNullOrEmpty()) {
            // **Modo creación**: Añadir un nuevo usuario
            Log.d("FragmentEdAcUser", "Modo creación: No se recibió un ID. Configurando para agregar un nuevo cliente.")

            // Configurar el botón para guardar un nuevo usuario
            configurarBotonGuardarParaCreacion()

            // Limpiar campos para ingreso de datos
            limpiarCampos()

        }else{
            // **Modo edición**: Editar usuario existente
            Log.d("FragmentEdAcUser", "Modo edición ID recibido: $datoRecibido")

            // Obtener datos del usuario desde la base de datos
            val usuarios = db.productoDao().findByCode(datoRecibido.toString())
            lifecycleScope.launch {
                try {
                    // Obtener datos del usuario desde la base de datos (operación en segundo plano)
                    val usuario = withContext(Dispatchers.IO) { db.productoDao().findByCode(datoRecibido.toString()) }

                    if (usuario != null) {
                        // Si el usuario existe, rellenar los campos y configurar el botón
                        llenarCamposConUsuario(usuario)
                        configurarBotonGuardarParaEdicion(usuario)
                    } else {
                        // Si el usuario no existe, mostrar un mensaje y registrar el error
                        Log.d("FragmentEdAcUser", "No se encontró el usuario con ID: $datoRecibido")
                        Toast.makeText(requireContext(), "Usuario no encontrado", Toast.LENGTH_SHORT).show()
                    }
                } catch (e: Exception) {
                    // Manejo de errores en caso de fallo
                    Log.e("FragmentEdAcUser", "Error al buscar el producto: ${e.message}", e)
                    Toast.makeText(requireContext(), "Error al buscar el producto", Toast.LENGTH_SHORT).show()
                }
            }
        }
        //llama al boton cancelar
        val button = view.findViewById<Button>(R.id.btnCancelarProd)
        button.setOnClickListener {
            onCancelar(view)
        }
    }


    // Método para limpiar los campos de los EditText (modo creación)
    private fun limpiarCampos() {
        val nombreCli= requireView().findViewById<EditText>(R.id.etxtCodProd)
        val direccionCli= requireView().findViewById<EditText>(R.id.etxtNomProd)
        val correoCli= requireView().findViewById<EditText>(R.id.etxtProveProd)
        val cedulaCli= requireView().findViewById<EditText>(R.id.etxtStockProd)
        val telefonoCli= requireView().findViewById<EditText>(R.id.etxtPrecioProd)

        nombreCli.setText("")
        direccionCli.setText("")
        correoCli.setText("")
        cedulaCli.setText("")
        telefonoCli.setText("")
    }

    // Método para rellenar los campos con los datos de un usuario existente (modo edición)
    private fun llenarCamposConUsuario(usuario: Productobd) {
        val nombreCli= requireView().findViewById<EditText>(R.id.etxtCodProd)
        val direccionCli= requireView().findViewById<EditText>(R.id.etxtNomProd)
        val correoCli= requireView().findViewById<EditText>(R.id.etxtProveProd)
        val cedulaCli= requireView().findViewById<EditText>(R.id.etxtStockProd)
        val telefonoCli= requireView().findViewById<EditText>(R.id.etxtPrecioProd)

        nombreCli.setText(usuario.codeProduct)
        direccionCli.setText(usuario.nameProduct)
        correoCli.setText(usuario.proveedor)
        cedulaCli.setText(usuario.stock.toString())
        telefonoCli.setText(usuario.precio.toString())
    }

    // Configurar el botón para guardar en modo creación
    private fun configurarBotonGuardarParaCreacion() {
        val botonGuardar: Button = requireView().findViewById(R.id.btnGuardarProd)
        botonGuardar.text = "Guardar Usuario"

        botonGuardar.setOnClickListener {
            val nombreCli= requireView().findViewById<EditText>(R.id.etxtCodProd)
            val direccionCli= requireView().findViewById<EditText>(R.id.etxtNomProd)
            val correoCli= requireView().findViewById<EditText>(R.id.etxtProveProd)
            val cedulaCli= requireView().findViewById<EditText>(R.id.etxtStockProd)
            val telefonoCli= requireView().findViewById<EditText>(R.id.etxtPrecioProd)

            val nuevoNombre = nombreCli.text.toString().trim()
            val nuevaDireccion = direccionCli.text.toString().trim()
            val nuevoCorreo = correoCli.text.toString().trim()
            val nuevaCedula = cedulaCli.text.toString().trim()
            val nuevoTelefono = telefonoCli.text.toString().trim()

            // Validar datos antes de guardar
            if (validarInputs(nuevoNombre, nuevaCedula)) {
                val id = db.productoDao().insert(Productobd(codeProduct=nuevoNombre, nameProduct=nuevaDireccion, proveedor=nuevoCorreo, stock=nuevaCedula.toInt(), precio=nuevoTelefono.toDouble()))
                if (id > 0) {
                    Toast.makeText(requireContext(), "Usuario creado con ID: $id", Toast.LENGTH_SHORT).show()
                    limpiarCampos() // Limpiar los campos después de guardar
                } else {
                    Toast.makeText(requireContext(), "Error al crear el usuario", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    // Configurar el botón para guardar en modo edición
    private fun configurarBotonGuardarParaEdicion(usuario: Productobd) {
        val botonGuardar: Button = requireView().findViewById(R.id.btnGuardarProd)
        botonGuardar.text = "Actualizar Usuario"

        botonGuardar.setOnClickListener {
            val nombreCli= requireView().findViewById<EditText>(R.id.etxtCodProd)
            val direccionCli= requireView().findViewById<EditText>(R.id.etxtNomProd)
            val correoCli= requireView().findViewById<EditText>(R.id.etxtProveProd)
            val cedulaCli= requireView().findViewById<EditText>(R.id.etxtStockProd)
            val telefonoCli= requireView().findViewById<EditText>(R.id.etxtPrecioProd)

            val nuevoNombre = nombreCli.text.toString().trim()
            val nuevaDireccion = direccionCli.text.toString().trim()
            val nuevoCorreo = correoCli.text.toString().trim()
            val nuevaCedula = cedulaCli.text.toString().trim()
            val nuevoTelefono = telefonoCli.text.toString().trim()


            // Validar datos antes de actualizar
            if (validarInputs(nuevoNombre, nuevaDireccion)) {
                val actualizado = db.productoDao().update(Productobd(id = usuario.id, codeProduct=nuevoNombre, nameProduct=nuevaDireccion, proveedor=nuevoCorreo, stock=nuevaCedula.toInt(), precio=nuevoTelefono.toDouble()))
                Log.d("FragmentEdAcUser", "Producto actualizado: $actualizado  id de usuario: ${usuario.id}")
                if (actualizado > 0) {
                    Toast.makeText(requireContext(), "Producto actualizado correctamente", Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(requireContext(), "Error al actualizar el usuario", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    // Validar los datos antes de guardar/actualizar
    private fun validarInputs(nombre: String, password: String): Boolean {
        if (nombre.isEmpty()) {
            Toast.makeText(requireContext(), "El nombre no puede estar vacío", Toast.LENGTH_SHORT).show()
            return false
        }
        if (password.isEmpty()) {
            Toast.makeText(requireContext(), "La contraseña no puede estar vacía", Toast.LENGTH_SHORT).show()
            return false
        }
        return true
    }

    fun onCancelar(view: View) {
        //limpia los campos
        limpiarCampos()
        //cambio de actividad al usuarios
        val newFragment = FragmentProducto()

        // Obtener el FragmentManager y comenzar una nueva transacción
        val transaction = requireActivity().supportFragmentManager.beginTransaction()

        // Reemplazar el contenido del contenedor de fragmentos con el nuevo fragmento
        transaction.replace(R.id.fragment_container, newFragment)

        // Agregar la transacción a la pila para que el usuario pueda regresar
        transaction.addToBackStack(null)

        // Confirmar la transacción
        transaction.commit()
    }
}