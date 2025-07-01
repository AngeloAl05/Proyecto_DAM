package com.example.proyecto.entidad

class Producto(
    var idProducto: Int = 0,
    var nombreProduc: String = "",
    var cantidadProduc: Int = 0,
    var precioProduc: Double = 0.0,
    var unidadesProduc: Int = 0,
    var idCategoria: String = "",
    var foto: String = ""
) {

    constructor() : this(0, "", 0, 0.0, 0, "", "")
}
