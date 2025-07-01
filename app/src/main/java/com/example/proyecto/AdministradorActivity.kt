package com.example.proyecto

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.ImageButton
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class AdministradorActivity: AppCompatActivity() {

    private lateinit var btnAdmin:ImageButton
    private lateinit var btnManProveedor:Button
    private lateinit var btnManUsuarios:Button
    private lateinit var btnManCategoria:Button
    private lateinit var btnManProducto:Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.admin_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        btnManProveedor=findViewById(R.id.btnManProveedor)
        btnManUsuarios=findViewById(R.id.btnManUsuarios)
        btnManCategoria=findViewById(R.id.btnManCategoria)
        btnManProducto=findViewById(R.id.btnManProducto)
        btnAdmin=findViewById(R.id.btnAdmin)
        btnAdmin.setOnClickListener { login() }
        btnManProveedor.setOnClickListener { proveedor() }
        btnManUsuarios.setOnClickListener { usuarios() }
        btnManCategoria.setOnClickListener { categoria() }
        btnManProducto.setOnClickListener { producto() }

    }
    fun login(){
        var data= Intent(this,LoginActivity::class.java)
        startActivity(data)
    }
    fun proveedor(){
        var data= Intent(this,ListaProveedorActivity::class.java)
        startActivity(data)
    }
    fun usuarios(){
        var data=Intent(this,ListaUsuarioActivity::class.java)
        startActivity(data)
    }
    fun categoria(){
        var data=Intent(this,ListaCategoriaActivity::class.java)
        startActivity(data)
    }
    fun producto(){
        var data=Intent(this,ListaProductoActivity::class.java)
        startActivity(data)
    }
}