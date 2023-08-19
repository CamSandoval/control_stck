package com.alura.jdbc.DAO;

import com.alura.jdbc.factory.ConnectionFactory;
import com.alura.jdbc.modelo.Producto;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ProductoDAO {
    final private Connection con;

    public ProductoDAO(Connection con){
        this.con= con;
    }

    public int modificar(String nombre, String descripcion, Integer id,Integer cantidad){
        try{
            final PreparedStatement statement = con.prepareStatement("UPDATE producto SET nombre = ? , descripcion = ? "+
                    " , cantidad = ? WHERE id = ?;");

            try(statement){
                statement.setString(1, nombre);
                statement.setString(2, descripcion);
                statement.setInt(3, cantidad);
                statement.setInt(4, id);

                statement.execute();
                return statement.getUpdateCount();
            }
        }catch (SQLException e){
            throw new RuntimeException(e);
        }

    }
    public int eliminar(Integer id){
        try {
            final PreparedStatement statement = con.prepareStatement("DELETE FROM producto WHERE id= ?");

            try(statement) {
                statement.setInt(1, id);
                statement.execute();

                //Este metodo retorna cuantos registros fuern modificados en la base de datos
                return statement.getUpdateCount();
            }
        }catch (SQLException e){
            throw new RuntimeException(e);
        }
    }

    public void guardar(Producto producto) {
        //Agregamos la opcion de try with resources para que no sea necesario realizar los cierres de la conexion y el statement
        try{
            //con.setAutoCommit(false);

            final PreparedStatement statement = con.prepareStatement("INSERT INTO producto(nombre,descripcion,cantidad) " +
                            "VALUES (?, ?, ?)"
                    //Este metodo estatico de la clase statement nos permite obtener el id de una consulta cuando se insertan valores a una tabla
                    , Statement.RETURN_GENERATED_KEYS);

            try(statement){
                ejecutarRegistro(producto, statement);
                System.out.println("Commit");
                //Si todas las ejecuciones anteriores salen bien, con el metodo commit confirmamos que estos cambios srean permannetes
                //con.commit();
            }catch (SQLException e){
                //System.out.println("RollBack");
                //En caso de algun error el metodo rollback revertira los cambios
                //con.rollback();
                throw new RuntimeException(e);
            }
        }catch (SQLException e){
            throw new RuntimeException(e);
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

    public List<Producto> listar() {
        //Creamos una lista que nos reciba el resultdo de la quuery en una forma en la que pueda ser manipulado y retornado para mostar en la app
        List<Producto> resultados = new ArrayList<>();
        try{
            //Los objetos Statement son los encargados de ejecutar las querys de nuestras base de datos, para
            // crearlos no es necesario instanciasrlos, pero si es necesario crarlo a partir de un objeto Connection
            final PreparedStatement statement = con.prepareStatement("SELECT id , nombre , descripcion , cantidad FROM producto");

            try(statement) {
                //Ejecucion del quiery
                statement.execute();
                //El objeto resultSet es el encargado de recibir el resulta de la query en la db, con este podremos obtener los strings de el resultado
                final ResultSet resultSet = statement.getResultSet();

                try(resultSet){
                    while (resultSet.next()) {
                        resultados.add(new Producto(
                                resultSet.getInt("id"),
                                resultSet.getString("nombre"),
                                resultSet.getString("descripcion"),
                                resultSet.getInt("cantidad")));
                    }
                }
            }
            //con.close();
        }catch (SQLException e){
            throw new RuntimeException(e);
        }
        return resultados;
    }
}
