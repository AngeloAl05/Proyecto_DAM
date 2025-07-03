import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.proyecto.ProductoActualizarActivity
import com.example.proyecto.controller.CarritoController
import com.example.proyecto.R
import com.example.proyecto.adaptador.ViewProducto
import com.example.proyecto.controller.ProductoController
import com.example.proyecto.entidad.Carrito
import com.example.proyecto.entidad.Producto
import com.example.proyecto.utils.AppConfig
import com.google.firebase.database.*

class ProductoAdapter(
    private val lista: ArrayList<Producto>,
    private val showButtons: Boolean,
    private val onDetailClick: (Producto) -> Unit
) : RecyclerView.Adapter<ViewProducto>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewProducto {
        val inflater = LayoutInflater.from(parent.context)
        val itemView = inflater.inflate(R.layout.item_lista_productos, parent, false)
        return ViewProducto(itemView)
    }

    override fun getItemCount(): Int {
        return lista.size
    }

    override fun onBindViewHolder(holder: ViewProducto, position: Int) {
        val producto = lista[position]
        holder.txtCodigoProd.text = producto.idProducto.toString()
        holder.txtNombreProd.text = producto.nombreProduc
        holder.txtCategoriaProd.text = ProductoController.obtenerNombreCategoriaPorId(producto.idCategoria)

        if (showButtons) {
            holder.btnDetallesProd.visibility = View.VISIBLE
            holder.btnEditarProd.visibility = View.VISIBLE
            holder.btnCompraProd.visibility = View.GONE
        } else {
            holder.btnDetallesProd.visibility = View.VISIBLE
            holder.btnEditarProd.visibility = View.GONE
        }
        holder.btnDetallesProd.setOnClickListener {
            onDetailClick(producto)
        }
        holder.btnCompraProd.setOnClickListener {
            mostrarDialogoCompra(holder.itemView.context, producto)
        }
        val context = holder.itemView.context
        val resourceId = context.resources.getIdentifier(producto.foto, "drawable", context.packageName)
        if (resourceId != 0) {
            holder.ImgFoto.setImageResource(resourceId)
        } else {
            0
        }
        holder.btnEditarProd.setOnClickListener {
            val intent = Intent(AppConfig.CONTEXTO, ProductoActualizarActivity::class.java)
            intent.putExtra("codigo", producto.idProducto)
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
            ContextCompat.startActivity(AppConfig.CONTEXTO, intent, null)
        }

    }

    fun mostrarDialogoCompra(context: Context, producto: Producto) {
        val mensaje = "¿Agregar ${producto.nombreProduc} al carrito?"

        AlertDialog.Builder(context)
            .setTitle("Confirmar")
            .setMessage(mensaje)
            .setPositiveButton("Agregar") { dialog, _ ->
                añadirProductoACarrito(context, producto)
            }
            .setNegativeButton("Cancelar") { dialog, _ ->
                dialog.dismiss()
            }
            .show()
    }

    fun añadirProductoACarrito(context: Context, producto: Producto) {
        val controller = CarritoController()
        val carritoExistente = controller.findByProducto(producto.idProducto)

        if (carritoExistente.isNotEmpty()) {
            // Ya existe, actualizamos cantidad y total
            val existente = carritoExistente[0]
            val nuevaCantidad = existente.cantidad + 1
            val nuevoTotal = nuevaCantidad * producto.precioProduc

            existente.cantidad = nuevaCantidad
            existente.total = nuevoTotal

            val actualizado = controller.update(existente)
            if (actualizado > 0) {
                mostrarDialogoExito(context, producto, nuevaCantidad, producto.foto)
            } else {
                mostrarDialogoError(context)
            }
        } else {
            // Nuevo producto en el carrito
            val nuevo = com.example.proyecto.entidad.Compra(0, producto.idProducto, 1, producto.precioProduc)
            val insertado = controller.save(nuevo)
            if (insertado != -1) {
                mostrarDialogoExito(context, producto, 1, producto.foto)
            } else {
                mostrarDialogoError(context)
            }
        }
    }


    fun mostrarDialogoExito(context: Context, producto: Producto, cantidad: Int, foto: String) {
        val mensajeExito = "${producto.nombreProduc} agregado al carrito (Cantidad: $cantidad)"
        AlertDialog.Builder(context)
            .setTitle("Éxito")
            .setMessage(mensajeExito)
            .setPositiveButton("OK") { _, _ -> }
            .show()
    }

    fun mostrarDialogoError(context: Context) {
        AlertDialog.Builder(context)
            .setTitle("Error")
            .setMessage("No se pudo añadir el producto al carrito.")
            .setPositiveButton("OK") { _, _ -> }
            .show()
    }
}
