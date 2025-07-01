package com.example.proyecto.adaptador

import android.app.AlertDialog
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.proyecto.CategoriaActualizarActivity
import com.example.proyecto.R
import com.example.proyecto.entidad.Categoria
import com.example.proyecto.services.ApiServiceCategoria
import com.example.proyecto.utils.ApiUtils
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class CategoriaAdapter(
    private val context: Context,
    private var lista: ArrayList<Categoria>,
    private val onDetailClick: (Categoria) -> Unit,
    private val onDeleteSuccess: () -> Unit
) : RecyclerView.Adapter<ViewCategoria>() {

    private val apiCateg: ApiServiceCategoria = ApiUtils.getAPICategoria()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewCategoria {
        val inflador = LayoutInflater.from(parent.context).inflate(R.layout.item_lista_categoria, parent, false)
        return ViewCategoria(inflador)
    }

    override fun getItemCount(): Int {
        return lista.size
    }

    override fun onBindViewHolder(holder: ViewCategoria, position: Int) {
        val categoria = lista[position]
        holder.txtCodigoCat.setText(categoria.idCategoria.toString())
        holder.txtNombreCat.setText(categoria.nombreCate)

        holder.btnDetallesCat.setOnClickListener {
            onDetailClick(categoria)
        }
        holder.btnEditarCat.setOnClickListener {
            val intent = Intent(holder.itemView.context, CategoriaActualizarActivity::class.java)
            intent.putExtra("codigo", categoria.idCategoria)
            ContextCompat.startActivity(holder.itemView.context, intent, null)
        }
        holder.btnEliminarCat.setOnClickListener {
            showAlertEliminar("¿Está seguro de eliminar la categoría con el ID: ${categoria.idCategoria}?", categoria.idCategoria,position)
        }
    }

    fun showAlertEliminar(men: String, idCategoria: Int,position:Int) {
        val builder = AlertDialog.Builder(context)
        builder.setTitle("Sistema")
        builder.setMessage(men)
        builder.setPositiveButton("Aceptar") { dialogInterface: DialogInterface, i: Int ->
            apiCateg.deleteById(idCategoria).enqueue(object : Callback<Void> {
                override fun onResponse(call: Call<Void>, response: Response<Void>) {
                    if (response.isSuccessful) {
                        lista.removeAt(position)
                        notifyItemRemoved(position)
                        showAlert(context, "Categoría eliminada correctamente")
                        onDeleteSuccess()
                    } else {
                        showAlert(context, "Error al eliminar la categoría")
                    }
                }

                override fun onFailure(call: Call<Void>, t: Throwable) {
                    showAlert(context, t.localizedMessage)
                }
            })
        }
        builder.setNegativeButton("Cancelar", null)
        val dialog: AlertDialog = builder.create()
        dialog.show()
    }

    private fun showAlert(context: Context, men: String) {
        val builder = AlertDialog.Builder(context)
        builder.setTitle("SISTEMA")
        builder.setMessage(men)
        builder.setPositiveButton("Aceptar", null)
        val dialog: AlertDialog = builder.create()
        dialog.show()
    }
}
