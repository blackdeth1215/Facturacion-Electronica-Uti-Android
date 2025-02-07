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
import ec.uti.edu.utifact.database.databadeentity.Userbd
import ec.uti.edu.utifact.entity.Cliente
import ec.uti.edu.utifact.ui.AdminActivity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class FragmentEdAcCliente: Fragment() {

    private lateinit var db: AppDatabase

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.activity_clientes_dat, container, false)

        return view
    }

    override fun onResume() {
        super.onResume()
        (activity as? AdminActivity)?.setFragmentTitle("Editar usuario")
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        // Recibir info del otro fragmento
        val datoRecibido = arguments?.getString("username")
        // Inicializar la base de datos segun el dato si es 0 no trae los datos
        db = AppDatabase.getDatabase(requireContext())
        val usuario = db.clienteDao().findByCedula(datoRecibido.toString())

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
            val usuarios = db.userDao().findByUser(datoRecibido.toString())
            lifecycleScope.launch {
                try {
                    // Obtener datos del usuario desde la base de datos (operación en segundo plano)
                    val usuario = withContext(Dispatchers.IO) { db.clienteDao().findByCedula(datoRecibido.toString()) }

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
                    Log.e("FragmentEdAcUser", "Error al buscar el usuario: ${e.message}", e)
                    Toast.makeText(requireContext(), "Error al buscar el usuario", Toast.LENGTH_SHORT).show()
                }
            }
        }
        //llama al boton cancelar
        val button = view.findViewById<Button>(R.id.btnCancelar)
        button.setOnClickListener {
            onCancelar(view)
        }
    }


    // Método para limpiar los campos de los EditText (modo creación)
    private fun limpiarCampos() {
        val nombreEditText: EditText = requireView().findViewById(R.id.etxtNombreCli)
        nombreEditText.setText("")
    }

    // Método para rellenar los campos con los datos de un usuario existente (modo edición)
    private fun llenarCamposConUsuario(usuario: Clientebd) {
        val nombreCli= requireView().findViewById<EditText>(R.id.etxtNombreCli)
        val direccionCli= requireView().findViewById<EditText>(R.id.etxtDireccionCli)
        val correoCli= requireView().findViewById<EditText>(R.id.etxtCorreoCli)
        val cedulaCli= requireView().findViewById<EditText>(R.id.etxtCedulaCli)
        val telefonoCli= requireView().findViewById<EditText>(R.id.etxtTelefonoCli)

        nombreCli.setText(usuario.nombresClient)
        direccionCli.setText(usuario.direccionClient)
        correoCli.setText(usuario.correoClient)
        cedulaCli.setText(usuario.cedulaClient)
        telefonoCli.setText(usuario.telefonoClient)
    }

    // Configurar el botón para guardar en modo creación
    private fun configurarBotonGuardarParaCreacion() {
        val botonGuardar: Button = requireView().findViewById(R.id.btnGuardar)
        botonGuardar.text = "Guardar Usuario"

        botonGuardar.setOnClickListener {
            val nombreCli= requireView().findViewById<EditText>(R.id.etxtNombreCli)
            val direccionCli= requireView().findViewById<EditText>(R.id.etxtDireccionCli)
            val correoCli= requireView().findViewById<EditText>(R.id.etxtCorreoCli)
            val cedulaCli= requireView().findViewById<EditText>(R.id.etxtCedulaCli)
            val telefonoCli= requireView().findViewById<EditText>(R.id.etxtTelefonoCli)

            val nuevoNombre = nombreCli.text.toString().trim()
            val nuevaDireccion = direccionCli.text.toString().trim()
            val nuevoCorreo = correoCli.text.toString().trim()
            val nuevaCedula = cedulaCli.text.toString().trim()
            val nuevoTelefono = telefonoCli.text.toString().trim()

            // Validar datos antes de guardar
            if (validarInputs(nuevoNombre, nuevaCedula)) {
                val id = db.clienteDao().insert(Clientebd(nombresClient = nuevoNombre, direccionClient = nuevaDireccion, correoClient = nuevoCorreo, cedulaClient = nuevaCedula, telefonoClient = nuevoTelefono))
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
    private fun configurarBotonGuardarParaEdicion(usuario: Clientebd) {
        val botonGuardar: Button = requireView().findViewById(R.id.btnGuardar)
        botonGuardar.text = "Actualizar Usuario"

        botonGuardar.setOnClickListener {
            val nombreCli= requireView().findViewById<EditText>(R.id.etxtNombreCli)
            val direccionCli= requireView().findViewById<EditText>(R.id.etxtDireccionCli)
            val correoCli= requireView().findViewById<EditText>(R.id.etxtCorreoCli)
            val cedulaCli= requireView().findViewById<EditText>(R.id.etxtCedulaCli)
            val telefonoCli= requireView().findViewById<EditText>(R.id.etxtTelefonoCli)

            val nuevoNombre = nombreCli.text.toString().trim()
            val nuevaDireccion = direccionCli.text.toString().trim()
            val nuevoCorreo = correoCli.text.toString().trim()
            val nuevaCedula = cedulaCli.text.toString().trim()
            val nuevoTelefono = telefonoCli.text.toString().trim()


            // Validar datos antes de actualizar
            if (validarInputs(nuevoNombre, nuevaDireccion)) {
                val actualizado = db.clienteDao().update(Clientebd(id = usuario.id, nombresClient = nuevoNombre, direccionClient = nuevaDireccion, correoClient = nuevoCorreo, cedulaClient = nuevaCedula, telefonoClient = nuevoTelefono))
                Log.d("FragmentEdAcUser", "Usuario actualizado: $actualizado  id de usuario: ${usuario.id}")
                if (actualizado > 0) {
                    Toast.makeText(requireContext(), "Cliente actualizado correctamente", Toast.LENGTH_SHORT).show()
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
        val newFragment = FragmentClient()

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