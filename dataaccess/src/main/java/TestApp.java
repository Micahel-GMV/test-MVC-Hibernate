import java.io.*;

/**
 * Created by Micahel on 03.07.2017.
 */
public class TestApp {
    public static final int targetW = 320, targetH = 240;
    public static void main(String[] args) {
        TaskDao td = new TaskDao();
        PictEngine pe = new PictEngine();

        String fileName = "input\\pic.jpg";

        td.addTask(new Task("Mike", "111@mail.com", "Lots of complains 1.", fileName, pe.toArray(fileName, targetW, targetH)));
        td.addTask(new Task("Mike", "222@mail.com", "Lots of complains 2.", fileName, pe.toArray(fileName, targetW, targetH)));
        td.addTask(new Task("Mike", "333@mail.com", "Lots of complains 3.", fileName, pe.toArray(fileName, targetW, targetH)));

        //td.getTasks("111@mail.com");
        for (Task task : td.getTasks()) pe.toFile(task.getId(), task.getPictName(), "output", task.getPictBodyByte());
    }
}
