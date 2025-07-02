package com.example.proyecto

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.widget.AutoCompleteTextView
import android.widget.Button
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.proyecto.controller.CategoriaController
import com.example.proyecto.entidad.Categoria
import com.example.proyecto.entidad.Usuario
import com.example.proyecto.services.ApiServiceCategoria
import com.example.proyecto.utils.ApiUtils
import com.google.android.material.textfield.TextInputEditText
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class CategoriaActivity: AppCompatActivity() {

    private lateinit var txtNombre: TextInputEditText
    private lateinit var btnNuevoCat: Button
    private lateinit var btnRegresarNuevo: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.categoria_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        txtNombre = findViewById(R.id.txtNombreCategoriaNuevo)
        btnNuevoCat = findViewById(R.id.btnNuevaCategoria)
        btnRegresarNuevo = findViewById(R.id.btnRegresarNuevo)

        btnNuevoCat.setOnClickListener { grabar() }
        btnRegresarNuevo.setOnClickListener { regresar() }
    }

    fun grabar() {
        val nom = txtNombre.text.toString()

        if (nom.isBlank()) {
            val camposFaltantes = mutableListOf<String>()
            if (nom.isBlank()) camposFaltantes.add("Nombre")

            mostrarAlerta("Falta ingresar los siguientes campos:\n${camposFaltantes.joinToString("\n")}")
            return
        }

        val cate = Categoria(0, nom)
        val salida = CategoriaController().save(cate)

        if(salida>0) {
            showAlert("Categoria registrado correctamente")
        } else {
            showAlert("Error en el registro del categoria")
        }
    }


    private fun regresar() {
        val intent = Intent(this, ListaCategoriaActivity::class.java)
        startActivity(intent)
    }

    fun mostrarAlerta(mensaje: String) {
        AlertDialog.Builder(this)
            .setTitle("SISTEMA")
            .setMessage(mensaje)
            .setPositiveButton("Aceptar") { dialog, _ ->
                dialog.dismiss()
            }
            .show()
    }

    fun showAlert(men:String){
        val builder= AlertDialog.Builder(this)
        builder.setTitle("SISTEMA")
        builder.setMessage(men)
        builder.setPositiveButton("Aceptar",null)
        val dialog: AlertDialog =builder.create()
        dialog.show()
    }
}