package com.example.proyecto

import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.proyecto.adaptador.ProveedorAdapter
import com.example.proyecto.controller.ProveedorController
import com.example.proyecto.entidad.Proveedor

class ListaProveedorActivity: AppCompatActivity() {

    private lateinit var rvProveedor: RecyclerView
    private lateinit var btnNuevoProv:Button
    private lateinit var btnAdmin: ImageView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.mantenimiento_proveedor)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        rvProveedor = findViewById(R.id.rvProveedor)
        btnNuevoProv = findViewById(R.id.btnNuevoProv)
        btnAdmin = findViewById(R.id.btnAdmin)
        btnAdmin.setOnClickListener { admin() }
        btnNuevoProv.setOnClickListener { nuevo() }
        lista()
    }
    fun admin(){
        var intent = Intent(this, AdministradorActivity::class.java)
        startActivity(intent)
    }
    fun lista() {
        val proveedores = ProveedorController().findAll()
        val adaptador = ProveedorAdapter(proveedores, this::showDetailDialog) {
            showAlert("Proveedor eliminado correctamente")
            lista()
        }
        rvProveedor.layoutManager = LinearLayoutManager(this)
        rvProveedor.adapter = adaptador
    }
    fun nuevo(){
        val intent = Intent(this, ProveedorActivity::class.java)
        startActivity(intent)
    }
    fun showDetailDialog(proveedor: Proveedor) {
        val mensaje = """
            Código: ${proveedor.cod}
            Nombre: ${proveedor.nom}
            Dirección: ${proveedor.dir}
            Teléfono: ${proveedor.telefono}
            Correo: ${proveedor.correo}
        """.trimIndent()

        val builder = AlertDialog.Builder(this)
        builder.setTitle("Detalle del Proveedor")
        builder.setMessage(mensaje)
        builder.setPositiveButton("Aceptar", null)
        val dialog: AlertDialog = builder.create()
        dialog.show()
    }
    fun showAlert(mensaje: String) {
        val builder = AlertDialog.Builder(this)
        builder.setTitle("SISTEMA")
        builder.setMessage(mensaje)
        builder.setPositiveButton("Aceptar", null)
        val dialog: AlertDialog = builder.create()
        dialog.show()
    }

}