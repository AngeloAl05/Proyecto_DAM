package com.example.proyecto.adaptador

import android.app.AlertDialog
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.proyecto.R
import com.example.proyecto.UsuarioActualizarActivity
import com.example.proyecto.entidad.Usuario
import com.example.proyecto.services.ApiServiceUsuario
import com.example.proyecto.utils.ApiUtils
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class UsuarioAdapter(
    private val context: Context,
    private val lista: ArrayList<Usuario>,
    private val onDetailClick: (Usuario) -> Unit,
    private val onDeleteSuccess: () -> Unit
) : RecyclerView.Adapter<ViewUsuario>() {

    private val apiUsuario: ApiServiceUsuario = ApiUtils.getAPIUsuario()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewUsuario {
        val inflador = LayoutInflater.from(parent.context).inflate(R.layout.item_manteniminto_usuarios, parent, false)
        return ViewUsuario(inflador)
    }

    override fun getItemCount(): Int {
        return lista.size
    }

    override fun onBindViewHolder(holder: ViewUsuario, position: Int) {
        val usuario = lista[position]
        holder.txtCodigoUsu.setText(usuario.idUser.toString())
        holder.txtNombreUsu.setText(usuario.username)
        holder.txtTipoUsu.setText(usuario.tipoUser)

        holder.btnDetallesUsu.setOnClickListener {
            onDetailClick(usuario)
        }

        holder.btnEditarUsu.setOnClickListener {
            val intent = Intent(context, UsuarioActualizarActivity::class.java)
            intent.putExtra("codigo", usuario.idUser)
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
            ContextCompat.startActivity(context, intent, null)
        }

        holder.btnEliminarUsu.setOnClickListener {
            showAlertEliminar("¿Está seguro de eliminar al usuario con el ID: ${usuario.idUser}?", usuario.idUser)
        }
    }

    fun showAlertEliminar(men: String, userId: Int) {
        val builder = AlertDialog.Builder(context)
        builder.setTitle("Sistema")
        builder.setMessage(men)
        builder.setPositiveButton("Aceptar") { dialogInterface: DialogInterface, i: Int ->
            apiUsuario.deleteById(userId).enqueue(object : Callback<Void> {
                override fun onResponse(call: Call<Void>, response: Response<Void>) {
                    if (response.isSuccessful) {
                        showAlert("Usuario eliminado correctamente")
                        onDeleteSuccess()
                    } else {
                        showAlert("Error al eliminar el usuario")
                    }
                }

                override fun onFailure(call: Call<Void>, t: Throwable) {
                    showAlert(t.localizedMessage)
                }
            })
        }
        builder.setNegativeButton("Cancelar", null)
        val dialog: AlertDialog = builder.create()
        dialog.show()
    }

    fun showAlert(men: String) {
        val builder = AlertDialog.Builder(context)
        builder.setTitle("SISTEMA")
        builder.setMessage(men)
        builder.setPositiveButton("Aceptar", null)
        val dialog: AlertDialog = builder.create()
        dialog.show()
    }
}
