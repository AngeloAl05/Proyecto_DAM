package com.example.proyecto

import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.proyecto.adaptador.CategoriaAdapter
import com.example.proyecto.adaptador.ProveedorAdapter
import com.example.proyecto.controller.CategoriaController
import com.example.proyecto.controller.ProductoController
import com.example.proyecto.controller.ProveedorController
import com.example.proyecto.entidad.Categoria
import com.example.proyecto.services.ApiServiceCategoria
import com.example.proyecto.utils.ApiUtils
import com.example.proyecto.utils.AppConfig
import com.google.android.material.textfield.TextInputEditText
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ListaCategoriaActivity: AppCompatActivity() {

    private lateinit var btnNuevaCat: Button
    private lateinit var textBuscarCat: TextInputEditText
    private lateinit var btnBuscar: Button
    private lateinit var rvCategoria: RecyclerView
    private lateinit var btnAdmin:ImageView


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.mantenimiento_categoria)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        btnNuevaCat = findViewById(R.id.btnNuevaCat)
        textBuscarCat = findViewById(R.id.txtBuscarCat)
        btnBuscar = findViewById(R.id.btnBuscarCat)
        rvCategoria = findViewById(R.id.rvCategoria)
        btnAdmin = findViewById(R.id.btnAdmin)


        btnAdmin.setOnClickListener { admin() }
        btnNuevaCat.setOnClickListener { nueva() }
        btnBuscar.setOnClickListener { /*buscar()*/ }
        lista()
    }

    fun admin(){
        var intent = Intent(this, AdministradorActivity::class.java)
        startActivity(intent)
    }

    fun nueva() {
        var intent = Intent(this, CategoriaActivity::class.java)
        startActivity(intent)
    }

    fun lista() {
        val categorias = CategoriaController().findAll()
        val adaptador = CategoriaAdapter(categorias, this::showDetailDialog) {
            showAlert("Categoria eliminada correctamente")
            lista()
        }
        rvCategoria.layoutManager = LinearLayoutManager(this)
        rvCategoria.adapter = adaptador
    }


    fun showDetailDialog(categoria: Categoria) {
        val mensaje = """
        Código: ${categoria.idCategoria}
        Nombre: ${categoria.nombreCate}
    """.trimIndent()

        val builder = AlertDialog.Builder(this)
        builder.setTitle("Detalles de la Categoría")
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

    /*fun buscar() {
        val codigoCategoria = textBuscarCat.text.toString().toIntOrNull()
        if (codigoCategoria != null) {
            apiService.findById(codigoCategoria).enqueue(object : Callback<Categoria> {
                override fun onResponse(call: Call<Categoria>, response: Response<Categoria>) {
                    if (response.isSuccessful) {
                        val categoria = response.body()
                        if (categoria != null) {
                            categorias.clear()
                            categorias.add(categoria)
                            categoriaAdapter.notifyDataSetChanged()
                        } else {
                            Toast.makeText(
                                this@ListaCategoriaActivity,
                                "Categoría no encontrada",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    } else {
                        Toast.makeText(
                            this@ListaCategoriaActivity,
                            "Error al buscar categoría",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }

                override fun onFailure(call: Call<Categoria>, t: Throwable) {
                    Toast.makeText(
                        this@ListaCategoriaActivity,
                        "Error en la llamada: ${t.message}",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            })
        } else {
            cargar()
        }
    }*/

    /*fun buscar() {
        val codigoCategoria = textBuscarCat.text.toString().toIntOrNull()
        if (codigoCategoria==null) {
            lista()
        } else {
            var cate = CategoriaController().findById(codigoCategoria)
            categorias.clear()
            categorias.addAll(cate)
            categoriaAdapter?.notifyDataSetChanged()
        }
    }
*/
    /*private fun fetchCategorias() {
        apiService.findAll().enqueue(object : Callback<List<Categoria>> {
            override fun onResponse(
                call: Call<List<Categoria>>,
                response: Response<List<Categoria>>
            ) {
                if (response.isSuccessful) {
                    val categoriaList = response.body()
                    if (categoriaList != null) {
                        categorias.clear()
                        categorias.addAll(categoriaList)
                        categoriaAdapter.notifyDataSetChanged()
                    }
                } else {
                    Toast.makeText(
                        this@ListaCategoriaActivity,
                        "Error al cargar categorías",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }

            override fun onFailure(call: Call<List<Categoria>>, t: Throwable) {
                Toast.makeText(
                    this@ListaCategoriaActivity,
                    "Error en la llamada: ${t.message}",
                    Toast.LENGTH_SHORT
                ).show()
            }
        })
    }*/
}