package ec.uti.edu.utifact.fragments

import android.app.DatePickerDialog
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.Spinner
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import ec.uti.edu.utifact.R
import ec.uti.edu.utifact.adapter.UserAdapter
import ec.uti.edu.utifact.database.AppDatabase
import ec.uti.edu.utifact.database.databadeentity.Userbd
import ec.uti.edu.utifact.databasebd
import ec.uti.edu.utifact.entity.User
import ec.uti.edu.utifact.ui.AdminActivity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.math.BigInteger
import java.security.MessageDigest
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale

class FragmentEdAcUser: Fragment() {

    private lateinit var db: AppDatabase
    private var rolSeleccionado: Int = 0

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragments_users_dat, container, false)

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
        val usuario = db.userDao().findByUser(datoRecibido.toString())

        //configurar spinner
        val spinner: Spinner = view.findViewById(R.id.spinner_roles)


        // Obtener los valores del array de strings.xml
        val rolesArray = resources.getStringArray(R.array.roles)

        // Crear adaptador
        val adapter = ArrayAdapter.createFromResource(
            requireContext(),
            R.array.roles,
            R.layout.spinner_item
        )


        // Asignar el adaptador al Spinner
        spinner.adapter = adapter

        // Seleccionar el primer rol del elemento
        val posicionRol = 0
        spinner.setSelection(posicionRol)

        //seleccionar fecha con el calendario
        val etxtFechaEdit = view.findViewById<EditText>(R.id.etxtFechaEdit)
        // Al hacer clic en el campo de texto, abrir el calendario
        etxtFechaEdit.setOnClickListener {
            mostrarDatePicker(etxtFechaEdit)
        }

        if(datoRecibido.isNullOrEmpty()) {
            // **Modo creación**: Añadir un nuevo usuario
            Log.d("FragmentEdAcUser", "Modo creación: No se recibió un ID. Configurando para agregar un nuevo usuario.")
            val fecha = obtenerFechaActual()
            val fechaActual = view.findViewById<EditText>(R.id.etxtFecha)
            Log.d("FragmentEdAcUser", "Modo edición fecha actual $fecha")
            fechaActual.setText(fecha)
            fechaActual.isEnabled = false
            // Configurar el botón para guardar un nuevo usuario
            configurarBotonGuardarParaCreacion()

            // Limpiar campos para ingreso de datos
            limpiarCampos()

        }else{
            // **Modo edición**: Editar usuario existente
            Log.d("FragmentEdAcUser", "Modo edición: ID recibido: $datoRecibido")
            // Obtener datos del usuario desde la base de datos
            val usuarios = db.userDao().findByUser(datoRecibido.toString())
            lifecycleScope.launch {
                try {
                    // Obtener datos del usuario desde la base de datos (operación en segundo plano)
                    val usuario = withContext(Dispatchers.IO) { db.userDao().findByUser(datoRecibido.toString()) }

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
        val nombreEditText: EditText = requireView().findViewById(R.id.etxtUsuario)
        val passwordEditText: EditText = requireView().findViewById(R.id.extPass)
        val passwordEditTextC: EditText = requireView().findViewById(R.id.extPassC)

        nombreEditText.setText("")
        passwordEditText.setText("")
        passwordEditTextC.setText("")
    }

    // Método para rellenar los campos con los datos de un usuario existente (modo edición)
    private fun llenarCamposConUsuario(usuario: Userbd) {
        val nombreEditText: EditText = requireView().findViewById(R.id.etxtUsuario)
        val passwordEditText: EditText = requireView().findViewById(R.id.extPass)
        val passwordEditTextC: EditText = requireView().findViewById(R.id.extPassC)
        val fechaActual: EditText = requireView().findViewById(R.id.etxtFecha)
        val fechaCaducidad: EditText = requireView().findViewById(R.id.etxtFechaEdit)
        val spinnerRoles: Spinner=requireView().findViewById(R.id.spinner_roles)


        nombreEditText.setText(usuario.user)
        fechaActual.setText(usuario.fechaCreacion)
        fechaCaducidad.setText(usuario.fechaCaducidad)
        passwordEditText.setText(usuario.password)
        passwordEditTextC.setText(usuario.password)
        spinnerRoles.setSelection(usuario.rol)
        fechaActual.isEnabled = false

    }

    // Configurar el botón para guardar en modo creación
    private fun configurarBotonGuardarParaCreacion() {
        val botonGuardar: Button = requireView().findViewById(R.id.btnGuardar)
        botonGuardar.text = "Guardar Usuario"

        botonGuardar.setOnClickListener {
            val nombreEditText: EditText = requireView().findViewById(R.id.etxtUsuario)
            val fechaActual: EditText = requireView().findViewById(R.id.etxtFecha)
            val fechaEdit: EditText = requireView().findViewById(R.id.etxtFechaEdit)
            val passwordEditText: EditText = requireView().findViewById(R.id.extPass)
            val passwordEditTextC: EditText = requireView().findViewById(R.id.extPassC)
            val spinnerRoles: Spinner=requireView().findViewById(R.id.spinner_roles)

            val nombre = nombreEditText.text.toString().trim()
            val fecha = fechaActual.text.toString().trim()
            val fechaEdit1 = fechaEdit.text.toString().trim()
            val password = if(passwordEditTextC == passwordEditText){
                passwordEditText.text.toString().trim()
            }else{
                passwordEditTextC.text.toString().trim()
            }
            // Lista de roles con su respectivo valor numérico
            val valoresRoles = listOf(0, 1, 2) // 0: No seleccionado, 1: Admin, 2: Usuario
            val posicionSeleccionada = spinnerRoles.selectedItemPosition
            val rol = valoresRoles[posicionSeleccionada]

            // Validar que se haya seleccionado un rol válido
            if (rol == 0) {
                Toast.makeText(requireContext(), "Seleccione un rol válido", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            // Validar datos antes de guardar
            if (validarInputs(nombre, passwordEditText.text.toString().trim(),passwordEditTextC.text.toString().trim(),fecha,fechaEdit1)) {
                val passwordMD5 = password.toMD5()
                val id = db.userDao().insert(Userbd(user = nombre, password = passwordMD5, fechaCreacion = fecha, fechaCaducidad = fechaEdit1 , rol = rol))
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
    private fun configurarBotonGuardarParaEdicion(usuario: Userbd) {
        val botonGuardar: Button = requireView().findViewById(R.id.btnGuardar)
        botonGuardar.text = "Actualizar Usuario"

        botonGuardar.setOnClickListener {
            val nombreEditText: EditText = requireView().findViewById(R.id.etxtUsuario)
            val fechaActual: EditText = requireView().findViewById(R.id.etxtFecha)
            val fechaEdit: EditText = requireView().findViewById(R.id.etxtFechaEdit)
            val passwordEditText: EditText = requireView().findViewById(R.id.extPass)
            val passwordEditTextC: EditText = requireView().findViewById(R.id.extPassC)
            val spinnerRoles: Spinner=requireView().findViewById(R.id.spinner_roles)

            val nuevoNombre = nombreEditText.text.toString().trim()
            val nuevaFecha = fechaActual.text.toString().trim()
            val nuevaFechaEdit = fechaEdit.text.toString().trim()
            val nuevoPassword = if(passwordEditTextC == passwordEditText){
            passwordEditText.text.toString().trim()
            }else{
                passwordEditTextC.text.toString().trim()
            }
            // Lista de roles con su respectivo valor numérico
            val valoresRoles = listOf(0, 1, 2) // 0: No seleccionado, 1: Admin, 2: Usuario
            val posicionSeleccionada = spinnerRoles.selectedItemPosition
            val rol = valoresRoles[posicionSeleccionada]

            // Validar que se haya seleccionado un rol válido
            if (rol == 0) {
                Toast.makeText(requireContext(), "Seleccione un rol válido", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            // Validar datos antes de actualizar
            if (validarInputs(nuevoNombre, passwordEditText.text.toString().trim(),passwordEditTextC.text.toString().trim(),nuevaFecha,nuevaFechaEdit)) {
                val passwordMD5 = nuevoPassword.toMD5()
                val actualizado = db.userDao().update(Userbd(id = usuario.id, user = nuevoNombre, password = passwordMD5, fechaCreacion =nuevaFecha, fechaCaducidad = nuevaFechaEdit , rol = rol))
                Log.d("FragmentEdAcUser", "Usuario actualizado: $actualizado  id de usuario: ${usuario.id}")
                if (actualizado > 0) {
                    Toast.makeText(requireContext(), "Usuario actualizado correctamente", Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(requireContext(), "Error al actualizar el usuario", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    // Validar los datos antes de guardar/actualizar
    private fun validarInputs(nombre: String, password: String,passwordC: String,fecha: String,fechaEdit: String): Boolean {
        if (nombre.isEmpty()) {
            Toast.makeText(requireContext(), "El nombre no puede estar vacío", Toast.LENGTH_SHORT).show()
            return false
        }
        if (password.isEmpty()) {
            Toast.makeText(requireContext(), "La contraseña no puede estar vacía", Toast.LENGTH_SHORT).show()
            return false
        }
        if(fecha.isEmpty() || fechaEdit.isEmpty()){
            Toast.makeText(requireContext(), "La fecha no puede estar vacía", Toast.LENGTH_SHORT).show()
        }
        if(password != passwordC){
            println("password: $password , passwordC: $passwordC")
            Toast.makeText(requireContext(), "Las contraseñas no coinciden coloquelas bien", Toast.LENGTH_SHORT).show()
            limpiarCampos()
            return false
        }
        return true
    }

    fun onCancelar(view: View) {
        //limpia los campos
        limpiarCampos()
        //cambio de actividad al usuarios
        val newFragment = FragmentUser()

        // Obtener el FragmentManager y comenzar una nueva transacción
        val transaction = requireActivity().supportFragmentManager.beginTransaction()

        // Reemplazar el contenido del contenedor de fragmentos con el nuevo fragmento
        transaction.replace(R.id.fragment_container, newFragment)

        // Agregar la transacción a la pila para que el usuario pueda regresar
        transaction.addToBackStack(null)

        // Confirmar la transacción
        transaction.commit()
    }
    fun obtenerFechaActual(): String {
        val formato = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
        return formato.format(Date()) // Obtiene la fecha actual
    }
    private fun mostrarDatePicker(editText: EditText) {
        val calendario = Calendar.getInstance()

        val datePickerDialog = DatePickerDialog(
            requireContext(),
            { _, year, month, dayOfMonth ->
                val formatoFecha = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())

                // Fecha seleccionada
                val calendarioSeleccionado = Calendar.getInstance()
                calendarioSeleccionado.set(year, month, dayOfMonth)
                val fechaSeleccionada = formatoFecha.format(calendarioSeleccionado.time)

                // Fecha actual obtenida del EditText
                val fechaActualEditText = requireView().findViewById<EditText>(R.id.etxtFecha)
                val fechaActualTexto = fechaActualEditText.text.toString()

                try {
                    val fechaActualDate = formatoFecha.parse(fechaActualTexto)
                    val fechaSeleccionadaDate = formatoFecha.parse(fechaSeleccionada)

                    if (fechaSeleccionadaDate != null && fechaActualDate != null) {
                        if (fechaSeleccionadaDate.before(fechaActualDate)) {
                            editText.setText("")
                            Toast.makeText(
                                requireContext(),
                                "La fecha de caducidad no puede ser menor a la fecha actual",
                                Toast.LENGTH_SHORT
                            ).show()
                        } else {
                            editText.setText(fechaSeleccionada)
                        }
                    }
                } catch (e: Exception) {
                    Toast.makeText(requireContext(), "Error al procesar la fecha", Toast.LENGTH_SHORT).show()
                }
            },
            calendario.get(Calendar.YEAR),
            calendario.get(Calendar.MONTH),
            calendario.get(Calendar.DAY_OF_MONTH)
        )

        datePickerDialog.show()
    }

    fun String.toMD5(): String {
        val md = MessageDigest.getInstance("MD5")
        val digest = md.digest(this.toByteArray())
        val bigInt = BigInteger(1, digest)
        return bigInt.toString(16).padStart(32, '0')
    }
}