package com.example.proyecto.controller

class CarritoController {

    fun findAll(): ArrayList<Compra> {
        val lista = ArrayList<Compra>()
        val CN = AppConfig.BD.readableDatabase
        val sql = "SELECT * FROM tb_carrito"
        val RS = CN.rawQuery(sql, null)
        while (RS.moveToNext()) {
            val bean = Compra(
                RS.getInt(0),     // idcarrito
                RS.getInt(1),     // idproducto
                RS.getInt(2),     // cantidad
                RS.getDouble(3)   // total
            )
            lista.add(bean)
        }
        RS.close()
        return lista
    }

    fun save(doc: Compra): Int {
        val CN = AppConfig.BD.writableDatabase
        val content = ContentValues()
        content.put("idproducto", doc.idProducto)
        content.put("cantidad", doc.cantidad)
        content.put("total", doc.total)
        return CN.insert("tb_carrito", "idcarrito", content).toInt()
    }

    fun findById(id: Int): Compra? {
        val CN = AppConfig.BD.readableDatabase
        val sql = "SELECT * FROM tb_carrito WHERE idcarrito=?"
        val RS = CN.rawQuery(sql, arrayOf(id.toString()))
        var bean: Compra? = null
        if (RS.moveToFirst()) {
            bean = Compra(
                RS.getInt(0),
                RS.getInt(1),
                RS.getInt(2),
                RS.getDouble(3)
            )
        }
        RS.close()
        return bean
    }

    fun update(doc: Compra): Int {
        val CN = AppConfig.BD.writableDatabase
        val content = ContentValues()
        content.put("idproducto", doc.idProducto)
        content.put("cantidad", doc.cantidad)
        content.put("total", doc.total)
        return CN.update("tb_carrito", content, "idcarrito=?", arrayOf(doc.idCarrito.toString()))
    }

    fun deleteById(id: Int): Int {
        val CN = AppConfig.BD.writableDatabase
        return CN.delete("tb_carrito", "idcarrito=?", arrayOf(id.toString()))
    }

    fun findByProducto(idProducto: Int): ArrayList<Compra> {
        val lista = ArrayList<Compra>()
        val CN = AppConfig.BD.readableDatabase
        val query = "SELECT * FROM tb_carrito WHERE idproducto=?"
        val RS = CN.rawQuery(query, arrayOf(idProducto.toString()))
        while (RS.moveToNext()) {
            val bean = Compra(
                RS.getInt(0),
                RS.getInt(1),
                RS.getInt(2),
                RS.getDouble(3)
            )
            lista.add(bean)
        }
        RS.close()
        return lista
    }
}