package com.example.proyecto.controller

import android.content.ContentValues
import com.example.proyecto.entidad.Proveedor
import com.example.proyecto.utils.AppConfig

class ProveedorController {
    fun findAll():ArrayList<Proveedor>{
        var lista=ArrayList<Proveedor>()
        var CN= AppConfig.BD.readableDatabase
        var sql="select *from tb_proveedor"
        var RS=CN.rawQuery(sql,null)
        while(RS.moveToNext()){
            var bean=Proveedor(RS.getInt(0),
                RS.getString(1),
                RS.getString(2),
                RS.getString(3),
                RS.getString(4))
            lista.add(bean)
        }
        RS.close()
        return lista
    }
    fun save(doc:Proveedor):Int{
        var salida=-1;
        var CN=AppConfig.BD.writableDatabase
        var content= ContentValues()
        content.put("nom",doc.nom)
        content.put("dir",doc.dir)
        content.put("telefono",doc.telefono)
        content.put("correo",doc.correo)
        salida=CN.insert("tb_proveedor","cod",content).toInt()
        return salida
    }
    fun findById(codigo:Int):Proveedor{
        lateinit var bean:Proveedor
        var CN=AppConfig.BD.readableDatabase
        var sql="select *from tb_proveedor where cod=?"
        var RS=CN.rawQuery(sql, arrayOf(codigo.toString()))
        if(RS.moveToNext()){
            bean= Proveedor(RS.getInt(0),RS.getString(1),
                RS.getString(2),RS.getString(3),
                RS.getString(4))
        }
        RS.close()
        return bean
    }
    fun update(doc:Proveedor):Int{
        var salida=-1;
        var CN=AppConfig.BD.writableDatabase
        var content=ContentValues()
        content.put("nom",doc.nom)
        content.put("dir",doc.dir)
        content.put("telefono",doc.telefono)
        content.put("correo",doc.correo)
        salida=CN.update("tb_proveedor",content,"cod=?", arrayOf(doc.cod.toString()))
        return salida
    }
    fun deleteById(cod:Int):Int{
        var salida=-1;
        var CN=AppConfig.BD.writableDatabase
        salida=CN.delete("tb_proveedor","cod=?", arrayOf(cod.toString()))
        return salida
    }
}