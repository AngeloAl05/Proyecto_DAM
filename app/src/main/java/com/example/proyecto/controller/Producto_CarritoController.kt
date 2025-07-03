package com.example.proyecto.controller

import android.content.ContentValues
import com.example.proyecto.entidad.Carrito
import com.example.proyecto.entidad.Producto_carrito
import com.example.proyecto.entidad.Producto_carrito_detalle
import com.example.proyecto.utils.AppConfig

class Producto_CarritoController {
    fun findAll(): ArrayList<Producto_carrito> {
        val lista = ArrayList<Producto_carrito>()
        val CN = AppConfig.BD.readableDatabase
        val sql = "SELECT * FROM tb_producto_carrito"
        val RS = CN.rawQuery(sql, null)
        while (RS.moveToNext()) {
            val bean = Producto_carrito(
                RS.getInt(0),     // id
                RS.getInt(1),
                RS.getInt(2),
                RS.getInt(3),
                RS.getDouble(4)   // total
            )
            lista.add(bean)
        }
        RS.close()
        return lista
    }

    fun save(doc: Producto_carrito): Int {
        val CN = AppConfig.BD.writableDatabase
        val content = ContentValues()
        content.put("idproducto", doc.idproducto)
        content.put("idcarrito", doc.idcarrito)
        content.put("cantidad", doc.cantidad)
        content.put("subtotal", doc.subtotal)
        return CN.insert("tb_producto_carrito", "id", content).toInt()
    }

    fun findById(id: Int): Producto_carrito? {
        val CN = AppConfig.BD.readableDatabase
        val sql = "SELECT * FROM tb_producto_carrito WHERE id=?"
        val RS = CN.rawQuery(sql, arrayOf(id.toString()))
        var bean: Producto_carrito? = null
        if (RS.moveToFirst()) {
            bean = Producto_carrito(
                RS.getInt(0),
                RS.getInt(1),
                RS.getInt(2),
                RS.getInt(3),
                RS.getDouble(4)
            )
        }
        RS.close()
        return bean
    }

    fun update(doc: Producto_carrito): Int {
        val CN = AppConfig.BD.writableDatabase
        val content = ContentValues()
        content.put("idproducto", doc.idproducto)
        content.put("idcarrito", doc.idcarrito)
        content.put("cantidad", doc.cantidad)
        content.put("subtotal", doc.subtotal)
        return CN.update("tb_producto_carrito", content, "id=?", arrayOf(doc.id.toString()))
    }

    fun deleteById(id: Int): Int {
        val CN = AppConfig.BD.writableDatabase
        return CN.delete("tb_producto_carrito", "id=?", arrayOf(id.toString()))
    }

    fun listarProductosDeCarrito(idCarrito: Int): ArrayList<Producto_carrito_detalle> {
        val lista = ArrayList<Producto_carrito_detalle>()
        val CN = AppConfig.BD.readableDatabase

        val sql = """
        SELECT  p.nom, p.pre, pc.cantidad, p.foto
        FROM tb_producto_carrito pc
        INNER JOIN tb_producto p ON pc.idproducto = p.cod
        WHERE pc.idcarrito = ?
    """.trimIndent()

        val RS = CN.rawQuery(sql, arrayOf(idCarrito.toString()))

        while (RS.moveToNext()) {
            val item = Producto_carrito_detalle(
                RS.getString(0),
                RS.getDouble(1),
                RS.getInt(2),
                RS.getString(3)
            )
            lista.add(item)
        }
        RS.close()
        return lista
    }

    /*fun findByProducto(idProducto: Int): ArrayList<Carrito> {
        val lista = ArrayList<Carrito>()
        val CN = AppConfig.BD.readableDatabase
        val query = "SELECT * FROM tb_carrito WHERE idproducto=?"
        val RS = CN.rawQuery(query, arrayOf(idProducto.toString()))
        while (RS.moveToNext()) {
            val bean = Carrito(
                RS.getInt(0),
                RS.getDouble(1)
            )
            lista.add(bean)
        }
        RS.close()
        return lista
    }*/
}