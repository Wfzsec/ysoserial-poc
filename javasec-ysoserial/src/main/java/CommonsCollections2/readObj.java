package CommonsCollections2;

import java.io.*;

public class readObj {
    public static void main(String[] args) {
        try {
            FileInputStream fio = new FileInputStream(new File(System.getProperty("user.dir")+"/javasec-ysoserial/src/main/resources/commoncollections2.ser"));
            ObjectInputStream obj = new ObjectInputStream(fio);
            obj.readObject();
            obj.close();

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }


    }

}
