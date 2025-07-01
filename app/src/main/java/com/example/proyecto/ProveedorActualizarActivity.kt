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
import com.example.proyecto.controller.ProveedorController
import com.example.proyecto.entidad.Proveedor
import com.google.android.material.textfield.TextInputEditText

class ProveedorActualizarActivity: AppCompatActivity() {
    private lateinit var txtCodigoProv: TextInputEditText
    private lateinit var txtNombreProv: TextInputEditText
    private lateinit var txtDireccionProv: TextInputEditText
    private lateinit var txtTelefonoProv: TextInputEditText
    private lateinit var txtCorreoProv: TextInputEditText
    private lateinit var btnActualizar: Button
    private lateinit var btnRegresar: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.editar_proveedor_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        txtCodigoProv=findViewById(R.id.txtCodigoProvActualizar)
        txtNombreProv=findViewById(R.id.txtNombreProvActualizar)
        txtDireccionProv=findViewById(R.id.txtDireccionProvActualizar)
        txtTelefonoProv=findViewById(R.id.txtTelefonoProvActualizar)
        txtCorreoProv=findViewById(R.id.txtCorreoProvActualizar)
        btnActualizar=findViewById(R.id.btnActualizar)
        btnRegresar=findViewById(R.id.btnRegresar)

        btnActualizar.setOnClickListener { actualizar() }
        btnRegresar.setOnClickListener { regresar() }

        datos()
    }
    fun actualizar(){
        var cod=txtCodigoProv.text.toString().toInt()
        var nom=txtNombreProv.text.toString()
        var dir=txtDireccionProv.text.toString()
        var telefono=txtTelefonoProv.text.toString()
        var correo=txtCorreoProv.text.toString()
        var bean=Proveedor(cod,nom,dir,telefono,correo)
        var salida=ProveedorController().update(bean)
        if(salida>0)
            showAlert("Proveedor actualizado correctamente")
        else
            showAlert("Error en la actualización del proveedor")
    }
    fun regresar(){
        var intent= Intent(this,ListaProveedorActivity::class.java)
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
        val info = intent.extras
        if (info != null) {
            val codigo = info.getInt("codigo", -1)
            if (codigo != -1) {
                val bean = ProveedorController().findById(codigo)
                if (bean != null) {
                    txtCodigoProv.setText(bean.cod.toString())
                    txtNombreProv.setText(bean.nom)
                    txtDireccionProv.setText(bean.dir)
                    txtTelefonoProv.setText(bean.telefono)
                    txtCorreoProv.setText(bean.correo)
                } else {
                    showAlert("Proveedor no encontrado")
                }
            } else {
                showAlert("Código de proveedor inválido")
            }
        } else {
            showAlert("No se recibieron datos del proveedor")
        }
    }
}