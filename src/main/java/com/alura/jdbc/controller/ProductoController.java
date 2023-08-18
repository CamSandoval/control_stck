package com.alura.jdbc.controller;

import com.alura.jdbc.factory.ConnectionFactory;
import com.alura.jdbc.modelo.Producto;

import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ProductoController {

	public int modificar(String nombre, String descripcion, Integer id,Integer cantidad) throws SQLException {
		final Connection con = new ConnectionFactory().recuperarConexion();

		try(con){
			final PreparedStatement statement = con.prepareStatement("UPDATE producto SET nombre = ? , descripcion = ? "+
					" , cantidad = ? WHERE id = ?;");

			try(statement){
				statement.setString(1, nombre);
				statement.setString(2, descripcion);
				statement.setInt(3, cantidad);
				statement.setInt(4, id);

				statement.execute();
				int updateCount = statement.getUpdateCount();
				con.close();
				return updateCount;
			}
		}
	}

	public int eliminar(Integer id) throws SQLException {
		final Connection con = new ConnectionFactory().recuperarConexion();

		try (con) {
			final PreparedStatement statement = con.prepareStatement("DELETE FROM producto WHERE id= ?");

			try(statement) {
				statement.setInt(1, id);
				statement.execute();

				//Este metodo retorna cuantos registros fuern modificados en la base de datos
				return statement.getUpdateCount();
			}
		}
	}

	public List<Map<String,String>> listar() throws SQLException {
		final Connection con = new ConnectionFactory().recuperarConexion();

		try(con) {
			//Los objetos Statement son los encargados de ejecutar las querys de nuestras base de datos, para
			// crearlos no es necesario instanciasrlos, pero si es necesario crarlo a partir de un objeto Connection
			final PreparedStatement statement = con.prepareStatement("SELECT * FROM producto");

			try(statement) {
				//Ejecucion del quiery
				statement.execute();
				//El objeto resultSet es el encargado de recibir el resulta de la query en la db, con este podremos obtener los strings de el resultado
				ResultSet resultSet = statement.getResultSet();

				//Creamos una lista que nos reciba el resultdo de la quuery en una forma en la que pueda ser manipulado y retornado para mostar en la app
				List<Map<String, String>> resultados = new ArrayList<>();
				while (resultSet.next()) {
					Map<String, String> fila = new HashMap<>();
					fila.put("ID", String.valueOf(resultSet.getInt("id")));
					fila.put("NOMBRE", resultSet.getString("nombre"));
					fila.put("DESCRIPCION", resultSet.getString("descripcion"));
					fila.put("CANTIDAD", String.valueOf(resultSet.getInt("cantidad")));
					resultados.add(fila);
				}
				//con.close();
				return resultados;
			}
		}
	}

    public void guardar(Producto producto) throws SQLException {

		Connection con = new ConnectionFactory().recuperarConexion();

		//Agregamos la opcion de try with resources para que no sea necesario realizar los cierres de la conexion y el statement
		try(con){
			con.setAutoCommit(false);

			final PreparedStatement statement = con.prepareStatement("INSERT INTO producto(nombre,descripcion,cantidad) " +
							"VALUES (?, ?, ?)"
					//Este metodo estatico de la clase statement nos permite obtener el id de una consulta cuando se insertan valores a una tabla
					,Statement.RETURN_GENERATED_KEYS);

			try(statement){
				ejecutarRegistro(producto, statement);
				System.out.println("Commit");
				//Si todas las ejecuciones anteriores salen bien, con el metodo commit confirmamos que estos cambios srean permannetes
				con.commit();
			}catch (Exception e){
				System.out.println("RollBack");
				//En caso de algun error el metodo rollback revertira los cambios
				con.rollback();
			}

		}
			//statement.close();
			//con.close();
	}

	private void ejecutarRegistro(Producto producto, PreparedStatement statement) throws SQLException {


		statement.setString(1, producto.getNombre());
		statement.setString(2, producto.getDescripcion());
		statement.setInt(3, producto.getCantidad());

		statement.execute();

		final ResultSet resultSet = statement.getGeneratedKeys();
		try(resultSet){
			while (resultSet.next()){
				producto.setId(resultSet.getInt(1));
				System.out.printf("Fue insertado el producto de ID %s",producto);
			}
		}
	}

}
