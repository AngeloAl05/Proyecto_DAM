package com.example.proyecto.adaptador

import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.proyecto.R

class ViewCompra(vista: View): RecyclerView.ViewHolder(vista) {

    var txtNombreCarrito: TextView
    var txtPrecio: TextView
    var txtCantidad: TextView
    var ImgCarrito:ImageView
    var btnEliminarProdCarrito: Button

    init {
        txtNombreCarrito = vista.findViewById(R.id.txtNombreCarrito)
        txtPrecio = vista.findViewById(R.id.txtPrecio)
        txtCantidad = vista.findViewById(R.id.txtCantidad)
        ImgCarrito = vista.findViewById(R.id.imgFoto)
        btnEliminarProdCarrito = vista.findViewById(R.id.btnEliminarProdCarrito)
    }
}