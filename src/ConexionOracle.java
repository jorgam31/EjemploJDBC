import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class ConexionOracle {
	/**
	 * ConexionOracle Clase para conectar a oracle database 
	 * 		Clase funcional con java se 11 LTS y Oracle Database 11g Express Edition Release 11.2.0.2.0 
	 * @param jdbcurl : url de conexion. Debe seguir el esquema de url de Oracle
	 *                jdbc:oracle:thin:@hostname:port Number:databaseName
	 * @param user:   usuario que se conectara a Oracle Database
	 * @param pass:   contraseña del usuario que se conectara a Oracle Database
	 * @param conn:   conexion a oracle de tipo Connection.
	 */
	private String jdbcurl;
	private String user;
	private String pass;
	private Connection cn;

	/**
	 * Crea una nueva instancia de Conexion. Default Constructor.
	 */
	public ConexionOracle() {

	}

	/**
	 * establecerConexion Establece una nueva conexion a Oracle Database
	 * 
	 * @param jdbcurl  : url de conexion a oracle jdbc:// indicando el servidor y el
	 *                 puerto Oracle utiliza 1521 por defecto
	 * @param user:    usuario que se conectara a su esquema de Oracle
	 * @param pass:    contraseña del usuario
	 * @param sqlstat: sentencia sql a ejecutar
	 * @throws SQLException si el acceso a la base de datos falla o la url es null.
	 */
	public void establecerConexion(String jdbcurl, String user, String pass, String sqlstat) throws SQLException {

		this.jdbcurl = jdbcurl;
		this.user = user;
		this.pass = pass;
		// getData(this.sqlstat);
		cn = DriverManager.getConnection(this.jdbcurl, this.user, this.pass);
		// getData();
	}

	/**
	 * establecerConexion Establece una nueva conexion a Oracle Database
	 * 
	 * @param jdbcurl : url de conexion a oracle jdbc:// indicando el servidor y el
	 *                puerto Oracle utiliza 1521 por defecto
	 * @param user:   usuario que se conectara a su esquema de Oracle
	 * @param pass:   contraseña del usuario
	 */
	private void establecerConexion(String jdbcurl, String user, String pass) {

		this.jdbcurl = jdbcurl;
		this.user = user;
		this.pass = pass;

	}

	/**
	 * realizarSelect Realiza un select a la tabla MisEmples.
	 * 
	 * @throws SQLException Si no se puede acceder a la base de datos.
	 */
	public void realizarSelect() throws SQLException {
		String sql = "SELECT * FROM MISEMPLES";
		this.establecerConexion("jdbc:oracle:thin:@localhost:1521:xe", "usuario", "contrasena"); // remplazar por el usuario correcto y contraseña correcta
		Connection cn = DriverManager.getConnection(this.jdbcurl, this.user, this.pass);
		Statement stmt = cn.createStatement();
		ResultSet resultSet = stmt.executeQuery(sql);

		while (resultSet.next()) {

			System.out.println("Empleado Id: \n=========\n" + resultSet.getInt(1));
			System.out.println("Nombre : " + resultSet.getString(2));
			System.out.println("Puesto : " + resultSet.getString(3));
			System.out.println("Sueldo : " + resultSet.getString(4));

		}

		stmt.close();
		resultSet.close();

		System.out.println("Se ha cerrado la conexion a Oracle.");

	}

	/**
	 * realizarInsert Realiza una inserccion en la tabla.
	 * 
	 * @param nombre : Nombre a insertar en Oracle Database
	 * @param puesto : El puesto del empleado a insertar en Oracle Database
	 * @param sueldo : El sueldo del empleado a insertar en Oracle Database
	 * @return 0 Si ha fallado > 0 : Numero de insercciones nuevas realizadas en
	 *         Oracle Database
	 */
	public int realizarInsert(String nombre, String puesto, double sueldo) throws SQLException {

		// Conseguimos el siguiente id de la secuencia.
		String seq_sql = "select MISEMPLE_SEQ.NEXTVAL FROM DUAL";
		int status = -1;

		this.establecerConexion("jdbc:oracle:thin:@localhost:1521:xe", "usuario", "contrasena"); // remplazar por el usuario correcto y contraseña correcta

		this.cn = DriverManager.getConnection(jdbcurl, user, pass);

		PreparedStatement ps = this.cn.prepareStatement(seq_sql);
		try {

			ps = cn.prepareStatement(seq_sql);
		} catch (SQLException e1) {

			e1.printStackTrace();
		}
		ResultSet rs = null;
		try {
			rs = ps.executeQuery();
		} catch (SQLException e1) {

			e1.printStackTrace();
		}
		int nextIdSequence = 0;
		try {
			if (rs.next())
				nextIdSequence = rs.getInt(1);
		} catch (SQLException e1) {

			e1.printStackTrace();
		}

		// Introducimos el registro en Oracle.

		String sql = "INSERT INTO MISEMPLES(EMPLEADO_ID,NOMBRE,PUESTO,SUELDO) VALUES (?, ? , ?, ?)";
		PreparedStatement prepstmt = null;
		try {
			prepstmt = cn.prepareStatement(sql);
			prepstmt.setInt(1, nextIdSequence);
			prepstmt.setString(2, nombre);
			prepstmt.setString(3, puesto);
			prepstmt.setDouble(4, sueldo);

			// Si ha actualizado con exito devolvera el numero de filas afectadas.
			// Si ha fallado al actualizar devolvera 0
			status = prepstmt.executeUpdate();

		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				cn.close(); // cerramos la conexion a oracle.
			} catch (SQLException e) {

				e.printStackTrace();
			}
		}
		return status;

	}

	/**
	 * realizarUpdate Actualiza un registro en Oracle Database
	 * 
	 * @return 0 : Si no se ha podido actualizar ningun registro > 0 : Si se ha
	 *         actualizado uno o mas registros.
	 */
	protected int realizarUpdate(int empleadoId, String nombre, String puesto, String sueldo) throws SQLException {

		String sql = "UPDATE MISEMPLES SET nombre = ?, puesto = ?, sueldo = ? WHERE empleado_id = ?";
		this.establecerConexion("jdbc:oracle:thin:@localhost:1521:xe", "usuario", "contrasena"); // remplazar por el usuario correcto y contraseña correcta
		this.cn = DriverManager.getConnection(this.jdbcurl, this.user, this.pass);

		try {
			PreparedStatement ps1 = cn.prepareStatement(sql);

			ps1.setString(1, nombre);
			ps1.setString(2, puesto);
			ps1.setString(3, sueldo);
			ps1.setInt(4, empleadoId);
			int filaActualizada = ps1.executeUpdate();

			if (filaActualizada > 0) {
				return 1;
			}

		} catch (SQLException e) {

			e.printStackTrace();
		}

		return 0;
	}

	/**
	 * Elimina un registro de Oracle Database
	 * 
	 * @param empleadoId: El id del empleado a eliminar
	 * @return Devuelve un numero : 0 : Si el registro se ha eliminado -1 : Si no se
	 *         ha podido eliminar el registro debido a un error.
	 */
	protected int realizarDelete(int empleadoId) throws SQLException {
		String sql = "DELETE FROM MISEMPLES WHERE empleado_id = ?";

		cn = DriverManager.getConnection(this.jdbcurl, this.user, this.pass);
		PreparedStatement ptsmt = cn.prepareStatement(sql);
		ptsmt.setInt(1, empleadoId);
		int exitStatus = ptsmt.executeUpdate();

		if (exitStatus == 0) {
			System.err.println("Error al eliminar un registro. Registro no eliminado.");
			return -1;
		}
		System.out.println("Se ha eliminado un registro de oracle database");
		return 0;
	}

}