import static org.junit.Assert.*;
import org.junit.*;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

public class test {
    static Socket s;
    static DataOutputStream dos;
    static DataInputStream dis;
    //execute before class
    @Before
    public void before() throws IOException {
         s=new Socket("127.0.0.1",8080);
         dos=new DataOutputStream(s.getOutputStream());
         dis=new DataInputStream(s.getInputStream());
    }

    //execute after class
    @After
    public void after() throws IOException {
        dos.close();
        dis.close();
        s.close();
    }
    @Test
    public void testDelayNotice() {
        try {
            dos.writeUTF("delay notice\n100,sanaz,50,ASSIGNED");
            dos.flush();
            String response1 = dis.readUTF();
            assertEquals(response1,"delay time is ----");

            dos.writeUTF("delay notice\n400,sara,50,ASSIGNED");
            dos.flush();
            String response2 = dis.readUTF();
            assertEquals(response2,"your request is in delay queue");

            dos.writeUTF("delay notice\n100,sara,50,ASSIGNED");
            dos.flush();
            String response3 = dis.readUTF();
            assertEquals(response3,"false");

        }
        catch (Exception e){
            fail();
        }

    }

    @Test
    public void testAsignOrder() {
        try {
            dos.writeUTF("delay notice\n100,sanaz,50,ASSIGNED");
            dos.flush();
            String response1 = dis.readUTF();
            assertEquals(response1,"delay time is ----");

            dos.writeUTF("delay notice\n400,sara,50,ASSIGNED");
            dos.flush();
            String response2 = dis.readUTF();
            assertEquals(response2,"your request is in delay queue");

            dos.writeUTF("delay notice\n100,sara,50,ASSIGNED");
            dos.flush();
            String response3 = dis.readUTF();
            assertEquals(response3,"false");

        }
        catch (Exception e){
            fail();
        }

    }
}
