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

class CategoriaActualizarActivity: AppCompatActivity() {

    private lateinit var txtCodigo: TextInputEditText
    private lateinit var txtNombre: TextInputEditText
    private lateinit var txtDescripcion: TextInputEditText
    private lateinit var snpEstado: AutoCompleteTextView
    private lateinit var btnActualizarCat: Button
    private lateinit var btnRegresarCat: Button
    //
    private lateinit var api: ApiServiceCategoria
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
        txtDescripcion=findViewById(R.id.txtDescripcionCategoriaActualizar)
        snpEstado=findViewById(R.id.spnCategoriaActualizar)
        btnActualizarCat=findViewById(R.id.btnActualizarCat)
        btnRegresarCat=findViewById(R.id.btnRegresarCat)

        api= ApiUtils.getAPICategoria()

        btnActualizarCat.setOnClickListener { actualizar() }
        btnRegresarCat.setOnClickListener { regresar() }
        datos()
    }
    fun actualizar(){
        var cod=txtCodigo.text.toString().toInt()
        var nom=txtNombre.text.toString()
        var des=txtDescripcion.text.toString()
        var estado = stringToEstado(snpEstado.text.toString())

        var bean= Categoria(cod,nom)

        api.update(bean).enqueue(object: Callback<Categoria> {
            override fun onResponse(call: Call<Categoria>, response: Response<Categoria>) {
                if(response.isSuccessful){
                    showAlert("Categoria actualizada")
                }
            }
            override fun onFailure(call: Call<Categoria>, t: Throwable) {
                showAlert(t.localizedMessage)
            }
        })
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
        var info=intent.extras!!
        var cod=info.getInt("codigo")
        api.findById(cod).enqueue(object :Callback<Categoria>{
            override fun onResponse(call: Call<Categoria>, response: Response<Categoria>) {
                if(response.isSuccessful){
                    var obj=response.body()!!
                    txtCodigo.setText(obj.idCategoria.toString())
                    txtNombre.setText(obj.nombreCate)
                }
            }
            override fun onFailure(call: Call<Categoria>, t: Throwable) {
                showAlert(t.localizedMessage)
            }
        })
    }
    fun estadoToString(estado: Int): String {
        return if (estado == 1) "Activo" else "Inactivo"
    }

    fun stringToEstado(estado: String): Int {
        return if (estado.equals("Activo", ignoreCase = true)) 1 else 2
    }
}