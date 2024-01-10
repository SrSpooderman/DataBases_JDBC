import lombok.Getter;
import lombok.Setter;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import java.io.File;
import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;

@Getter
@Setter
public class Reserva {
    private String locationNumber;
    private String client;
    private String agency;
    private String price;
    private String roomType;
    private String hotel;
    private String checkIn;
    private String roomNights;

    public Reserva(){

    }


    public void refreshDataFile(File archivo, String LocNumber){
        try{
            this.locationNumber = LocNumber;
            SAXParserFactory factory = SAXParserFactory.newInstance();
            SAXParser saxParser = factory.newSAXParser();
            HandlerSpooder handlerSpooder = new HandlerSpooder(LocNumber);
            saxParser.parse(archivo, handlerSpooder);

            String data = handlerSpooder.getData().toString();
            String[] lines = data.split("\n");

            this.client = lines[1];
            this.agency = lines[2];
            this.price = lines[3];
            this.roomType = lines[4];
            this.hotel = lines[5];
            this.checkIn = lines[6];
            this.roomNights = lines[7];
        } catch (Exception e) {
            System.out.println("Localizador no existente");
        }
    }

    public void refreshDataDB(ResultSet resultSet){
        try {
            this.locationNumber = resultSet.getString("location_number");
            this.client = resultSet.getString("client");
            this.agency = resultSet.getString("agency");
            this.price = resultSet.getString("price");
            this.roomType = resultSet.getString("roomtype");
            this.hotel = resultSet.getString("hotel");
            this.checkIn = resultSet.getString("checkIn");
            this.roomNights = resultSet.getString("roomNights");
        } catch (SQLException e) {
            System.out.println("No se han podido pasar los datos");
            throw new RuntimeException(e);
        }

    }

    public void printBookingData() {
        System.out.println("-------------------------");
        System.out.println("Location Number: " + this.locationNumber);
        System.out.println("Client: " + this.client);
        System.out.println("Agency: " + this.agency);
        System.out.println("Price: " + this.price);
        System.out.println("Room Type: " + this.roomType);
        System.out.println("Hotel: " + this.hotel);
        System.out.println("Check In Date: " + this.checkIn);
        System.out.println("Room Nights: " + this.roomNights);
        System.out.println("-------------------------");
    }
}
