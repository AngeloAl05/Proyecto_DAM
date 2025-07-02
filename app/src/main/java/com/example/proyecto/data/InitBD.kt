package com.example.proyecto.data

import android.annotation.SuppressLint
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import com.example.proyecto.utils.AppConfig

class InitBD:SQLiteOpenHelper(AppConfig.CONTEXTO,
    AppConfig.NOMBREBD,
    null,
    AppConfig.VERSION) {
    override fun onCreate(db: SQLiteDatabase) {
        db.execSQL("""create table tb_proveedor (
                      cod integer primary key autoincrement,
                      nom varchar(30),
                      dir varchar(50),
                      telefono varchar(30),
                      correo varchar(30)) """.trimMargin())


        db.execSQL("""create table tb_categoria (
                      idcat INTEGER primary key autoincrement,
                      nom varchar(50)
                    )""".trimIndent())

        db.execSQL("""create table tb_producto ( 
                     cod integer primary key autoincrement,
                     nom varchar(50),
                     can varchar(50),
                     pre double,
                     unidad int,
                     idcat varchar(50),
                     foto varchar(20))
   
                   """.trimMargin())

        db.execSQL("INSERT INTO tb_categoria VALUES (null, 'Bebidas')")
        db.execSQL("INSERT INTO tb_categoria VALUES (null, 'Galletas')")
        db.execSQL("INSERT INTO tb_categoria VALUES (null, 'Pasteles')")
        db.execSQL("INSERT INTO tb_categoria VALUES (null, 'Snacks')")
        db.execSQL("INSERT INTO tb_categoria VALUES (null, 'Golosinas')")

        db.execSQL("insert into tb_proveedor values(null,'Celeste','Mz M Lote 2','9985748723','celesterosas@gmail.com')")
        db.execSQL("insert into tb_proveedor values(null,'Cristina','Mz L Lote 5','987452672','cristina@gmail.com')")
        //
        /*db.execSQL("INSERT INTO tb_producto VALUES (null, 'Tres Leches', '4', 65.00, 2, 'Pasteles', 'tresleches')");
        db.execSQL("INSERT INTO tb_producto VALUES (null, 'Inka Cola', '4', 3.50, 3, 'Bebidas', 'inkacola')");
        db.execSQL("INSERT INTO tb_producto VALUES (null, 'Lays', '2', 2.50, 5, 'Snacks', 'lays')");
        db.execSQL("INSERT INTO tb_producto VALUES (null, 'Oreo', '1', 4.50, 7, 'Galletas', 'oreo')");
        db.execSQL("INSERT INTO tb_producto VALUES (null, 'Coca Cola', '3', 3.00, 4, 'Bebidas', 'cocacola')");
        db.execSQL("INSERT INTO tb_producto VALUES (null, 'Doritos', '5', 2.75, 6, 'Snacks', 'doritos')");
        db.execSQL("INSERT INTO tb_producto VALUES (null, 'Moka', '2', 6.00, 1, 'Café', 'moka')");
        db.execSQL("INSERT INTO tb_producto VALUES (null, 'Ole Ole', '3', 1.50, 8, 'Snacks', 'oleole')");
        db.execSQL("INSERT INTO tb_producto VALUES (null, 'Selva Negra', '4', 70.00, 9, 'Pasteles', 'selvanegra')");
        db.execSQL("INSERT INTO tb_producto VALUES (null, 'Rellenita', '1', 3.00, 10, 'Galletas', 'rellenita')");*/

        db.execSQL("INSERT INTO tb_producto VALUES (null, 'Tres Leches', '4', 65.00, 3, 'tresleches')")    // Pasteles → ID 3
        db.execSQL("INSERT INTO tb_producto VALUES (null, 'Inka Cola', '4', 3.50, 1, 'inkacola')")         // Bebidas → ID 1
        db.execSQL("INSERT INTO tb_producto VALUES (null, 'Lays', '2', 2.50, 4, 'lays')")                  // Snacks → ID 4
        db.execSQL("INSERT INTO tb_producto VALUES (null, 'Oreo', '1', 4.50, 2, 'oreo')")                  // Galletas → ID 2
        db.execSQL("INSERT INTO tb_producto VALUES (null, 'Coca Cola', '3', 3.00, 1, 'cocacola')")         // Bebidas → ID 1
        db.execSQL("INSERT INTO tb_producto VALUES (null, 'Doritos', '5', 2.75, 4, 'doritos')")            // Snacks → ID 4
        db.execSQL("INSERT INTO tb_producto VALUES (null, 'Moka', '2', 6.00, 1, 'moka')")                  // Café → NO EXISTE en la tabla, ⚠️ Cuidado
        db.execSQL("INSERT INTO tb_producto VALUES (null, 'Ole Ole', '3', 1.50, 4, 'oleole')")             // Snacks → ID 4
        db.execSQL("INSERT INTO tb_producto VALUES (null, 'Selva Negra', '4', 70.00, 3, 'selvanegra')")    // Pasteles → ID 3
        db.execSQL("INSERT INTO tb_producto VALUES (null, 'Rellenita', '1', 3.00, 2, 'rellenita')")

    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        TODO("Not yet implemented")
    }

    @SuppressLint("Range")
    fun verificarTipoUsuario(usuario: String): String {
        val db = readableDatabase
        var tipoUsuario = ""
        val query = "SELECT ${AppConfig.Tipo} FROM ${AppConfig.Usuario} WHERE ${AppConfig.Username} = ?"

        val cursor = db.rawQuery(query, arrayOf(usuario))
        if (cursor.moveToFirst()) {
            tipoUsuario = cursor.getString(cursor.getColumnIndex(AppConfig.Tipo))
        }
        cursor.close()
        return tipoUsuario
    }

}