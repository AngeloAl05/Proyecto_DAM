package com.example.proyecto

import android.content.Intent
import android.graphics.Typeface
import android.os.Bundle
import android.view.Gravity
import android.view.View
import android.widget.AutoCompleteTextView
import android.widget.Button
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.android.material.textfield.TextInputEditText

class CompraActivity:AppCompatActivity() {

    private lateinit var spnPago: AutoCompleteTextView
    private lateinit var txtNombreCompra: TextInputEditText
    private lateinit var txtDocumentoCompra: TextInputEditText
    private lateinit var txtProductoCompra: TextInputEditText
    private lateinit var txtCantidadCompra: TextInputEditText
    private lateinit var txtPrecioCompra: TextInputEditText
    private lateinit var txtTotalPagarCompra: TextInputEditText
    private lateinit var btnPagarCompra: Button
    private lateinit var btnRegresarCompra: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.detalle_compra_main)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        spnPago = findViewById(R.id.spnPago)
        txtNombreCompra = findViewById(R.id.txtNombreCompra)
        txtDocumentoCompra = findViewById(R.id.txtDocumentoCompra)
        txtProductoCompra = findViewById(R.id.txtProductoCompra)
        txtCantidadCompra = findViewById(R.id.txtCantidadCompra)
        txtPrecioCompra = findViewById(R.id.txtPrecioCompra)
        txtTotalPagarCompra = findViewById(R.id.txtTotalPagarCompra)
        btnPagarCompra = findViewById(R.id.btnPagarCompra)
        btnRegresarCompra = findViewById(R.id.btnRegresarCompra)

        btnRegresarCompra.setOnClickListener { regresar() }
        btnPagarCompra.setOnClickListener { pagar() }

        val nombreProducto = intent.getStringExtra("NOMBRE DEL PRODUCTO") ?: ""
        val cantidadProducto = intent.getIntExtra("CANTIDAD", 0)
        val precioProducto = intent.getDoubleExtra("PRECIO", 0.0)

        txtProductoCompra.setText(nombreProducto)
        txtCantidadCompra.setText(cantidadProducto.toString())
        txtPrecioCompra.setText(precioProducto.toString())
    }

    fun pagar() {
        val pago = spnPago.text.toString()
        val nom = txtNombreCompra.text.toString()
        val doc = txtDocumentoCompra.text.toString()

        if (pago.isBlank() || nom.isBlank() || doc.isBlank()) {
            val camposFaltantes = mutableListOf<String>()
            if (pago.isBlank()) camposFaltantes.add("Tipo de Pago")
            if (nom.isBlank()) camposFaltantes.add("Nombre")
            if (doc.isBlank()) camposFaltantes.add("Documento")

            mostrarAlerta("Falta ingresar los siguientes campos:\n${camposFaltantes.joinToString("\n")}")
            return
        }
        val cantidad = txtCantidadCompra.text.toString().toIntOrNull() ?: 0
        val precio = txtPrecioCompra.text.toString().toDoubleOrNull() ?: 0.0

        val totalPagar = cantidad * precio
        txtTotalPagarCompra.setText(totalPagar.toString())

        mostrar(pago)
    }

    fun mostrarAlerta(mensaje: String) {
        AlertDialog.Builder(this)
            .setTitle("SISTEMA")
            .setMessage(mensaje)
            .setPositiveButton("Aceptar") { dialog, _ ->
                dialog.dismiss()
            }
            .show()
    }

    fun regresar() {
        val intent = Intent(this, ListaCompraActivity::class.java)
        startActivity(intent)
    }

    fun mostrar(tipoPago: String) {
        val builder = AlertDialog.Builder(this)

        val linearLayout = LinearLayout(this)
        linearLayout.orientation = LinearLayout.VERTICAL

        val titleView = TextView(this)
        titleView.text = "Confirmación de Pago"
        titleView.gravity = Gravity.CENTER
        titleView.textSize = 20f
        titleView.setPadding(0, 20, 0, 20)
        titleView.setTypeface(null, Typeface.BOLD)

        val imageView = ImageView(this)
        val layoutParamsImage = LinearLayout.LayoutParams(
            LinearLayout.LayoutParams.MATCH_PARENT,
            500
        )
        imageView.layoutParams = layoutParamsImage
        imageView.scaleType = ImageView.ScaleType.FIT_CENTER



        when (tipoPago.lowercase()) {
            "yape" -> {
                imageView.setImageResource(R.drawable.yape)
            }
            "plin", "bcp" -> {
                imageView.setImageResource(R.drawable.bcp)
            }
            else -> {
                builder.setMessage("Tipo de pago no reconocido.")
                imageView.visibility = View.GONE
            }
        }

        linearLayout.addView(titleView)
        if (imageView.visibility != View.GONE) {
            linearLayout.addView(imageView)
        }

        builder.setView(linearLayout)

        builder.setPositiveButton("Aceptar") { dialog, _ ->
            dialog.dismiss()
        }

        val dialog: AlertDialog = builder.create()
        dialog.show()
    }

}