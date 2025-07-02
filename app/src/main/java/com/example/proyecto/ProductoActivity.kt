package com.example.proyecto

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import android.widget.Button
import android.widget.Spinner
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.proyecto.controller.CategoriaController
import com.example.proyecto.controller.ProductoController
import com.example.proyecto.entidad.Categoria
import com.example.proyecto.entidad.Producto
import com.example.proyecto.utils.ApiUtils
import com.google.android.material.textfield.TextInputEditText
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ProductoActivity : AppCompatActivity() {

    private lateinit var txtNombre: TextInputEditText
    private lateinit var txtCantidad: TextInputEditText
    private lateinit var txtPrecio: TextInputEditText
    private lateinit var txtStock: TextInputEditText
    private lateinit var spnCategoria: Spinner
    private lateinit var btnNuevoProd: Button
    private lateinit var btnRegresarNuevoProd: Button
    private var listaCategorias = CategoriaController().findAll()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.producto_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        txtNombre = findViewById(R.id.txtNombreNuevo)
        txtCantidad = findViewById(R.id.txtCantidadNuevo)
        txtPrecio = findViewById(R.id.txtPrecioNuevo)
        txtStock = findViewById(R.id.txtUnidadEnExisNuevo)
        spnCategoria = findViewById(R.id.spnCategoria)
        btnNuevoProd = findViewById(R.id.btnNuevoProducto)
        btnRegresarNuevoProd = findViewById(R.id.btnRegresarProducto)


        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, listaCategorias)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spnCategoria.adapter = adapter



        btnNuevoProd.setOnClickListener { grabar() }
        btnRegresarNuevoProd.setOnClickListener { regresar() }

    }

    fun grabar() {
        val nom = txtNombre.text.toString()
        val can = txtCantidad.text.toString().toIntOrNull()
        val pre = txtPrecio.text.toString().toDoubleOrNull()
        val stock = txtStock.text.toString().toIntOrNull()
        val catSeleccionada = spnCategoria.selectedItem as Categoria
        val cat = catSeleccionada.idCategoria

        if (nom.isBlank() || can ==null || pre == null || stock == null || cat== null) {
            val camposFaltantes = mutableListOf<String>()
            if (nom.isBlank()) camposFaltantes.add("Nombre")
            if (can == null) camposFaltantes.add("Cantidad")
            if (pre == null) camposFaltantes.add("Precio")
            if (stock == null) camposFaltantes.add("Stock")
            if (cat== null) camposFaltantes.add("Categoría")

            mostrarAlerta("Falta ingresar los siguientes campos:\n${camposFaltantes.joinToString("\n")}")
            return
        }

        val img = asignarImagen(nom)
        val bean = Producto(0, nom, can, pre, stock, cat, img)
        var salida = ProductoController().save(bean)

        if (salida != -1) {
            showAlert("Producto registrado correctamente") {
                Handler(mainLooper).postDelayed({
                    val intent = Intent(this, ListaProductoActivity::class.java)
                    startActivity(intent)
                    finish()
                }, 1500)
            }
        } else {
            showAlert("Error en el registro del producto")
        }
    }

    fun asignarImagen(nombre: String): String {
        return when (nombre.lowercase()) {
            "tres leches" -> "tresleches"
            "inka cola" -> "inkacola"
            "kr" -> "kr"
            "lays" -> "lays"
            "moka" -> "moka"
            "ole ole" -> "oleole"
            "oreo" -> "oreo"
            "picaras" -> "picaras"
            "rellenita" -> "rellenita"
            "selva negra" -> "selvanegra"
            "vainilla" -> "vainilla"
            "cheesetris" -> "cheesetris"
            "cheetos" -> "cheetos"
            "coca cola" -> "cocacola"
            "crocante chocolate" -> "crocantechocolate"
            "doritos" -> "doritos"
            "frugele" -> "frugele"
            "big ben" -> "bigben"
            "barriete" -> "barriete"
            "big cola" -> "bigcola"
            else -> "default_image"
        }
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
    fun regresar(){
        var intent = Intent(this, ListaProductoActivity::class.java)
        startActivity(intent)
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
