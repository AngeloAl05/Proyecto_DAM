package com.example.proyecto.controller

import android.content.ContentValues
import com.example.proyecto.entidad.Carrito
import com.example.proyecto.entidad.Producto_carrito
import com.example.proyecto.utils.AppConfig

class CarritoController {

    fun findAll(): ArrayList<Carrito> {
        val lista = ArrayList<Carrito>()
        val CN = AppConfig.BD.readableDatabase
        val sql = "SELECT * FROM tb_carrito"
        val RS = CN.rawQuery(sql, null)
        while (RS.moveToNext()) {
            val bean = Carrito(
                RS.getInt(0),     // id
                RS.getDouble(1)   // total
            )
            lista.add(bean)
        }
        RS.close()
        return lista
    }

    fun save(doc: Carrito): Int {
        val CN = AppConfig.BD.writableDatabase
        val content = ContentValues()
        content.put("total", doc.total)
        return CN.insert("tb_carrito", "idcarrito", content).toInt()
    }

    fun findById(id: Int): Carrito? {
        val CN = AppConfig.BD.readableDatabase
        val sql = "SELECT * FROM tb_carrito WHERE idcarrito=?"
        val RS = CN.rawQuery(sql, arrayOf(id.toString()))
        var bean: Carrito? = null
        if (RS.moveToFirst()) {
            bean = Carrito(
                RS.getInt(0),
                RS.getDouble(1)
            )
        }
        RS.close()
        return bean
    }

    fun update(doc: Carrito): Int {
        val CN = AppConfig.BD.writableDatabase
        val content = ContentValues()
        content.put("total", doc.total)
        return CN.update("tb_carrito", content, "idcarrito=?", arrayOf(doc.idCarrito.toString()))
    }

    fun deleteById(id: Int): Int {
        val CN = AppConfig.BD.writableDatabase
        return CN.delete("tb_carrito", "idcarrito=?", arrayOf(id.toString()))
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