package com.example.proyecto.adaptador

import android.view.View
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.proyecto.R

class ViewProveedor(vista: View): RecyclerView.ViewHolder(vista) {
         var txtCodigoProv:TextView
         var txtNombreProv:TextView
         var btnEditarProv:Button
         var btnDetallesProv:Button
         var btnEliminarProv:Button

 init {
     txtCodigoProv=vista.findViewById(R.id.txtCodigoProv)
     txtNombreProv=vista.findViewById(R.id.txtNombreProv)
     btnEditarProv=vista.findViewById(R.id.btnEditarProv)
     btnDetallesProv=vista.findViewById(R.id.btnDetallesProv)
     btnEliminarProv=vista.findViewById(R.id.btnEliminarProv)
 }


}