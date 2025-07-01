package com.example.proyecto

import android.content.Intent
import android.os.Bundle
import android.widget.AutoCompleteTextView
import android.widget.Button
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
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
    private lateinit var txtDescripcion: TextInputEditText
    private lateinit var snpEstado: AutoCompleteTextView
    private lateinit var btnNuevoCat: Button
    private lateinit var btnRegresarNuevo: Button
    //
    private lateinit var api: ApiServiceCategoria

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
        txtDescripcion = findViewById(R.id.txtDescripcionCategoriaNuevo)
        snpEstado = findViewById(R.id.spnCategoriaNuevo)
        btnNuevoCat = findViewById(R.id.btnNuevaCategoria)
        btnRegresarNuevo = findViewById(R.id.btnRegresarNuevo)

        api = ApiUtils.getAPICategoria()

        btnNuevoCat.setOnClickListener { grabar() }
        btnRegresarNuevo.setOnClickListener { regresar() }
    }

    fun grabar() {
        val nom = txtNombre.text.toString()
        val des = txtDescripcion.text.toString()
        val estado = snpEstado.text.toString()

        if (nom.isBlank() || des.isBlank() || estado.isBlank()) {
            val camposFaltantes = mutableListOf<String>()
            if (nom.isBlank()) camposFaltantes.add("Nombre")
            if (des.isBlank()) camposFaltantes.add("Descripción")
            if (estado.isBlank()) camposFaltantes.add("Estado")

            mostrarAlerta("Falta ingresar los siguientes campos:\n${camposFaltantes.joinToString("\n")}")
            return
        }

        val estadoInt = stringToEstado(estado)

        val cate = Categoria(0, nom, des, estadoInt)
        registrar(cate)
    }

    fun registrar(bean: Categoria) {
        api.save(bean).enqueue(object : Callback<Categoria> {
            override fun onResponse(call: Call<Categoria>, response: Response<Categoria>) {
                if (response.isSuccessful) {
                    val categoria = response.body()
                    if (categoria != null) {
                        showAlert("La Categoría se registró con el ID: ${categoria.idCategoria}")
                    } else {
                        showAlert("Error: Categoría recibida del servidor es nula")
                    }
                } else {
                    showAlert("Error al registrar categoría: ${response.message()}")
                }
            }

            override fun onFailure(call: Call<Categoria>, t: Throwable) {
                showAlert("Error: ${t.localizedMessage}")
            }
        })
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

    fun estadoToString(estado: Int): String {
        return if (estado == 1) "Activo" else "Inactivo"
    }

    fun stringToEstado(estado: String): Int {
        return if (estado.equals("Activo", ignoreCase = true)) 1 else 2
    }
    fun showAlert(mensaje: String, onPositiveClick: () -> Unit = {}) {
        AlertDialog.Builder(this)
            .setTitle("SISTEMA")
            .setMessage(mensaje)
            .setPositiveButton("Aceptar") { dialogInterface, _ ->
                dialogInterface.dismiss()
                onPositiveClick.invoke()
            }
            .setCancelable(false)
            .show()
    }
}