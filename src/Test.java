import java.sql.*;

public class Test {
	/**
	 * Test
	 * Clase de entrada de JDBCEjemplo
	 * Contiene el metodo main.
	 * Menu debe ser instanciado con new de lo contrario fallara el programa.
	 * @throws
	 * 	 SQLException 
	 * 		Si la clase menu lo lanza, puede ser debido a que la base de datos no esta disponible.
	 * @see Menu
	 * @see ConexionOracle
	 * **/
	public static void main(String[] args) throws SQLException {

		Menu menu = new Menu(); // Es necesario para inicializar las variables en el constructor.

		while (true) {
			Menu.mostrarMenu();

		}

	}

}
