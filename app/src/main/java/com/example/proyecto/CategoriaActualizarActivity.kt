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
import com.example.proyecto.controller.CategoriaController
import com.example.proyecto.controller.ProveedorController
import com.example.proyecto.entidad.Categoria
import com.example.proyecto.entidad.Usuario
import com.example.proyecto.services.ApiServiceCategoria
import com.example.proyecto.utils.ApiUtils
import com.google.android.material.textfield.TextInputEditText
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class CategoriaActualizarActivity: AppCompatActivity() {

    private lateinit var txtCodigo: TextInputEditText
    private lateinit var txtNombre: TextInputEditText
    private lateinit var btnActualizarCat: Button
    private lateinit var btnRegresarCat: Button


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.editar_categoria_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        txtCodigo=findViewById(R.id.txtCodigoCategoriaActualizar)
        txtNombre=findViewById(R.id.txtNombreCategoriaActualizar)

        btnActualizarCat=findViewById(R.id.btnActualizarCat)
        btnRegresarCat=findViewById(R.id.btnRegresarCat)


        btnActualizarCat.setOnClickListener { actualizar() }
        btnRegresarCat.setOnClickListener { regresar() }
        datos()
    }
    fun actualizar(){
        var cod=txtCodigo.text.toString().toInt()
        var nom=txtNombre.text.toString()

        var bean= Categoria(cod,nom)

        var salida= CategoriaController().update(bean)
        if(salida>0)
            showAlert("Categoria actualizada correctamente")
        else
            showAlert("Error en la actualizar la categoria")
    }

    fun regresar(){
        var intent= Intent(this,ListaCategoriaActivity::class.java)
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

    fun datos(){
        var info=intent.extras
        if (info != null) {
            val codigo = info.getInt("codigo", -1)
            if (codigo != -1) {
                val bean = CategoriaController().findById(codigo)
                if (bean != null) {
                    txtCodigo.setText(bean.idCategoria.toString())
                    txtNombre.setText(bean.nombreCate)
                } else {
                    showAlert("Categoria no encontrada")
                }
            } else {
                showAlert("Código de categoria inválido")
            }
        } else {
            showAlert("No se recibieron datos de la categoria")
        }
    }
}