package com.example.proyecto.adaptador

import android.view.View
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.proyecto.R

class ViewUsuario(vista: View): RecyclerView.ViewHolder(vista) {
    var txtCodigoUsu:TextView
    var txtNombreUsu:TextView
    var txtTipoUsu:TextView
    var btnEditarUsu:Button
    var btnDetallesUsu:Button
    var btnEliminarUsu:Button

    init {
        txtCodigoUsu=vista.findViewById(R.id.txtCodigoUsu)
        txtNombreUsu=vista.findViewById(R.id.txtNombreUsu)
        txtTipoUsu=vista.findViewById(R.id.txtTipoUsu)
        btnEditarUsu=vista.findViewById(R.id.btnEditarUsu)
        btnDetallesUsu=vista.findViewById(R.id.btnDetallesUsu)
        btnEliminarUsu=vista.findViewById(R.id.btnEliminarUsu)
    }
}