package com.example.proyecto.adaptador

import android.view.View
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.proyecto.R

class ViewCategoria(vista: View): RecyclerView.ViewHolder(vista) {
    var txtCodigoCat: TextView
    var txtNombreCat: TextView
    var btnEditarCat: Button
    var btnDetallesCat: Button
    var btnEliminarCat: Button

    init {
        txtCodigoCat = vista.findViewById(R.id.txtCodigoCat)
        txtNombreCat = vista.findViewById(R.id.txtNombreCat)
        btnEditarCat = vista.findViewById(R.id.btnEditarCat)
        btnDetallesCat = vista.findViewById(R.id.btnDetallesCat)
        btnEliminarCat = vista.findViewById(R.id.btnEliminarCat)
    }
}