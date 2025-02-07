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
import ec.uti.edu.utifact.adapter.ReporteAdapter
import ec.uti.edu.utifact.database.AppDatabase
import ec.uti.edu.utifact.databasebd
import ec.uti.edu.utifact.ui.AdminActivity

class FragmentReportUsuarios: Fragment() {
    private lateinit var recyclerView: RecyclerView
    private lateinit var reportadapter: ReporteAdapter
    private lateinit var db: AppDatabase

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.activity_reportes, container, false)
    }
    override fun onResume() {
        super.onResume()
        (activity as? AdminActivity)?.setFragmentTitle("Reportes de Usuarios")
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        // Inicializar el RecyclerView
        recyclerView = view.findViewById(R.id.rcvReports)
        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        // Inicializar la base de datos
        db = AppDatabase.getDatabase(requireContext())
        val reportes = db.facturaDao().getAll()


        // Configurar el adaptador
        reportadapter = ReporteAdapter(
            reportes,
            onEditClick = { facturas ->
                // Lógica para editar cliente
                Toast.makeText(requireContext(), "Editar: ${facturas.id}", Toast.LENGTH_SHORT).show()
            }
        )
        // Asignar el adaptador al RecyclerView
        recyclerView.adapter = reportadapter

        val button = view.findViewById<MaterialButton>(R.id.btnBusRep)
        button.setOnClickListener {
            onBuscarFactura(view)
        }
    }
    private fun onBuscarFactura(view: View) {
        Toast.makeText(requireContext(), "Buscar producto clickeado", Toast.LENGTH_SHORT).show()
    }
}