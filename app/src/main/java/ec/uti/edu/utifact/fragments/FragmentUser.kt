package ec.uti.edu.utifact.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.button.MaterialButton
import ec.uti.edu.utifact.R
import ec.uti.edu.utifact.adapter.UserAdapter
import ec.uti.edu.utifact.database

class FragmentUser: Fragment()   {
    private lateinit var recyclerView: RecyclerView
    private lateinit var usersadapter: UserAdapter
    private lateinit var dbHelper: database

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.activity_user, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        // Inicializar el RecyclerView
        recyclerView = view.findViewById(R.id.rcvUsers)
        recyclerView.layoutManager = LinearLayoutManager(context)
        // Inicializar la base de datos
        dbHelper = database(requireContext())

        // Obtener datos de la base de datos
        val users = dbHelper.getUsuarios()
        println("Usuarios obtenidos: $users")
        // Configurar el adaptador
        usersadapter = UserAdapter(
            users,
            onEditClick = { user ->
                // Lógica para editar cliente
                Toast.makeText(requireContext(), "Editar: ${user.user}", Toast.LENGTH_SHORT).show()
            },
            onDeleteClick = { user ->
                // Lógica para eliminar cliente
                Toast.makeText(requireContext(), "Eliminar: ${user.user}", Toast.LENGTH_SHORT).show()
            }
        )
        // Asignar el adaptador al RecyclerView
        recyclerView.adapter = usersadapter

        val button = view.findViewById<MaterialButton>(R.id.btnBusUser)
        button.setOnClickListener {
            onBuscarProducto(view)
        }
        val BtnAdd = view.findViewById<MaterialButton>(R.id.btnAddUser)
        BtnAdd.setOnClickListener {
            onAgregarProducto(view)
        }
    }
    private fun onBuscarProducto(view: View) {
        Toast.makeText(requireContext(), "Buscar producto clickeado", Toast.LENGTH_SHORT).show()
    }
    private fun onAgregarProducto(view: View) {
        Toast.makeText(requireContext(), "Agregar producto intent", Toast.LENGTH_SHORT).show()
    }
}