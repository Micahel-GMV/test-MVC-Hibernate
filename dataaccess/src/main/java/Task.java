/**
 * Created by Micahel on 03.07.2017.
 */
import javax.persistence.*;
import java.sql.Blob;

/**
 * The persistent class for the task database table.
 *
 */

/*
* Pretty simple class except that SQLite supports BLOB`s only for writing. It`s possible to use there strings
* for storing pictures. Each byte is converted to char with some offset (it`s explained later). Yes, stored
* size will be twice larger than picture has but pictures are small and this is test work. In real life we`ll
* use another database, usual DBA for accessing stored pictures or wait for BLOB support by JDBC SQLite driver
* in later versions.
* */

@Entity
@Table(name = "task")
public class Task {

    private static final int charOffset = 0xAE; //It`s offset to use characters from UTF16 in range 128 chars
                                                //long without special symbols. String constructor or .toArray
                                                //distorts some bytes and restored from database picture is
                                                //not readable.

    public Integer getId() {
        return id;
    }

    public Task() {
    }

    public Task(String name, String email, String text, String pictName, byte[] pictBodyByte) {
        this(name, email, text, pictName, stringFromByte(pictBodyByte));
    }

    public Task(String name, String email, String text, String pictName, String pictBody) {
        this.name = name;
        this.email = email;
        this.pictName = pictName;
        this.text = text;
        this.pictBody = pictBody;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getPictName() {
        return pictName;
    }

    public void setPictName(String pictName) {
        this.pictName = pictName;
    }

    public static String stringFromByte(byte[] byteArr){
        int l = byteArr.length;
        char[] charArr = new char[l];
        for (int i = 0; i < l; i++) charArr[i] = (char)(byteArr[i] + charOffset);
        return new String(charArr);
    }

    public byte[] getPictBodyByte() {
        char[] ca = pictBody.toCharArray();
        byte[] result = new byte[ca.length];
        for (int i = 0; i < ca.length; i++) result[i] = (byte)(ca[i] - charOffset);
        return result;
    }

    public String getPictBody(){
        return pictBody;
    }

    public void setPictBody(String pictBody) {
        this.pictBody = pictBody;
    }

    @Id
    @GeneratedValue
    private Integer id;

    private String name;
    private String email;
    private String text;
    private String pictName;
    private String pictBody;//Attemped to use BLOB but JDBC sqlite driver supports writing but not reading BLOBs.
                            //Had to change it to string. Think @Lob and byte arrays will work on other databases
                            //as well as on one of later sqlite jdbc drivers.
                            //Another possibility is to use getBytes without Hibernate. I`ll make this if I`ll
                            // have enough time for this tutorial task.


}