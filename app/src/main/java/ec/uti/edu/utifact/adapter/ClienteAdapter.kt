package ec.uti.edu.utifact.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import ec.uti.edu.utifact.R
import ec.uti.edu.utifact.database.databadeentity.Clientebd

class ClienteAdapter(
    private val clientes: List<Clientebd>,
    private val onEditClick: (Clientebd) -> Unit,
    private val onDeleteClick: (Clientebd) -> Unit
) : RecyclerView.Adapter<ClienteAdapter.ClienteViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ClienteViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_clients, parent, false)
        return ClienteViewHolder(view)
    }

    override fun onBindViewHolder(holder: ClienteViewHolder, position: Int) {
        val cliente = clientes[position]
        holder.bind(cliente)
        holder.itemView.findViewById<ImageButton>(R.id.imgEdit).setOnClickListener {
            onEditClick(cliente)
        }
        holder.itemView.findViewById<ImageButton>(R.id.imgDelete).setOnClickListener {
            onDeleteClick(cliente)
        }
    }

    override fun getItemCount(): Int = clientes.size

    class ClienteViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(cliente: Clientebd) {
            itemView.findViewById<TextView>(R.id.txtCliente).text = cliente.nombresClient
        }
    }
}

