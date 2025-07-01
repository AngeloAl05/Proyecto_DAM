package com.example.proyecto

import CompraAdapter
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.ImageButton
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.proyecto.entidad.Compra
import com.example.proyecto.utils.AppConfig
import com.google.firebase.FirebaseApp
import com.google.firebase.database.*

class ListaCompraActivity : AppCompatActivity() {

    private lateinit var rvCompras: RecyclerView
    private lateinit var btnSeguirCompra: Button
    private lateinit var compraAdapter: CompraAdapter
    private lateinit var database: DatabaseReference
    private lateinit var compras: ArrayList<Compra>
    private lateinit var btnAdmin:ImageButton

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.carrito_compra_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        rvCompras = findViewById(R.id.rvCompras)
        btnSeguirCompra = findViewById(R.id.btnSeguirCompra)
        compras = ArrayList()
        btnSeguirCompra.setOnClickListener { inicio() }
        FirebaseApp.initializeApp(this)
        btnAdmin=findViewById(R.id.btnAdmin)
        btnAdmin.setOnClickListener { login() }
        database = FirebaseDatabase.getInstance().reference
        cargarProductos()

        compraAdapter = CompraAdapter(compras, database,
            onPagarClick = { compra ->
                val intent = Intent(AppConfig.CONTEXTO, CompraActivity::class.java).apply {
                    putExtra("NOMBRE DEL PRODUCTO", compra.nombreProduc)
                    putExtra("CANTIDAD", compra.cantidadProduc)
                    putExtra("PRECIO", compra.precioProduc)
                    addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                }
                startActivity(intent)
            },
            onDeleteSuccess = {
                showAlert("Producto eliminado correctamente")
            }
        )
        rvCompras.adapter = compraAdapter
        rvCompras.layoutManager = LinearLayoutManager(this)
    }
    fun login(){
        var data= Intent(this,LoginActivity::class.java)
        startActivity(data)
    }
    fun cargarProductos() {
        database.child("carrito").addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val updatedCompras = ArrayList<Compra>()
                for (productoSnapshot in snapshot.children) {
                    val producto = productoSnapshot.getValue(Compra::class.java)
                    if (producto != null) {
                        if (producto.cantidadProduc > 0) {
                            val existingCompra = compras.find { it.nombreProduc == producto.nombreProduc }
                            if (existingCompra != null) {
                                existingCompra.cantidadProduc = producto.cantidadProduc
                                existingCompra.precioProduc = producto.precioProduc
                                updatedCompras.add(existingCompra)
                            } else {
                                updatedCompras.add(producto)
                            }
                        } else {
                            productoSnapshot.ref.removeValue().addOnSuccessListener {
                                showAlert("Producto '${producto.nombreProduc}' eliminado porque la cantidad llegó a cero.")
                            }.addOnFailureListener {
                                showAlert("Error al eliminar el producto '${producto.nombreProduc}': ${it.message}")
                            }
                        }
                    }
                }
                compras.clear()
                compras.addAll(updatedCompras)
                compraAdapter.notifyDataSetChanged()
            }

            override fun onCancelled(error: DatabaseError) {
                showAlert(error.message)
            }
        })
    }


    fun showAlert(mensaje: String) {
        val builder = AlertDialog.Builder(this)
        builder.setTitle("SISTEMA")
        builder.setMessage(mensaje)
        builder.setPositiveButton("Aceptar", null)
        val dialog: AlertDialog = builder.create()
        dialog.show()
    }

    fun inicio() {
        val intent = Intent(this, ClienteActivity::class.java)
        startActivity(intent)
    }
}

