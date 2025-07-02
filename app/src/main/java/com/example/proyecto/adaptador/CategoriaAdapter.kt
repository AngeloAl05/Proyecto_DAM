package com.example.proyecto.adaptador

import android.app.AlertDialog
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.proyecto.CategoriaActualizarActivity
import com.example.proyecto.R
import com.example.proyecto.controller.CategoriaController
import com.example.proyecto.entidad.Categoria
import com.example.proyecto.utils.AppConfig


class CategoriaAdapter(
    private var lista: ArrayList<Categoria>,
    private val onDetailClick: (Categoria) -> Unit,
    private val onDeleteSuccess: () -> Unit
) : RecyclerView.Adapter<ViewCategoria>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewCategoria {
        val inflador = LayoutInflater.from(parent.context).inflate(R.layout.item_lista_categoria, parent, false)
        return ViewCategoria(inflador)
    }

    override fun getItemCount(): Int {
        return lista.size
    }

    override fun onBindViewHolder(holder: ViewCategoria, position: Int) {
        val categoria = lista[position]
        holder.txtCodigoCat.text = categoria.idCategoria.toString()
        holder.txtNombreCat.text = categoria.nombreCate

        holder.btnDetallesCat.setOnClickListener {
            onDetailClick(categoria)
        }
        holder.btnEditarCat.setOnClickListener {
            val intent = Intent(holder.itemView.context, CategoriaActualizarActivity::class.java)
            intent.putExtra("codigo", lista[position].idCategoria)
            ContextCompat.startActivity(holder.itemView.context, intent, null)
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
            ContextCompat.startActivity(AppConfig.CONTEXTO, intent, null)
        }

        holder.btnEliminarCat.setOnClickListener {
            val builder = AlertDialog.Builder(holder.itemView.context)
            builder.setTitle("Eliminar Categoria")
            builder.setMessage("¿Está seguro de eliminar la categoría con el ID: ${categoria.idCategoria}?")

            builder.setPositiveButton("Aceptar") { dialogInterface, i ->
                val salida = CategoriaController().deleteById(categoria.idCategoria)
                if (salida > 0) {
                    lista.removeAt(position)
                    notifyItemRemoved(position)
                    notifyItemRangeChanged(position, lista.size)
                    onDeleteSuccess()
                } else {
                    showAlert("Error al eliminar el Categoria")
                }
            }

            builder.setNegativeButton("Cancelar", null)
            val dialog: AlertDialog = builder.create()
            dialog.show()
        }
    }


    private  fun showAlert(men:String){
        val builder= androidx.appcompat.app.AlertDialog.Builder(AppConfig.CONTEXTO)
        builder.setTitle("SISTEMA")
        builder.setMessage(men)
        builder.setPositiveButton("Aceptar",null)
        val dialog: androidx.appcompat.app.AlertDialog =builder.create()
        dialog.show()
    }
}
