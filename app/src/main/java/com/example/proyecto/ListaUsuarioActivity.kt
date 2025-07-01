package com.example.proyecto

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.proyecto.adaptador.ProveedorAdapter
import com.example.proyecto.adaptador.UsuarioAdapter
import com.example.proyecto.controller.ProveedorController
import com.example.proyecto.entidad.Usuario
import com.example.proyecto.services.ApiServiceUsuario
import com.example.proyecto.utils.ApiUtils
import com.example.proyecto.utils.AppConfig
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ListaUsuarioActivity: AppCompatActivity() {

    private lateinit var rvUsuario: RecyclerView
    private lateinit var btnNuevoUsu: Button
    private lateinit var btnAdmin: ImageView
    private lateinit var apiUsuario:ApiServiceUsuario

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.mantenimiento_cliente)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        rvUsuario=findViewById(R.id.rvUsuario)
        btnNuevoUsu=findViewById(R.id.btnNuevoUsu)
        btnAdmin = findViewById(R.id.btnAdmin)
        apiUsuario= ApiUtils.getAPIUsuario()
        btnNuevoUsu.setOnClickListener { nuevo() }
        btnAdmin.setOnClickListener { admin() }
        lista()
    }
    fun nuevo(){
        var intent= Intent(this,UsuarioActivity::class.java)
        startActivity(intent)
    }
    fun admin(){
        var intent = Intent(this, AdministradorActivity::class.java)
        startActivity(intent)
    }
    fun lista() {
        apiUsuario.findAll().enqueue(object : Callback<List<Usuario>> {
            override fun onResponse(call: Call<List<Usuario>>, response: Response<List<Usuario>>) {
                if (response.isSuccessful) {
                    val data = response.body()
                    val adaptador = UsuarioAdapter(
                        this@ListaUsuarioActivity,
                        data!! as ArrayList<Usuario>,
                        this@ListaUsuarioActivity::onDetailClick,
                        this@ListaUsuarioActivity::onDeleteSuccess
                    )
                    rvUsuario.layoutManager = LinearLayoutManager(this@ListaUsuarioActivity)
                    rvUsuario.adapter = adaptador
                }
            }

            override fun onFailure(call: Call<List<Usuario>>, t: Throwable) {
                showAlert(t.localizedMessage)
            }
        })
    }

    fun onDeleteSuccess() {
        lista()
    }

    fun onDetailClick(usuario: Usuario) {
        showDetailDialog(usuario)
    }

    fun showDetailDialog(usuario: Usuario) {
        val mensaje = """
            Código: ${usuario.idUser}
            Nombre: ${usuario.username}
            Contraseña: ${usuario.password}
            Email: ${usuario.emailUser}
            Tipo: ${usuario.tipoUser}
        """.trimIndent()

        val builder = AlertDialog.Builder(this)
        builder.setTitle("Detalle del Usuario")
        builder.setMessage(mensaje)
        builder.setPositiveButton("Aceptar", null)
        val dialog: AlertDialog = builder.create()
        dialog.show()
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