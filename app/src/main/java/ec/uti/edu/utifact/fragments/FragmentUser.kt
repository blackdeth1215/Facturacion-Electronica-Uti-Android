package ec.uti.edu.utifact.fragments

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.button.MaterialButton
import ec.uti.edu.utifact.R
import ec.uti.edu.utifact.adapter.UserAdapter
import ec.uti.edu.utifact.database.AppDatabase
import ec.uti.edu.utifact.database.databaseDao.UserDao
import ec.uti.edu.utifact.databasebd
import ec.uti.edu.utifact.ui.AdminActivity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class FragmentUser: Fragment()   {
    private lateinit var recyclerView: RecyclerView
    private lateinit var usersadapter: UserAdapter
    private lateinit var userDao: UserDao

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.activity_user, container, false)
    }
    override fun onResume() {
        super.onResume()
        (activity as? AdminActivity)?.setFragmentTitle("Usuarios")
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        // Inicializar el RecyclerView
        recyclerView = view.findViewById(R.id.rcvUsers)
        recyclerView.layoutManager = LinearLayoutManager(context)
        // Inicializar la base de datos
        val database = AppDatabase.getDatabase(requireContext())
        userDao = database.userDao()

        // Obtener datos de la base de datos
        loadUsers()

        // Configurar el adaptador
//        usersadapter = UserAdapter(
//            users,
//            onEditClick = { user ->
//                // Lógica para editar cliente
//                Toast.makeText(requireContext(), "Editar: ${user.user}", Toast.LENGTH_SHORT).show()
//            },
//            onDeleteClick = { user ->
//                // Lógica para eliminar cliente
//                Toast.makeText(requireContext(), "Eliminar: ${user.user}", Toast.LENGTH_SHORT).show()
//            }
//        )
        // Asignar el adaptador al RecyclerView
        //recyclerView.adapter = usersadapter

        val button = view.findViewById<MaterialButton>(R.id.btnBusUser)
        button.setOnClickListener {
            val Usuario = view.findViewById<TextView>(R.id.edtUserB).getText().toString()
            onBuscarProducto(view, Usuario)
        }
    }
    private fun onBuscarProducto(view: View,Usuario:String) {
        // Crear una instancia del nuevo fragmento
        val newFragment = FragmentEdAcUser()

        // Crear un Bundle y añadir el dato Int
        val args = Bundle()
        args.putString("miDatoClave", Usuario) // "miDatoClave" es la clave con la que accederás al dato

        // Configurar el Bundle en el nuevo fragmento
        newFragment.arguments = args

        // Obtener el FragmentManager y comenzar una nueva transacción
        val transaction = requireActivity().supportFragmentManager.beginTransaction()

        // Reemplazar el contenido del contenedor de fragmentos con el nuevo fragmento
        transaction.replace(R.id.fragment_container, newFragment)

        // Agregar la transacción a la pila para que el usuario pueda regresar
        transaction.addToBackStack(null)

        // Confirmar la transacción
        transaction.commit()
    }
    private fun onAgregarProducto(view: View) {
        val newFragment = FragmentEdAcUser()

        // Obtener el FragmentManager y comenzar una nueva transacción
        val transaction = requireActivity().supportFragmentManager.beginTransaction()

        // Reemplazar el contenido del contenedor de fragmentos con el nuevo fragmento
        transaction.replace(R.id.fragment_container, newFragment)

        // Agregar la transacción a la pila para que el usuario pueda regresar
        transaction.addToBackStack(null)

        // Confirmar la transacción
        transaction.commit()
    }

    private fun loadUsers() {
        // Obtener datos desde Room (operación en segundo plano)
        lifecycleScope.launch {
            val users = withContext(Dispatchers.IO) { userDao.getAll() }
            usersadapter = UserAdapter(
                users,
                onEditClick = { user ->
                    // Lógica para editar usuario
                    val newFragment = FragmentEdAcUser()

                    // Enviar dato al otro fragmento
                    val bundle = Bundle()
                    bundle.putString("username", user.user)
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
                onDeleteClick = { user ->
                    lifecycleScope.launch {
                        withContext(Dispatchers.IO) { userDao.delete(user) }
                        loadUsers() // Recargar usuarios después de eliminar
                        Toast.makeText(requireContext(), "Eliminado: ${user.user}", Toast.LENGTH_SHORT).show()
                    }
                }
            )
            recyclerView.adapter = usersadapter
        }
    }
}