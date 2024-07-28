package graph;
import java.util.Date;

/**
 * The Message class represents a message with data in various formats (byte array, text, double) and a timestamp.
 */
public class Message {
    public final byte[] data;
    public final String asText;
    public final double asDouble;
    public final Date date;

    /**
     * Constructs a Message with the specified byte array data.
     *
     * @param data the byte array data
     */
    public Message(byte[] data) {
        this.data = data;
        this.asText = new String(data);
        this.asDouble = convertToDouble(this.asText);
        this.date = new Date();
    }

    /**
     * Returns the byte array data of the message.
     *
     * @return the byte array data
     */
    public byte[] getData() {
        return data;
    }

    /**
     * Constructs a Message with the specified text.
     *
     * @param text the text data
     */
    public Message(String text) {
        this(text.getBytes());
    }

    /**
     * Constructs a Message with the specified double value.
     *
     * @param value the double value
     */
    public Message(double value) {
        this(Double.toString(value).getBytes());
    }

    /**
     * Helper method to convert a String to a double.
     *
     * @param text the text to convert
     * @return the double value, or Double.NaN if the text cannot be converted
     */
    private double convertToDouble(String text) {
        try {
            return Double.parseDouble(text);
        } catch (NumberFormatException e) {
            return Double.NaN; // Not a number
        }
    }
}
