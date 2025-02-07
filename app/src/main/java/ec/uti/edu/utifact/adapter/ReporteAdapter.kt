package ec.uti.edu.utifact.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import ec.uti.edu.utifact.R
import ec.uti.edu.utifact.database.databadeentity.Facturabd
import ec.uti.edu.utifact.entity.Factura

class ReporteAdapter(
    private val reportes: List<Facturabd>,
    private val onEditClick: (Facturabd) -> Unit
) : RecyclerView.Adapter<ReporteAdapter.ReporteViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ReporteViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_factura_reporte, parent, false)
        return ReporteViewHolder(view)
    }

    override fun onBindViewHolder(holder: ReporteViewHolder, position: Int) {
        val reporte = reportes[position]
        holder.bind(reporte)
    }


    override fun getItemCount(): Int = reportes.size

    class ReporteViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(reporte: Facturabd) {
            itemView.findViewById<TextView>(R.id.txtfacturanum).text = "Factura ${reporte.numero}"
            itemView.findViewById<TextView>(R.id.txtfecha).text = "Fecha: ${reporte.fecha}"
        }
    }
}