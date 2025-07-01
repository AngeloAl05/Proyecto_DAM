package com.example.proyecto

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.ImageView
import android.widget.LinearLayout
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.android.material.textfield.TextInputEditText
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.FirebaseAuthInvalidUserException
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class LoginActivity : AppCompatActivity() {

    private lateinit var txtUsuario: TextInputEditText
    private lateinit var txtContrasena: TextInputEditText
    private lateinit var btnIniciarSesion: Button
    private lateinit var btnGoogle: ImageView
    private lateinit var btnRegistrar: Button
    private lateinit var auth: FirebaseAuth
    private lateinit var BD: DatabaseReference
    private lateinit var googleSignInClient: GoogleSignInClient

    companion object {
        private const val TAG = "LoginActivity"
        private const val RC_SIGN_IN = 9001
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.login)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        txtUsuario = findViewById(R.id.txtUsuario)
        txtContrasena = findViewById(R.id.txtContrasena)
        btnIniciarSesion = findViewById(R.id.btnIniciarSesion)
        btnGoogle = findViewById(R.id.btnGoogle)
        btnRegistrar = findViewById(R.id.btnPagRegistrar)

        auth = FirebaseAuth.getInstance()
        BD = FirebaseDatabase.getInstance().reference

        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken("159135233620-gef7t8m67f4sdiog0n40nqbqi5lrd8ae.apps.googleusercontent.com")
            .requestEmail()
            .build()

        googleSignInClient = GoogleSignIn.getClient(this, gso)
        btnGoogle.setOnClickListener {
            googleSignIn()
        }

        btnIniciarSesion.setOnClickListener {
            iniciarSesion()
        }

        btnRegistrar.setOnClickListener {
            registrar()
        }
    }

    fun googleSignIn() {
        val signInIntent = googleSignInClient.signInIntent
        startActivityForResult(signInIntent, RC_SIGN_IN)
    }

    fun firebaseAuthWithGoogle(idToken: String) {
        val credential = GoogleAuthProvider.getCredential(idToken, null)
        auth.signInWithCredential(credential)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    Log.d(TAG, "signInWithCredential:success")
                    val user = auth.currentUser
                    updateUI(user)
                } else {
                    Log.w(TAG, "signInWithCredential:failure", task.exception)
                    showAlert("Error en inicio de sesión con Firebase: ${task.exception?.message}")
                    updateUI(null)
                }
            }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == RC_SIGN_IN) {
            val task = GoogleSignIn.getSignedInAccountFromIntent(data)
            try {
                val account = task.getResult(ApiException::class.java)!!
                firebaseAuthWithGoogle(account.idToken!!)
            } catch (e: ApiException) {
                Log.w(TAG, "Google sign in failed", e)
                showAlert("Error en inicio de sesión con Google: ${e.message}")
            }
        }
    }


    fun updateUI(user: FirebaseUser?) {
        if (user != null) {
            startActivity(Intent(this@LoginActivity, ClienteActivity::class.java))
            finish()
        } else {
            showAlert("Usuario no autenticado")
        }
    }


    fun showAlert(message: String) {
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Alerta")
        builder.setMessage(message)
        builder.setPositiveButton("Aceptar", null)
        val dialog: AlertDialog = builder.create()
        dialog.show()
    }

    fun iniciarSesion() {
        val usu = txtUsuario.text.toString()
        val contra = txtContrasena.text.toString()

        if (usu.isBlank() || contra.isBlank()) {
            val camposFaltantes = mutableListOf<String>()
            if (usu.isBlank()) camposFaltantes.add("Correo")
            if (contra.isBlank()) camposFaltantes.add("Contraseña")

            showAlert("Falta completar los siguientes campos:\n${camposFaltantes.joinToString("\n")}")
            return
        }

        auth.signInWithEmailAndPassword(usu, contra)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    val userId = auth.currentUser?.uid
                    if (userId != null) {
                        BD.child("Usuarios").child(userId).child("Tipo")
                            .addListenerForSingleValueEvent(object : ValueEventListener {
                                override fun onDataChange(snapshot: DataSnapshot) {
                                    val tipoUsuario = snapshot.getValue(String::class.java)
                                    Log.d("LoginActivity", "Tipo de usuario obtenido: $tipoUsuario")
                                    if (tipoUsuario == "Administrador") {
                                        startActivity(
                                            Intent(
                                                this@LoginActivity,
                                                AdministradorActivity::class.java
                                            )
                                        )
                                    } else {
                                        startActivity(
                                            Intent(
                                                this@LoginActivity,
                                                ClienteActivity::class.java
                                            )
                                        )
                                    }
                                    finish()
                                }

                                override fun onCancelled(error: DatabaseError) {
                                    showAlert("Error al obtener el tipo de usuario: ${error.message}")
                                }
                            })
                    }
                } else {
                    val errorMessage = when (task.exception) {
                        is FirebaseAuthInvalidUserException -> "El usuario no existe"
                        is FirebaseAuthInvalidCredentialsException -> "La contraseña es incorrecta."
                        else -> "Error al iniciar sesión: ${task.exception?.message}"
                    }
                    showAlert(errorMessage)
                }
            }
    }

    private fun registrar() {
        val intent = Intent(this, RegistrarActivity::class.java)
        startActivity(intent)
    }
}
