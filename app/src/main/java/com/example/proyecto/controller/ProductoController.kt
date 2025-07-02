package com.example.proyecto.controller

import android.content.ContentValues
import android.database.Cursor
import com.example.proyecto.entidad.Producto
import com.example.proyecto.utils.AppConfig

class ProductoController {

    fun findAll():ArrayList<Producto>{
        var lista=ArrayList<Producto>()
        var CN= AppConfig.BD.readableDatabase
        var sql="select *from tb_producto"
        var RS=CN.rawQuery(sql,null)
        while(RS.moveToNext()){
            var bean=Producto(RS.getInt(0),
                RS.getString(1),
                RS.getInt(2),
                RS.getDouble(3),
                RS.getInt(4),
                RS.getInt(5),
                RS.getString(6))
            lista.add(bean)
        }
        return lista
    }
    fun save(doc:Producto):Int{
        var salida=-1;
        var CN=AppConfig.BD.writableDatabase
        var content= ContentValues()
        content.put("nom",doc.nombreProduc)
        content.put("can",doc.cantidadProduc)
        content.put("pre",doc.precioProduc)
        content.put("unidad",doc.unidadesProduc)
        content.put("idcat",doc.idCategoria)
        content.put("foto",doc.foto)
        salida=CN.insert("tb_producto","cod",content).toInt()
        return salida
    }
    fun findById(codigo:Int):Producto{
        lateinit var bean:Producto
        var CN=AppConfig.BD.readableDatabase
        var sql="select *from tb_producto where cod=?"
        var RS=CN.rawQuery(sql, arrayOf(codigo.toString()))
        if(RS.moveToNext()){
            bean= Producto(RS.getInt(0),RS.getString(1),
                RS.getInt(2),RS.getDouble(3),
                RS.getInt(4),RS.getInt(5),
                RS.getString(6))
        }
        return bean
    }
    fun update(doc:Producto):Int{
        var salida=-1;
        var CN=AppConfig.BD.writableDatabase
        var content=ContentValues()
        content.put("nom",doc.nombreProduc)
        content.put("can",doc.cantidadProduc)
        content.put("pre",doc.precioProduc)
        content.put("unidad",doc.unidadesProduc)
        content.put("idcat",doc.idCategoria)
        content.put("foto",doc.foto)
        salida=CN.update("tb_producto",content,"cod=?", arrayOf(doc.idProducto.toString()))
        return salida
    }

    fun deleteById(cod:Int):Int{
        var salida=-1;
        var CN=AppConfig.BD.writableDatabase
        salida=CN.delete("tb_producto","cod=?", arrayOf(cod.toString()))
        return salida
    }
    fun findByNombre(nombre: String): ArrayList<Producto> {
        var lista = ArrayList<Producto>()
        var CN = AppConfig.BD.readableDatabase
        var query = "select * from tb_producto where nom like?"
        var cursor: Cursor = CN.rawQuery(query, arrayOf("$nombre%"))
        if (cursor.moveToFirst()) {
            do {

                var produc = Producto(
                    cursor.getInt(0),
                    cursor.getString(1),
                    cursor.getInt(2),
                    cursor.getDouble(3),
                    cursor.getInt(4),
                    cursor.getInt(5),
                    cursor.getString(6)
                )
                lista.add(produc)
            } while (cursor.moveToNext())
        }
        cursor.close()
        return lista
    }
    companion object {
        fun obtenerNombreCategoriaPorId(id: Int): String {
            val db = AppConfig.BD.readableDatabase
            val cursor = db.rawQuery("SELECT nom FROM tb_categoria WHERE idcat = ?", arrayOf(id.toString()))
            var nombre = "Desconocido"
            if (cursor.moveToFirst()) {
                nombre = cursor.getString(0)
            }
            cursor.close()
            return nombre
        }
    }
}