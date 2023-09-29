package network;

import java.io.*;
import java.net.*;
import java.sql.SQLException;
import java.util.*;
import controller.Controller;

public class RequestHandler extends Thread {
    public static Socket socket;
    public RequestHandler(Socket socket){
        this.socket = socket;
    }
    @Override
    public void run(){
        try {

            DataInputStream dis = new DataInputStream(socket.getInputStream());
            DataOutputStream dos = new DataOutputStream(socket.getOutputStream());


            String request = dis.readUTF();

            Scanner sc = new Scanner(request);

            String command = sc.nextLine();
            String data = sc.nextLine();

            String response = new Controller().run(command,data);

            dos.writeUTF(response);
            dos.flush();
            dos.close();
            dis.close();

            socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (SQLException | ClassNotFoundException | InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
}
