package com.example.proyecto.controller

import android.content.ContentValues
import com.example.proyecto.entidad.Categoria
import com.example.proyecto.utils.AppConfig

class CategoriaController {
    fun findAll(): ArrayList<Categoria> {
        var lista = ArrayList<Categoria>()
        var CN = AppConfig.BD.readableDatabase
        var sql = "SELECT * FROM tb_categoria"
        var RS = CN.rawQuery(sql, null)
        while (RS.moveToNext()) {
            val bean = Categoria(
                RS.getInt(0),     // idCategoria
                RS.getString(1)  // nombreCate
            )
            lista.add(bean)
        }
        RS.close()
        return lista
    }


    fun save(doc: Categoria): Int {
        var salida=-1;
        val CN = AppConfig.BD.writableDatabase
        val content = ContentValues()
        content.put("nom", doc.nombreCate)
        salida = CN.insert("tb_categoria", "idcat", content).toInt()
        return salida
    }

    fun findById(codigo: Int): ArrayList<Categoria> {
        val lista = ArrayList<Categoria>()
        val CN = AppConfig.BD.readableDatabase
        val sql = "SELECT * FROM tb_categoria WHERE idcat=?"
        val RS = CN.rawQuery(sql, arrayOf(codigo.toString()))
        if (RS.moveToFirst()) {
            val bean = Categoria(
                RS.getInt(0),     // idCategoria
                RS.getString(1)
            )
            lista.add(bean)
        }
        RS.close()
        return lista
    }


    fun update(doc: Categoria): Int {
        val CN = AppConfig.BD.writableDatabase
        val content = ContentValues()
        content.put("nom", doc.nombreCate)
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
                    cursor.getString(1)
                )
                lista.add(categoria)
            } while (cursor.moveToNext())
        }
        cursor.close()
        return lista
    }
}