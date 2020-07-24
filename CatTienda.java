package com.desoft.lastrayopedidos.objetos;

public class CatTienda {
    private String categoriaProducto;
    private String empresa;

    public CatTienda() {
    }

    public CatTienda(String categoriaProducto, String empresa) {
        this.categoriaProducto = categoriaProducto;
        this.empresa = empresa;
    }

    public String getCategoriaProducto() {
        return categoriaProducto;
    }

    public void setCategoriaProducto(String categoriaProducto) {
        this.categoriaProducto = categoriaProducto;
    }

    public String getEmpresa() {
        return empresa;
    }

    public void setEmpresa(String empresa) {
        this.empresa = empresa;
    }
}
