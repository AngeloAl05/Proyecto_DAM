package com.example.proyecto.controller

import android.content.ContentValues
import com.example.proyecto.entidad.Categoria
import com.example.proyecto.utils.AppConfig

class CategoriaController {
    fun findAll(): ArrayList<Categoria> {
        val lista = ArrayList<Categoria>()
        val CN = AppConfig.BD.readableDatabase
        val sql = "SELECT * FROM tb_categoria"
        val RS = CN.rawQuery(sql, null)
        while (RS.moveToNext()) {
            val bean = Categoria(
                RS.getInt(0),     // idCategoria
                RS.getString(1),  // nombreCate
                RS.getString(2),  // descripcion
                RS.getInt(3)      // estado
            )
            lista.add(bean)
        }
        RS.close()
        return lista
    }

    fun findById(codigo: Int): Categoria? {
        val CN = AppConfig.BD.readableDatabase
        val sql = "SELECT * FROM tb_categoria WHERE idcat=?"
        val RS = CN.rawQuery(sql, arrayOf(codigo.toString()))
        var bean: Categoria? = null
        if (RS.moveToFirst()) {
            bean = Categoria(
                RS.getInt(0),
                RS.getString(1),
                RS.getString(2),
                RS.getInt(3)
            )
        }
        RS.close()
        return bean
    }

    fun save(doc: Categoria): Int {
        val CN = AppConfig.BD.writableDatabase
        val content = ContentValues()
        content.put("nom", doc.nombreCate)
        content.put("des", doc.descripcion)
        content.put("estado", doc.estado)
        return CN.insert("tb_categoria", null, content).toInt()
    }

    fun update(doc: Categoria): Int {
        val CN = AppConfig.BD.writableDatabase
        val content = ContentValues()
        content.put("nom", doc.nombreCate)
        content.put("des", doc.descripcion)
        content.put("estado", doc.estado)
        return CN.update("tb_categoria", content, "idcat=?", arrayOf(doc.idCategoria.toString()))
    }

    fun deleteById(cod: Int): Int {
        val CN = AppConfig.BD.writableDatabase
        return CN.delete("tb_categoria", "idcat=?", arrayOf(cod.toString()))
    }

    fun findByNombre(nombre: String): ArrayList<Categoria> {
        val lista = ArrayList<Categoria>()
        val CN = AppConfig.BD.readableDatabase
        val query = "SELECT * FROM tb_categoria WHERE nom LIKE ?"
        val cursor = CN.rawQuery(query, arrayOf("$nombre%"))
        if (cursor.moveToFirst()) {
            do {
                val categoria = Categoria(
                    cursor.getInt(0),
                    cursor.getString(1),
                    cursor.getString(2),
                    cursor.getInt(3)
                )
                lista.add(categoria)
            } while (cursor.moveToNext())
        }
        cursor.close()
        return lista
    }
}