import java.io.*;
import java.net.*;

public class Client {
    public static void main(String[] args) throws IOException {
        Socket s=new Socket("127.0.0.1",8080);
        DataOutputStream dos=new DataOutputStream(s.getOutputStream());
        DataInputStream dis=new DataInputStream(s.getInputStream());
        dos.writeUTF("delay notice\n100,sara,50,ASSIGNED");
        dos.flush();
        System.out.println(dis.readUTF());
        dos.close();
        dis.close();
        s.close();
    }
}
