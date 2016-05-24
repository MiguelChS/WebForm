package models;

/**
 * Created by jn185090 on 5/24/2016.
 */
public class Sender {
    public String passwd;
    public String emailAddress;
    public String name;
    public String CSRCode;

    public Sender() {
    }

    public Sender(String passwd, String emailAddress, String name, String CSRCode) {
        this.passwd = passwd;
        this.emailAddress = emailAddress;
        this.name = name;
        this.CSRCode = CSRCode;
    }
}
