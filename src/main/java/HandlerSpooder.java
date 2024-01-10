import org.xml.sax.helpers.DefaultHandler;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class HandlerSpooder extends DefaultHandler {
    private boolean inBooking = false;
    private boolean isTargetBooking = false;
    private String targetLocationNumber;

    private boolean inClient = false;
    private boolean inAgency = false;
    private boolean inPrice = false;
    private boolean inRoom = false;
    private boolean inHotel = false;
    private boolean inCheckIn = false;
    private boolean inRoomNights = false;


    private StringBuilder data;

    public HandlerSpooder(String targetLocationNumber){
        this.targetLocationNumber = targetLocationNumber;
    }

    @Override
    public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
        if ("booking".equals(qName)) {
            inBooking = true;
            String locationNumber = attributes.getValue("location_number");
            isTargetBooking = targetLocationNumber.equals(locationNumber);
            if (isTargetBooking) {
                data = new StringBuilder();
            }
        } else if (isTargetBooking) {
            if ("client".equals(qName)) {
                inClient = true;
            } else if ("agency".equals(qName)) {
                inAgency = true;
            } else if ("price".equals(qName)) {
                inPrice = true;
            } else if ("room".equals(qName)) {
                inRoom = true;
            } else if ("hotel".equals(qName)) {
                inHotel = true;
            } else if ("check_in".equals(qName)) {
                inCheckIn = true;
            } else if ("room_nights".equals(qName)) {
                inRoomNights = true;
            }
        }
    }

    @Override
    public void characters(char ch[], int start, int length) throws SAXException {
        if (isTargetBooking) {
            data.append(new String(ch, start, length));
        }
    }

    @Override
    public void endElement(String uri, String localName, String qName) throws SAXException {
        if (isTargetBooking) {
            if ("booking".equals(qName)) {
                inBooking = false;
                isTargetBooking = false;
            } else if ("client".equals(qName)) {
                inClient = false;
            } else if ("agency".equals(qName)) {
                inAgency = false;
            } else if ("price".equals(qName)) {
                inPrice = false;
            } else if ("room".equals(qName)) {
                inRoom = false;
            } else if ("hotel".equals(qName)) {
                inHotel = false;
            } else if ("check_in".equals(qName)) {
                inCheckIn = false;
            } else if ("room_nights".equals(qName)) {
                inRoomNights = false;
            }
        }
    }

    public StringBuilder getData() {
        return data;
    }
}
