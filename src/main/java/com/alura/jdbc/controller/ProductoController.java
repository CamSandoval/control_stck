package com.alura.jdbc.controller;

import com.alura.jdbc.factory.ConnectionFactory;
import com.alura.jdbc.modelo.Categoria;
import com.alura.jdbc.modelo.Producto;
import com.alura.jdbc.DAO.ProductoDAO;

import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ProductoController {

	private ProductoDAO productoDAO;

	public ProductoController(){
		this.productoDAO = new ProductoDAO(new ConnectionFactory().recuperarConexion());
	}

	public int modificar(String nombre, String descripcion, Integer id,Integer cantidad) {
		return productoDAO.modificar(nombre,descripcion,id,cantidad);
	}

	public int eliminar(Integer id) {
		return this.productoDAO.eliminar(id);
	}

	public List<Producto> listar(){
		return this.productoDAO.listar();
	}
	public List<Producto> listar(Categoria categoria){
		return productoDAO.listar(categoria.getId());
	}

    public void guardar(Producto producto,Integer categoriaId){
		producto.setCategoriaId(categoriaId);
		this.productoDAO.guardar(producto);
	}

}
