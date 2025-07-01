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
import com.example.proyecto.entidad.Usuario
import com.example.proyecto.services.ApiServiceUsuario
import com.example.proyecto.utils.ApiUtils
import com.google.android.material.textfield.TextInputEditText
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class UsuarioActualizarActivity: AppCompatActivity() {
    private lateinit var txtCodigo: TextInputEditText
    private lateinit var txtNombre: TextInputEditText
    private lateinit var txtContrasena: TextInputEditText
    private lateinit var txtCorreo: TextInputEditText
    private lateinit var snpTipo: AutoCompleteTextView
    private lateinit var btnActualizar: Button
    private lateinit var btnRegresar: Button
    //
    private lateinit var api:ApiServiceUsuario

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.editar_usuarios_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        txtCodigo=findViewById(R.id.txtCodigoUsuarioActualizar)
        txtNombre=findViewById(R.id.txtUsuarioActualizar)
        txtContrasena=findViewById(R.id.txtContrasenaActualizar)
        txtCorreo=findViewById(R.id.txtCorreoActualizar)
        snpTipo=findViewById(R.id.spnTipoActualizar)
        btnActualizar=findViewById(R.id.btnUsuarioActualizar)
        btnRegresar=findViewById(R.id.btnRegresarActualizar)

        api=ApiUtils.getAPIUsuario()

        btnActualizar.setOnClickListener { actualizar() }
        btnRegresar.setOnClickListener { regresar() }
        datos()
    }
    fun actualizar(){
        var cod=txtCodigo.text.toString().toInt()
        var nom=txtNombre.text.toString()
        var contra=txtContrasena.text.toString()
        var correo=txtCorreo.text.toString()
        var tipo=snpTipo.text.toString()

        var bean=Usuario(cod,nom,contra,correo,tipo)

        api.update(bean).enqueue(object: Callback<Usuario> {
            override fun onResponse(call: Call<Usuario>, response: Response<Usuario>) {
                if(response.isSuccessful){
                    showAlert("Usuario actualizado")
                }
            }
            override fun onFailure(call: Call<Usuario>, t: Throwable) {
                showAlert(t.localizedMessage)
            }
        })
    }
    fun showAlert(men:String){
        val builder= AlertDialog.Builder(this)
        builder.setTitle("SISTEMA")
        builder.setMessage(men)
        builder.setPositiveButton("Aceptar",null)
        val dialog: AlertDialog =builder.create()
        dialog.show()
    }
    fun regresar(){
        var intent= Intent(this,ListaUsuarioActivity::class.java)
        startActivity(intent)
    }
    fun datos(){
        var info=intent.extras!!
        var cod=info.getInt("codigo")
        api.findById(cod).enqueue(object :Callback<Usuario>{
            override fun onResponse(call: Call<Usuario>, response: Response<Usuario>) {
                if(response.isSuccessful){
                    var obj=response.body()!!
                    txtCodigo.setText(obj.idUser.toString())
                    txtNombre.setText(obj.username)
                    txtContrasena.setText(obj.password)
                    txtCorreo.setText(obj.emailUser)
                    snpTipo.setText(obj.tipoUser,false)
                }
            }
            override fun onFailure(call: Call<Usuario>, t: Throwable) {
                showAlert(t.localizedMessage)
            }
        })
    }
}