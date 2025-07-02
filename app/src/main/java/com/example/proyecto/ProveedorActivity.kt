package com.example.proyecto

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.proyecto.controller.ProveedorController
import com.example.proyecto.entidad.Proveedor
import com.google.android.material.textfield.TextInputEditText

class ProveedorActivity: AppCompatActivity()  {

    private lateinit var txtNombre: TextInputEditText
    private lateinit var txtDireccion: TextInputEditText
    private lateinit var txtTelefono: TextInputEditText
    private lateinit var txtCorreo: TextInputEditText
    private lateinit var btnGrabar: Button
    private lateinit var btnRegresar: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.proveedor_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        txtNombre=findViewById(R.id.txtNombreProveedor)
        txtDireccion=findViewById(R.id.txtDireccionProveedor)
        txtTelefono=findViewById(R.id.txtTelefonoProveedor)
        txtCorreo=findViewById(R.id.txtCorreoProveedor)
        btnGrabar=findViewById(R.id.btnGrabarProveedor)
        btnRegresar=findViewById(R.id.btnRegresarProveedor)

        btnGrabar.setOnClickListener { grabar() }
        btnRegresar.setOnClickListener { regresar() }
    }
    fun grabar(){
        var nom=txtNombre.text.toString()
        var dir=txtDireccion.text.toString()
        var telefono=txtTelefono.text.toString()
        var correo=txtCorreo.text.toString()

        if (nom.isBlank() || dir.isBlank() || telefono.isBlank() || correo.isBlank()) {
            val camposFaltantes = mutableListOf<String>()
            if (nom.isBlank()) camposFaltantes.add("Nombre")
            if (dir.isBlank()) camposFaltantes.add("Dirección")
            if (telefono.isBlank()) camposFaltantes.add("Teléfono")
            if (correo.isBlank()) camposFaltantes.add("Correo")

            mostrarAlerta("Falta ingresar los siguientes campos:\n${camposFaltantes.joinToString("\n")}")
            return
        }

        var bean=Proveedor(0,nom,dir,telefono,correo)
        var salida=ProveedorController().save(bean)

        if(salida>0)
            showAlert("Proveedor registrado correctamente")
        else
            showAlert("Error en el registro de proveedor")
    }
    fun regresar(){
        var intent= Intent(this,ListaProveedorActivity::class.java)
        startActivity(intent)
    }
    fun showAlert(men:String){
        val builder= AlertDialog.Builder(this)
        builder.setTitle("SISTEMA")
        builder.setMessage(men)
        builder.setPositiveButton("Aceptar",null)
        val dialog: AlertDialog =builder.create()
        dialog.show()
    }
    private fun mostrarAlerta(mensaje: String) {
        AlertDialog.Builder(this)
            .setTitle("SISTEMA")
            .setMessage(mensaje)
            .setPositiveButton("Aceptar") { dialog, _ ->
                dialog.dismiss()
            }
            .show()
    }
}