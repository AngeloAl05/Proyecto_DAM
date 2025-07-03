import android.content.Context
import android.content.DialogInterface
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.RecyclerView
import com.example.proyecto.R
import com.example.proyecto.adaptador.ViewCompra
import com.example.proyecto.entidad.Carrito
import com.example.proyecto.entidad.Producto_carrito_detalle
import com.google.firebase.database.DatabaseReference

class CompraAdapter(
    private val lista: ArrayList<Producto_carrito_detalle>,
    private val database: DatabaseReference,
    private val onDeleteSuccess: () -> Unit
) : RecyclerView.Adapter<ViewCompra>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewCompra {
        val inflater = LayoutInflater.from(parent.context)
        val itemView = inflater.inflate(R.layout.item_carrito_compras, parent, false)
        return ViewCompra(itemView)
    }

    override fun getItemCount(): Int {
        return lista.size
    }

    override fun onBindViewHolder(holder: ViewCompra, position: Int) {
        val productosDeCarrito = lista[position]
        holder.txtNombreCarrito.text = productosDeCarrito.nombreProducto
        holder.txtPrecio.text = productosDeCarrito.precio.toString()
        holder.txtCantidad.text = productosDeCarrito.cantidad.toString()

        holder.btnEliminarProdCarrito.setOnClickListener {
            showAlertEliminar("¿Está seguro de eliminar el producto: ${productosDeCarrito.nombreProducto}?", position, holder.itemView.context)
        }
        val context = holder.itemView.context
        val resourceId = context.resources.getIdentifier(productosDeCarrito.foto, "drawable", context.packageName)
        if (resourceId != 0) {
            holder.ImgCarrito.setImageResource(resourceId)
        } else {
            0
        }
    }

    fun eliminarProducto(position: Int) {
        lista.removeAt(position)
        notifyDataSetChanged()
        onDeleteSuccess()
    }

    fun showAlertEliminar(mensaje: String, position: Int, context: Context) {
        val builder = AlertDialog.Builder(context)
        builder.setTitle("Confirmación")
        builder.setMessage(mensaje)
        builder.setPositiveButton("Aceptar") { dialogInterface: DialogInterface, i: Int ->
            eliminarProducto(position)
        }
        builder.setNegativeButton("Cancelar", null)
        val dialog: AlertDialog = builder.create()
        dialog.show()
    }
}
