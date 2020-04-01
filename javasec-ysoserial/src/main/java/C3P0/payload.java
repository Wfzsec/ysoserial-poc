package C3P0;

import java.io.*;
import java.lang.Runtime;

public class payload {
    public static void main(String[] args) throws IOException, ClassNotFoundException {
            File file;
            file = new File(System.getProperty("user.dir")+"/javasec-ysoserial/src/main/resources/t.ser");
            ObjectInputStream obj = new ObjectInputStream(new FileInputStream(file));
            obj.readObject();
    }
}
