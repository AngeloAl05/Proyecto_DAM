package com.example.proyecto

import ProductoAdapter
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
import com.example.proyecto.controller.ProductoController
import com.example.proyecto.entidad.Producto
import com.google.android.material.textfield.TextInputEditText

class ListaProductoActivity: AppCompatActivity() {

    private lateinit var btnNuevaProd: Button
    private lateinit var textBuscarProd: TextInputEditText
    private lateinit var btnBuscar: Button
    private lateinit var btnAdmin: ImageView
    private lateinit var rvProducto: RecyclerView

    private var prodAdapter: ProductoAdapter? = null
    private val produc: ArrayList<Producto> = ArrayList()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.mantenimiento_producto)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        btnNuevaProd = findViewById(R.id.btnNuevoProducto)
        textBuscarProd = findViewById(R.id.txtBuscarProd)
        btnBuscar = findViewById(R.id.btnBuscarProducto)
        rvProducto = findViewById(R.id.rvProducto)
        btnAdmin = findViewById(R.id.btnAdmin)
        prodAdapter = ProductoAdapter(produc, true) { producto -> showDetailDialog(producto) }

        rvProducto.layoutManager = LinearLayoutManager(this)
        rvProducto.adapter = prodAdapter
        btnAdmin.setOnClickListener { admin() }
        btnNuevaProd.setOnClickListener { nuevo() }
        btnBuscar.setOnClickListener { buscar() }
        cargar()
    }
    fun admin(){
        var intent = Intent(this, AdministradorActivity::class.java)
        startActivity(intent)
    }
    fun nuevo() {
        var intent = Intent(this, ProductoActivity::class.java)
        startActivity(intent)
    }

    fun buscar() {
        var nom = textBuscarProd.text.toString().trim()
        if (nom.isEmpty()) {
            cargar()
        } else {
            var producto = ProductoController().findByNombre(nom)
            produc.clear()
            produc.addAll(producto)
            prodAdapter?.notifyDataSetChanged()
        }
    }

    fun showDetailDialog(producto: Producto) {
        val mensaje = """
        Código: ${producto.idProducto}
        Nombre: ${producto.nombreProduc}
        Cantidad: ${producto.cantidadProduc}
        Precio: ${producto.precioProduc}
        Unidades: ${producto.unidadesProduc}
        Categoría: ${producto.idCategoria}
        """.trimIndent()

        val builder = AlertDialog.Builder(this)
        builder.setTitle("Detalles del Producto")
        builder.setMessage(mensaje)
        builder.setPositiveButton("Aceptar", null)
        val dialog: AlertDialog = builder.create()
        dialog.show()
    }

    fun cargar(){
        produc.clear()
        produc.addAll(ProductoController().findAll())
        prodAdapter?.notifyDataSetChanged()
    }
}