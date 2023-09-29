import static org.junit.Assert.*;
import org.junit.*;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

public class Test {
    static Socket s;
    static DataOutputStream dos;
    static DataInputStream dis;

    @Before
    public void before() throws IOException {
         s = new Socket("127.0.0.1",8080);
         dos = new DataOutputStream(s.getOutputStream());
         dis = new DataInputStream(s.getInputStream());
    }

    @After
    public void after() throws IOException {
        dos.close();
        dis.close();
        s.close();
    }
    @org.junit.Test
    public void testDelayNotice1() {
        try {
            dos.writeUTF("delay notice\n80,rr,60,AT_VENDOR");
            dos.flush();
            String response1 = dis.readUTF();
            System.out.println(response1);
            assertEquals(response1,"delay time is 14");
        }
        catch (Exception e){
            fail();
        }

    }
    @org.junit.Test
    public void testDelayNotice2() {
        try {
            dos.writeUTF("delay notice\n6,helia,60,DELIVERED");
            dos.flush();
            String response2 = dis.readUTF();
            assertEquals(response2,"your request is in delay queue");
        }
        catch (Exception e){
            fail();
        }

    }
    @org.junit.Test
    public void testDelayNotice3() {
        try {
            dos.writeUTF("delay notice\n100,sara,50,ASSIGNED");
            dos.flush();
            String response3 = dis.readUTF();
            assertEquals(response3,"false");
        }
        catch (Exception e){
            fail();
        }

    }

    @org.junit.Test
    public void testAsignOrder1() {
        try {
            dos.writeUTF("asign order\n900");
            dos.flush();
            String response1 = dis.readUTF();
            assertEquals(response1,"this agent is in progress");


        }
        catch (Exception e){
            fail();
        }

    }
    @org.junit.Test
    public void testAsignOrder2() {
        try {
            dos.writeUTF("asign order\n50");
            dos.flush();
            String response1 = dis.readUTF();
            assertEquals(response1,"delay order is in progress");


        }
        catch (Exception e){
            fail();
        }

    }
}
