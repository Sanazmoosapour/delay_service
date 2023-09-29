import java.io.*;
import java.net.*;

import static org.junit.Assert.assertEquals;

public class Client {
    public static void main(String[] args) throws IOException {

        Socket s = new Socket("127.0.0.1",8080);
        DataOutputStream dos = new DataOutputStream(s.getOutputStream());
        DataInputStream dis = new DataInputStream(s.getInputStream());

        dos.writeUTF("query\n ");
        dos.flush();


        System.out.println(dis.readUTF());

        dos.close();
        dis.close();
        s.close();
    }
}
