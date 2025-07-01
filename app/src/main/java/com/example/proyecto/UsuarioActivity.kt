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

class UsuarioActivity: AppCompatActivity() {
    private lateinit var txtUsuario:TextInputEditText
    private lateinit var txtContrasena:TextInputEditText
    private lateinit var txtCorreo:TextInputEditText
    private lateinit var spnTipo:AutoCompleteTextView
    private lateinit var btnGrabarUsuario:Button
    private lateinit var btnRegresarUsuario:Button

    private lateinit var apiUsuario: ApiServiceUsuario

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.usuario_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        txtUsuario=findViewById(R.id.txtUsuario)
        txtContrasena=findViewById(R.id.txtContrasena)
        txtCorreo=findViewById(R.id.txtCorreo)
        spnTipo=findViewById(R.id.spnTipo)
        btnRegresarUsuario=findViewById(R.id.btnRegresarUsuario)
        btnGrabarUsuario=findViewById(R.id.btnGrabarUsuario)

        btnGrabarUsuario.setOnClickListener { grabar() }
        btnRegresarUsuario.setOnClickListener { regresar() }
        //
        apiUsuario= ApiUtils.getAPIUsuario()
    }
    fun grabar(){
        var usu=txtUsuario.text.toString()
        var contra=txtContrasena.text.toString()
        var correo=txtCorreo.text.toString()
        var tipo=spnTipo.text.toString()

        if (usu.isBlank() || contra.isBlank() || correo.isBlank() || tipo.isBlank()) {
            val camposFaltantes = mutableListOf<String>()
            if (usu.isBlank()) camposFaltantes.add("Usuario")
            if (contra.isBlank()) camposFaltantes.add("Contraseña")
            if (correo.isBlank()) camposFaltantes.add("Correo")
            if (tipo.isBlank()) camposFaltantes.add("Tipo de Usuario")

            mostrarAlerta("Falta ingresar los siguientes campos:\n${camposFaltantes.joinToString("\n")}")
            return
        }

        var usuario=Usuario(0,usu,contra,correo,tipo)
        registrar(usuario)
    }

    private fun mostrarAlerta(mensaje: String) {
        AlertDialog.Builder(this)
            .setTitle("SISTEMA")
            .setMessage(mensaje)
            .setPositiveButton("Aceptar") { dialog, _ ->
                dialog.dismiss()
            }
            .show()
    }
    
    fun registrar(bean: Usuario) {
        apiUsuario.save(bean).enqueue(object : Callback<Usuario> {
            override fun onResponse(call: Call<Usuario>, response: Response<Usuario>) {
                if (response.isSuccessful) {
                    runOnUiThread {
                        showAlert("Usuario registrado exitosamente")
                    }
                } else {
                    runOnUiThread {
                        showAlert("Error al registrar usuario: ${response.message()}")
                    }
                }
            }

            override fun onFailure(call: Call<Usuario>, t: Throwable) {
                runOnUiThread {
                    showAlert("Error: ${t.localizedMessage}")
                }
            }
        })
    }

    fun regresar(){
        var intent= Intent(this,ListaUsuarioActivity::class.java)
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
}