import java.io.File;
import java.sql.*;
import java.util.Scanner;
import java.util.Vector;

public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        String url = "jdbc:mariadb://localhost:3306/reserves";
        String usuario = "spooder";
        String password = "1234";

        while (true) {
            System.out.println("Choose an option:");
            System.out.println("1. Cargar reserva");
            System.out.println("2. Limpiar las reservas");
            System.out.println("3. Consultar los datos de una reserva");
            System.out.println("4. Consultar las reservas por agencia");
            System.out.println("5. Insertar nueva reserva");
            System.out.println("6. Borrar una reserva");
            System.out.println("7. Modificar una reserva");
            System.out.println("8. Exit");

            System.out.print("Insert: ");
            int choice = scanner.nextInt();

            switch (choice) {
                case 1:
                    System.out.println("Cargando booking.xml:");
                    Reserva bookingInstance = new Reserva();
                    File archivo = new File("bookings.xml");
                    Vector<Reserva> reservas = new Vector<>();
                    int locNumber = 0;

                    while (true){
                        try {
                            locNumber++;
                            bookingInstance = new Reserva();
                            bookingInstance.refreshDataFile(archivo, String.valueOf(locNumber));
                            reservas.add(bookingInstance);
                        }catch (Exception ex){
                            break;
                        }
                    }

                    try {
                        Connection connection = DriverManager.getConnection(url,usuario,password);

                        for (Reserva reserva:reservas){
                            String peticion = "INSERT INTO reserves (location_number, client, agency, price, roomtype, hotel, checkIn, roomNights) VALUES(?,?,?,?,?,?,?,?)";
                            PreparedStatement preparedStatement = connection.prepareStatement(peticion);
                            preparedStatement.setString(1, reserva.getLocationNumber());
                            preparedStatement.setString(2, reserva.getClient());
                            preparedStatement.setString(3, reserva.getAgency());
                            preparedStatement.setString(4, reserva.getPrice());
                            preparedStatement.setString(5, reserva.getRoomType());
                            preparedStatement.setString(6, reserva.getHotel());
                            preparedStatement.setString(7, reserva.getCheckIn());
                            preparedStatement.setString(8, reserva.getRoomNights());
                            preparedStatement.executeUpdate();
                        }
                    } catch (SQLException e) {
                        throw new RuntimeException(e);
                    }
                    break;
                case 2:
                    try {
                        Connection connection = DriverManager.getConnection(url,usuario,password);
                        Statement statement = connection.createStatement();
                        String peticion = "DELETE FROM reserves";
                        statement.executeUpdate(peticion);
                    } catch (SQLException e) {
                        throw new RuntimeException(e);
                    }
                    break;
                case 3:
                    System.out.println("Ingrese numero de locación");
                    String loc_num = scanner.next();
                    try {
                        Connection connection = DriverManager.getConnection(url,usuario,password);
                        String consultaSQL = "SELECT * FROM reserves WHERE location_number = ?";
                        PreparedStatement preparedStatement = connection.prepareStatement(consultaSQL);
                        preparedStatement.setString(1, loc_num);

                        ResultSet resultado = preparedStatement.executeQuery();
                        Reserva reserva = new Reserva();
                        reserva.refreshDataDB(resultado);

                        reserva.printBookingData();
                    } catch (SQLException e) {
                        throw new RuntimeException(e);
                    }
                    break;
                case 4:
                    System.out.println("Ingrese agencia");
                    String agency = scanner.next();
                    try {
                        Connection connection = DriverManager.getConnection(url,usuario,password);
                        String consultaSQL = "SELECT * FROM reserves WHERE agency = ?";
                        PreparedStatement preparedStatement = connection.prepareStatement(consultaSQL);
                        preparedStatement.setString(1, agency);

                        ResultSet resultado = preparedStatement.executeQuery();
                        while (resultado.next()) {

                            Reserva reserva = new Reserva();
                            reserva.refreshDataDB(resultado);

                            reserva.printBookingData();
                        }
                    } catch (SQLException e) {
                        throw new RuntimeException(e);
                    }
                    break;
                case 5:
                    System.out.println("Insertar nueva reserva:");

                    System.out.print("Número de localización: ");
                    String locNumberInsert = scanner.next();

                    System.out.print("Cliente: ");
                    String clientInsert = scanner.next();

                    System.out.print("Agencia: ");
                    String agencyInsert = scanner.next();

                    System.out.print("Precio: ");
                    String priceInsert = scanner.next();

                    System.out.print("Tipo de habitación: ");
                    String roomTypeInsert = scanner.next();

                    System.out.print("Hotel: ");
                    String hotelInsert = scanner.next();

                    System.out.print("Fecha de check-in: ");
                    String checkInInsert = scanner.next();

                    System.out.print("Número de noches: ");
                    String roomNightsInsert = scanner.next();

                    try {
                        Connection connection = DriverManager.getConnection(url, usuario, password);
                        String peticionInsert = "INSERT INTO reserves (location_number, client, agency, price, roomtype, hotel, checkIn, roomNights) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
                        PreparedStatement preparedStatementInsert = connection.prepareStatement(peticionInsert);
                        preparedStatementInsert.setString(1, locNumberInsert);
                        preparedStatementInsert.setString(2, clientInsert);
                        preparedStatementInsert.setString(3, agencyInsert);
                        preparedStatementInsert.setString(4, priceInsert);
                        preparedStatementInsert.setString(5, roomTypeInsert);
                        preparedStatementInsert.setString(6, hotelInsert);
                        preparedStatementInsert.setString(7, checkInInsert);
                        preparedStatementInsert.setString(8, roomNightsInsert);
                        preparedStatementInsert.executeUpdate();
                        System.out.println("Reserva añadido correctamente.");
                    } catch (SQLException e) {
                        throw new RuntimeException(e);
                    }
                    break;
                case 6:
                    System.out.println("Borrar una reserva");
                    System.out.print("Ingrese el número de localización a borrar: ");
                    String locNumberDelete = scanner.next();
                    try {
                        Connection connection = DriverManager.getConnection(url,usuario,password);
                        String peticionDelete = "DELETE FROM reserves WHERE location_number = ?";
                        PreparedStatement preparedStatementDelete = connection.prepareStatement(peticionDelete);
                        preparedStatementDelete.setString(1, locNumberDelete);
                        preparedStatementDelete.executeUpdate();
                    } catch (SQLException e) {
                        throw new RuntimeException(e);
                    }
                    break;
                case 7:
                    System.out.println("Modificar una reserva:");
                    System.out.print("Ingrese el número de localización a modificar: ");
                    String locNumberUpdate = scanner.next();
                    try {
                        Connection connection = DriverManager.getConnection(url, usuario, password);
                        String peticionSelect = "SELECT * FROM reserves WHERE location_number = ?";
                        PreparedStatement preparedStatementSelect = connection.prepareStatement(peticionSelect);
                        preparedStatementSelect.setString(1, locNumberUpdate);
                        ResultSet resultado = preparedStatementSelect.executeQuery();

                        Reserva reserva = new Reserva();
                        reserva.refreshDataDB(resultado);
                        reserva.printBookingData();

                        System.out.println("Insertar los nuevos datos:");

                        System.out.print("Nuevo cliente: ");
                        String newClient = scanner.next();

                        System.out.print("Nueva agencia: ");
                        String newAgency = scanner.next();

                        System.out.print("Nuevo precio: ");
                        String newPrice = scanner.next();

                        System.out.print("Nuevo tipo de habitación: ");
                        String newRoomType = scanner.next();

                        System.out.print("Nuevo hotel: ");
                        String newHotel = scanner.next();

                        System.out.print("Nueva fecha de check-in: ");
                        String newCheckIn = scanner.next();

                        System.out.print("Nuevo número de noches: ");
                        String newRoomNights = scanner.next();

                        String peticionUpdate = "UPDATE reserves SET client = ?, agency = ?, price = ?, roomtype = ?, hotel = ?, checkIn = ?, roomNights = ? WHERE location_number = ?";
                        PreparedStatement preparedStatementUpdate = connection.prepareStatement(peticionUpdate);
                        preparedStatementUpdate.setString(1, newClient);
                        preparedStatementUpdate.setString(2, newAgency);
                        preparedStatementUpdate.setString(3, newPrice);
                        preparedStatementUpdate.setString(4, newRoomType);
                        preparedStatementUpdate.setString(5, newHotel);
                        preparedStatementUpdate.setString(6, newCheckIn);
                        preparedStatementUpdate.setString(7, newRoomNights);
                        preparedStatementUpdate.setString(8, locNumberUpdate);

                        preparedStatementUpdate.executeUpdate();

                        System.out.println("Datos actualizados");
                    } catch (SQLException e) {
                        throw new RuntimeException(e);
                    }
                    break;
                case 8:
                    System.out.println("Cerrando");
                    System.exit(0);
                default:
                    System.out.println("Seleccion no tratada.");
            }
        }
    }
}
