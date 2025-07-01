package com.example.proyecto.adaptador

import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.proyecto.R

class ViewProducto(vista: View): RecyclerView.ViewHolder(vista) {
    var txtCodigoProd: TextView
    var txtNombreProd: TextView
    var txtCategoriaProd: TextView
    var ImgFoto: ImageView
    var btnEditarProd: Button
    var btnDetallesProd: Button
    var btnCompraProd: Button

    init {
        txtCodigoProd = vista.findViewById(R.id.txtCodigoProd)
        txtNombreProd = vista.findViewById(R.id.txtNombreProd)
        txtCategoriaProd = vista.findViewById(R.id.txtCategoriaProd)
        ImgFoto = vista.findViewById(R.id.ImgFoto)
        btnEditarProd = vista.findViewById(R.id.btnEditarProd)
        btnDetallesProd = vista.findViewById(R.id.btnDetallesProd)
        btnCompraProd = vista.findViewById(R.id.btnComprarProd)
    }
}