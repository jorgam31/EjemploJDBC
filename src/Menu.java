import java.sql.SQLException;
import java.util.Scanner;

/**
 * Menu 
 * Clase para mostrar el menu de la consola
 * 
 * */
public class Menu {
	
	private static int opcion = -1;
	private static Scanner entrada;
	private static ConexionOracle conora;

	/**
	 * Inicializa los objetos 
	 *  1 de entrada de Scanner para permitir la entrada por teclado.
	 *  2 Inicializa la instancia de la clase conora de ConexionOracle 
	 *  @see Scanner
	 *  @see ConexionOracle
	 * */
	public Menu() {
		entrada = new Scanner(System.in);
		conora = new ConexionOracle();

	}

	/**
	 * Muestra el menu en consola y se llama al metodo elegirOpcion
	 * @throws 
	 * 	SQLException : si el metodo elegirOpcion lo lanza
	 *
	 * */
	public static void mostrarMenu() throws SQLException {

		System.out.println("APLICACION");
		System.out.println("==========");
		System.out.println(" 1 Insertar");
		System.out.println(" 2 Listar");
		System.out.println(" 3 Actualizar");
		System.out.println(" 4 Eliminar");
		System.out.println(" 0 Salir");

		elegirOpcion();
	}
	
	/**
	 * Metodo que permite elegir una opcion al usuario 
	 * @throws 
	 * 	  SQLException si el objeto conora de la clase ConexionOracle falla
	 * @see ConexionOracle
	 * */
	private static void elegirOpcion() throws SQLException {
		System.out.print("Opcion : ");
		opcion = entrada.nextInt();
		System.out.println();

		switch (opcion) {
		case 1:
			String nombre = "null";
			String puesto = "null";
			double sueldo = 0.0;
			System.out.println("Nombre:");
			nombre = entrada.next();
			System.out.println("Puesto:");
			puesto = entrada.next();
			System.out.println("Sueldo:");
			sueldo = entrada.nextDouble();

			int estado = conora.realizarInsert(nombre, puesto, sueldo);

			if (estado != 0) {
				System.out.println("El empleado o empleada " + nombre + " ha sido insertado/a en Oracle Database");
			} else {
				System.err.println("Ha ocurrido un error empleado " + nombre + " no insertado");
			}

			break;
		case 2:

			conora.realizarSelect(); // Funciona.

			break;
		case 3:

			int empleadoId = -1;
			String nuevonombre = "null";
			String nuevopuesto = "null";
			String nuevosueldo = "null";

			int status = -1;
			// conora.getData();
			System.out.println("Que empleado/a quieres actualizar?\n\nIntroduce el numero de ID:\t");
			empleadoId = entrada.nextInt();

			System.out.println("Introduce el nuevo nombre ");
			nuevonombre = entrada.next();
			System.out.println("Introduce el nuevo sueldo ");
			nuevosueldo = entrada.next();
			System.out.println("Introduce el nuevo puesto ");
			nuevopuesto = entrada.next();

			status = conora.realizarUpdate(empleadoId, nuevonombre, nuevopuesto, nuevosueldo);

			if (status > 0) {
				System.out.println("se ha actualizado el registro en Oracle Database");
			} else {
				System.out.println("No se ha podido actualizar el registro.");
			}

			break;
		case 4:
			empleadoId = -1;
			conora.realizarSelect();
			System.out.println("Introduce el ID del empleado que quieres eliminar: ");
			empleadoId = entrada.nextInt();
			conora.realizarDelete(empleadoId);

			break;
		case 0:
			System.out.println("Programa Finalizado.");
			System.exit(0);
			entrada.close();
			break;
		default:
			System.out.println("Opcion no valida");
			break;
		}

	}
}
