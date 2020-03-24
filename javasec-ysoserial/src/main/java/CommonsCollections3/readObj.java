package CommonsCollections3;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;

public class readObj {
    public static void main(String[] args) {
        File file;
        file = new File(System.getProperty("user.dir")+"/javasec-ysoserial/src/main/resources/commoncollections3.ser");
        try {
            ObjectInputStream obj = new ObjectInputStream(new FileInputStream(file));
            obj.readObject();
            obj.close();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

}
