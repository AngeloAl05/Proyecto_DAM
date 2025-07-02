package com.example.proyecto.adaptador

import android.app.AlertDialog
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.proyecto.ProveedorActualizarActivity
import com.example.proyecto.R
import com.example.proyecto.controller.ProveedorController
import com.example.proyecto.entidad.Proveedor
import com.example.proyecto.utils.AppConfig

class ProveedorAdapter(
    private val lista: ArrayList<Proveedor>,
    private val onDetailClick: (Proveedor) -> Unit,
    private val onDeleteSuccess: () -> Unit
) : RecyclerView.Adapter<ViewProveedor>(){
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewProveedor {
        var inflador= LayoutInflater.from(parent.context).inflate(R.layout.item_lista_proveedor,parent,false)
        return ViewProveedor(inflador)
    }

    override fun getItemCount(): Int {
        return lista.size;
    }

    override fun onBindViewHolder(holder: ViewProveedor, position: Int) {
        val proveedor = lista[position]
        holder.txtCodigoProv.text = proveedor.cod.toString()
        holder.txtNombreProv.text = proveedor.nom

        holder.btnDetallesProv.setOnClickListener {
            onDetailClick(proveedor)
        }

        holder.btnEditarProv.setOnClickListener {
            val intent = Intent(AppConfig.CONTEXTO, ProveedorActualizarActivity::class.java)
            intent.putExtra("codigo", lista[position].cod)
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
            ContextCompat.startActivity(AppConfig.CONTEXTO, intent, null)
        }
        holder.btnEliminarProv.setOnClickListener {
            val builder = AlertDialog.Builder(holder.itemView.context)
            builder.setTitle("Eliminar Proveedor")
            builder.setMessage("¿Está seguro de eliminar al proveedor ${proveedor.cod}?")

            builder.setPositiveButton("Aceptar") { dialogInterface, i ->
                val salida = ProveedorController().deleteById(proveedor.cod)
                if (salida > 0) {
                    lista.removeAt(position)
                    notifyItemRemoved(position)
                    notifyItemRangeChanged(position, lista.size)
                    onDeleteSuccess()
                } else {
                    showAlert("Error al eliminar el proveedor")
                }
            }

            builder.setNegativeButton("Cancelar", null)
            val dialog: AlertDialog = builder.create()
            dialog.show()
        }
    }



    fun showAlert(men:String){
        val builder= androidx.appcompat.app.AlertDialog.Builder(AppConfig.CONTEXTO)
        builder.setTitle("SISTEMA")
        builder.setMessage(men)
        builder.setPositiveButton("Aceptar",null)
        val dialog: androidx.appcompat.app.AlertDialog =builder.create()
        dialog.show()
    }
}