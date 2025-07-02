package com.example.proyecto

import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import android.widget.Button
import android.widget.ImageView
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.proyecto.controller.ProductoController
import com.example.proyecto.entidad.Categoria
import com.example.proyecto.entidad.Producto
import com.example.proyecto.services.ApiServiceCategoria
import com.example.proyecto.utils.ApiUtils
import com.google.android.material.textfield.TextInputEditText
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ProductoActualizarActivity : AppCompatActivity() {

    private lateinit var txtCodigo: TextInputEditText
    private lateinit var txtNombre: TextInputEditText
    private lateinit var txtCantidad: TextInputEditText
    private lateinit var txtPrecio: TextInputEditText
    private lateinit var txtStock: TextInputEditText
    private lateinit var spnCategoria: AutoCompleteTextView
    private lateinit var btnActualizarProd: Button
    private lateinit var btnRegresarActualizarProd: Button
    private lateinit var btnEliminarProd: Button

    private lateinit var apiCategoria: ApiServiceCategoria
    private var categorias: List<Categoria> = ArrayList()
    private var producto: Producto? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.editar_producto_main)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        txtCodigo = findViewById(R.id.txtCodigoActualizarProd)
        txtNombre = findViewById(R.id.txtNombreActualizarProd)
        txtCantidad = findViewById(R.id.txtCantidadActualizarProd)
        txtPrecio = findViewById(R.id.txtPrecioActualizarProd)
        txtStock = findViewById(R.id.txtUnidadEnExisActualizar)
        spnCategoria = findViewById(R.id.spnCategoriaActualizar)
        btnActualizarProd = findViewById(R.id.btnActualizarProd)
        btnRegresarActualizarProd = findViewById(R.id.btnRegresarActualizarProd)
        btnEliminarProd = findViewById(R.id.btnEliminarProd)


        apiCategoria = ApiUtils.getAPICategoria()

        btnActualizarProd.setOnClickListener { actualizar() }
        btnRegresarActualizarProd.setOnClickListener { regresar() }
        btnEliminarProd.setOnClickListener { eliminar() }

        cargarCategorias()
        datos()
    }

    private fun cargarCategorias() {
        apiCategoria.findAll().enqueue(object : Callback<List<Categoria>> {
            override fun onResponse(call: Call<List<Categoria>>, response: Response<List<Categoria>>) {
                if (response.isSuccessful) {
                    response.body()?.let {
                        categorias = it
                        val adapter = ArrayAdapter(this@ProductoActualizarActivity, android.R.layout.simple_dropdown_item_1line, categorias.map { it.nombreCate })
                        spnCategoria.setAdapter(adapter)
                    }
                }
            }

            override fun onFailure(call: Call<List<Categoria>>, t: Throwable) {
                showAlert(t.localizedMessage)
            }
        })
    }

    private fun datos() {
        val info = intent.extras!!
        val bean = ProductoController().findById(info.getInt("codigo"))
        txtCodigo.setText(bean.idProducto.toString())
        txtNombre.setText(bean.nombreProduc)
        txtCantidad.setText(bean.cantidadProduc.toString())
        txtPrecio.setText(bean.precioProduc.toString())
        txtStock.setText(bean.unidadesProduc.toString())


    }

    fun actualizar() {
        var cod = txtCodigo.text.toString().toInt()
        var nom = txtNombre.text.toString()
        var can = txtCantidad.text.toString().toInt()
        var pre = txtPrecio.text.toString().toDouble()
        var stock = txtStock.text.toString().toInt()
        var cat = spnCategoria.text.toString().toInt()

        var img = asignarImagen(nom)

        var bean = Producto(cod, nom, can, pre, stock,cat, img)

        var salida = ProductoController().update(bean)
        if (salida > 0) {
            showAlert("Producto actualizado correctamente")
        } else {
            showAlert("Error en la actualización del producto")
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

    fun regresar() {
        val intent = Intent(this, ListaProductoActivity::class.java)
        startActivity(intent)
        finish()
    }

    fun eliminar() {
        val cod = txtCodigo.text.toString().toInt()
        showAlertEliminar("Seguro de eliminar el Producto con ID : $cod", cod)
    }

    fun showAlertEliminar(s: String, cod: Int) {
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Sistema")
        builder.setMessage(s)
        builder.setPositiveButton("Aceptar") { _, _ ->
            val salida = ProductoController().deleteById(cod)
            if (salida > 0) {
                showAlert("Producto eliminado correctamente")
                regresar()
            } else {
                showAlert("Error en la eliminación del producto")
            }
        }
        builder.setNegativeButton("Cancelar", null)
        val dialog: AlertDialog = builder.create()
        dialog.show()
    }

    fun showAlert(mensaje: String) {
        if (!isFinishing) {
            val builder = AlertDialog.Builder(this)
            builder.setTitle("Mensaje")
            builder.setMessage(mensaje)
            builder.setPositiveButton("Aceptar", null)
            val dialog: AlertDialog = builder.create()
            dialog.show()
        }

    }
}
