package com.example.proyecto

import ProductoAdapter
import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import android.widget.ImageView
import android.widget.LinearLayout
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.proyecto.controller.ProductoController
import com.example.proyecto.entidad.Producto

class ClienteActivity: AppCompatActivity() {

    private lateinit var btnCarrito: ImageView
    private lateinit var rvCliente:RecyclerView
    private lateinit var productoAdapter: ProductoAdapter
    private lateinit var productos: ArrayList<Producto>
    private lateinit var banner:ImageView
    private lateinit var perfilBtn: LinearLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.cliente_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        btnCarrito = findViewById(R.id.btnCarrito)
        rvCliente = findViewById(R.id.rvCliente)
        productos = ArrayList()
        banner = findViewById(R.id.imgBanner)
        perfilBtn = findViewById(R.id.perfilBtn)
        productoAdapter = ProductoAdapter(productos, false) { producto ->
            verDetalle(producto)
        }
        rvCliente.layoutManager = LinearLayoutManager(this)
        rvCliente.adapter = productoAdapter
        perfilBtn.setOnClickListener { perfil() }
        cargarProductos()
        btnCarrito.setOnClickListener { carrito() }
    }

    fun cargarProductos() {
        productos.clear()
        productos.addAll(ProductoController().findAll())
        productoAdapter.notifyDataSetChanged()
    }
    fun perfil(){
        var intent = Intent(this, LoginActivity::class.java)
        startActivity(intent)
    }

    fun carrito() {
        var intent = Intent(this, ListaCompraActivity::class.java)
        startActivity(intent)
    }
    fun verDetalle(producto: Producto){
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
}