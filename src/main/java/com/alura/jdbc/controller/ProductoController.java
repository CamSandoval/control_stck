package com.alura.jdbc.controller;

import com.alura.jdbc.factory.ConnectionFactory;

import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ProductoController {

	public int modificar(String nombre, String descripcion, Integer id,Integer cantidad) throws SQLException {
		Connection con = ConnectionFactory.recuperarConexion();

		Statement statement = con.createStatement();
		String query = "UPDATE producto SET nombre ='"+nombre+"' , descripcion = '"+ descripcion+ "', cantidad = "+ cantidad + " WHERE id ="+ id + " ;";
		System.out.println(query);
		statement.execute(query);
		int updateCount = statement.getUpdateCount();
		con.close();
		return updateCount;
	}

	public int eliminar(Integer id) throws SQLException {
		Connection con = ConnectionFactory.recuperarConexion();

		Statement statement = con.createStatement();
		statement.execute("DELETE FROM producto WHERE id="+id);

		//Este metodo retorna cuantos registros fuern modificados en la base de datos
		return statement.getUpdateCount();
	}

	public List<Map<String,String>> listar() throws SQLException {
		Connection con = ConnectionFactory.recuperarConexion();

		//Los objetos Statement son los encargados de ejecutar las querys de nuestras base de datos, para
		// crearlos no es necesario instanciasrlos, pero si es necesario crarlo a partir de un objeto Connection
		Statement statement = con.createStatement();
		//Ejecucion del quiery
		boolean result = statement.execute("SELECT * FROM producto");
		//El objeto resultSet es el encargado de recibir el resulta de la query en la db, con este podremos obtener los strings de el resultado
		ResultSet resultSet = statement.getResultSet();

		//Creamos una lista que nos reciba el resultdo de la quuery en una forma en la que pueda ser manipulado y retornado para mostar en la app
		List<Map<String,String>> resultados = new ArrayList<>();
		while (resultSet.next()){
			Map<String,String> fila = new HashMap<>();
			fila.put("ID", String.valueOf(resultSet.getInt("id")));
			fila.put("NOMBRE", resultSet.getString("nombre"));
			fila.put("DESCRIPCION", resultSet.getString("descripcion"));
			fila.put("CANTIDAD", String.valueOf(resultSet.getInt("cantidad")));
			resultados.add(fila);
		}
		con.close();
		return resultados;
	}

    public void guardar(Map<String ,String> producto) throws SQLException {
		Connection con = ConnectionFactory.recuperarConexion();

		Statement statement = con.createStatement();
		statement.execute("INSERT INTO producto(nombre,descripcion,cantidad) " +
								"VALUES('"+producto.get("NOMBRE")+"','"
								+producto.get("DESCRIPCION")+"',"
								+producto.get("CANTIDAD")+");"
				//Este metodo estatico de la clase statement nos permite obtener el id de una consulta cuando se insertan valores a una tabla
				,statement.RETURN_GENERATED_KEYS);

		ResultSet resultSet = statement.getGeneratedKeys();
		while (resultSet.next()){
			System.out.println(String.format("Fue insertado el producto de ID %d",
			resultSet.getInt(1)));
		}
	}

}
