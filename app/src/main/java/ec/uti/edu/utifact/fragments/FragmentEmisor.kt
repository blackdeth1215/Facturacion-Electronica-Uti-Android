package ec.uti.edu.utifact.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.button.MaterialButton
import ec.uti.edu.utifact.R
import ec.uti.edu.utifact.adapter.ReporteAdapter
import ec.uti.edu.utifact.database.AppDatabase
import ec.uti.edu.utifact.database.databadeentity.Emisorbd
import ec.uti.edu.utifact.databasebd
import ec.uti.edu.utifact.entity.Emisor
import ec.uti.edu.utifact.ui.AdminActivity

class FragmentEmisor: Fragment() {
    private lateinit var recyclerView: RecyclerView
    private lateinit var clienteAdapter: Emisor
    private lateinit var db: AppDatabase
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.activity_emisor, container, false)
    }
    override fun onResume() {
        super.onResume()
        (activity as? AdminActivity)?.setFragmentTitle("Editar Emisor")
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Inicializar la base de datos
        db = AppDatabase.getDatabase(requireContext())
        val emisor = db.emisorDao().findById(1)

        val nombreEmi= requireView().findViewById<EditText>(R.id.txtEmiName)
        val direccionEmi= requireView().findViewById<EditText>(R.id.txtEmiDirec)
        val correoEmi= requireView().findViewById<EditText>(R.id.txtEmiCorr)
        val rucEmi= requireView().findViewById<EditText>(R.id.txtEmiRuc)
        val telefonoEmi= requireView().findViewById<EditText>(R.id.txtEmiTel)
        val sucursal = requireView().findViewById<EditText>(R.id.txtEmiSucur)
        val puntoEmision = requireView().findViewById<EditText>(R.id.txtEmiPunto)

        if (emisor != null) {
            nombreEmi.setText(emisor.razonSocial)
            direccionEmi.setText(emisor.direccion)
            correoEmi.setText(emisor.correo)
            rucEmi.setText(emisor.ruc)
            telefonoEmi.setText(emisor.telefono)
            sucursal.setText(emisor.sucursal)
            puntoEmision.setText(emisor.puntoEmision)
        } else {
            nombreEmi.setText("No encontrado")
        }

        val button = view.findViewById<MaterialButton>(R.id.btnGuardarEmi)
        button.setOnClickListener {
            onBuscarFactura(view)
        }
    }
    private fun onBuscarFactura(view: View) {
        val ruc = view.findViewById<EditText>(R.id.txtEmiRuc).text.toString()
        val nombre = view.findViewById<EditText>(R.id.txtEmiName).text.toString()
        val direccion = view.findViewById<EditText>(R.id.txtEmiDirec).text.toString()
        val correo = view.findViewById<EditText>(R.id.txtEmiCorr).text.toString()
        val telefono = view.findViewById<EditText>(R.id.txtEmiTel).text.toString()
        val sucursal = view.findViewById<EditText>(R.id.txtEmiSucur).text.toString()
        val puntoEmision = view.findViewById<EditText>(R.id.txtEmiPunto).text.toString()

        db = AppDatabase.getDatabase(requireContext())
        val emisor = db.emisorDao().update(Emisorbd(1, ruc, nombre, direccion,telefono, correo,sucursal,puntoEmision))
        if (emisor != null) {
            Toast.makeText(requireContext(), "Emisor actualizado", Toast.LENGTH_SHORT).show()
        } else {
            Toast.makeText(requireContext(), "Error al actualizar", Toast.LENGTH_SHORT).show()
        }
    }
}