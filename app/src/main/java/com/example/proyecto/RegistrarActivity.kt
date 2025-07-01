package com.example.proyecto

import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.AutoCompleteTextView
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.android.material.textfield.TextInputEditText
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import java.util.Properties
import javax.mail.Authenticator
import javax.mail.Message
import javax.mail.PasswordAuthentication
import javax.mail.Session
import javax.mail.Transport
import javax.mail.internet.InternetAddress
import javax.mail.internet.MimeMessage
import com.google.firebase.database.*

class RegistrarActivity: AppCompatActivity() {

    private lateinit var txtUsuarioRegistrar: TextInputEditText
    private lateinit var txtApellidoRegistrar: TextInputEditText
    private lateinit var txtContrasenaRegistrar: TextInputEditText
    private lateinit var txtCorreoRegistrar: TextInputEditText
    private lateinit var spnTipoRegistrar: AutoCompleteTextView
    private lateinit var btnRegistrarUsuario: Button
    private lateinit var btnPagIniciarSesion: Button

    private lateinit var auth: FirebaseAuth
    private lateinit var BD: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.registrar_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        txtUsuarioRegistrar=findViewById(R.id.txtUsuarioRegistrar)
        txtApellidoRegistrar=findViewById(R.id.txtApellidoRegistrar)
        txtContrasenaRegistrar=findViewById(R.id.txtContrasenaRegistrar)
        txtCorreoRegistrar=findViewById(R.id.txtCorreoRegistrar)
        spnTipoRegistrar=findViewById(R.id.spnTipoRegistrar)
        btnRegistrarUsuario=findViewById(R.id.btnRegistrarUsuario)
        btnPagIniciarSesion=findViewById(R.id.btnPagIniciarSesion)
        auth = FirebaseAuth.getInstance()
        BD = FirebaseDatabase.getInstance().reference
        btnRegistrarUsuario.setOnClickListener { registrarusu() }
        btnPagIniciarSesion.setOnClickListener { iniciarsesion() }
    }
    fun registrarusu() {
        var usuario = txtUsuarioRegistrar.text.toString()
        var apellido = txtApellidoRegistrar.text.toString()
        var contrasena = txtContrasenaRegistrar.text.toString()
        var correo = txtCorreoRegistrar.text.toString()
        var tipo = spnTipoRegistrar.text.toString()

        if (usuario.isEmpty() || apellido.isEmpty() || contrasena.isEmpty() || correo.isEmpty() || tipo.isEmpty()) {
            val camposFaltantes = mutableListOf<String>()
            if (usuario.isEmpty()) camposFaltantes.add("Nombre")
            if (apellido.isEmpty()) camposFaltantes.add("Apellido")
            if (contrasena.isEmpty()) camposFaltantes.add("Contraseña")
            if (correo.isEmpty()) camposFaltantes.add("Correo")
            if (tipo.isEmpty()) camposFaltantes.add("Tipo")

            showAlert("Falta completar los siguientes campos:\n${camposFaltantes.joinToString("\n")}")
            return
        }

        auth.createUserWithEmailAndPassword(correo, contrasena)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    val userId = auth.currentUser?.uid
                    if (userId != null) {
                        val usuarioMap = mapOf(
                            "Username" to usuario,
                            "Apellido" to apellido,
                            "Password" to contrasena,
                            "Email" to correo,
                            "Tipo" to tipo
                        )
                        BD.child("Usuarios").child(userId).setValue(usuarioMap)
                            .addOnCompleteListener {
                                if (it.isSuccessful) {
                                    showAlert("Usuario registrado con éxito") {
                                        startActivity(Intent(this, LoginActivity::class.java))
                                        finish()
                                    }
                                } else {
                                    showAlert("Error al registrar usuario en la base de datos.")
                                }
                            }
                    } else {
                        showAlert("Error al registrar usuario.")
                    }
                } else {
                    showAlert("Error al registrar usuario: ${task.exception?.message}")
                }
            }
    }

    fun iniciarsesion() {
        val intent = Intent(this, LoginActivity::class.java)
        startActivity(intent)
    }

    private fun showAlert(men: String, onDismiss: (() -> Unit)? = null) {
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Sistema")
        builder.setMessage(men)
        builder.setPositiveButton("Aceptar") { _, _ ->
            onDismiss?.invoke()
        }
        val dialog: AlertDialog = builder.create()
        dialog.show()
    }
}
