package network;

import java.io.*;
import java.net.*;
import java.sql.SQLException;
import java.util.*;
import controller.controller;

public class requestHandler extends Thread {
    public static Socket socket;
    public requestHandler(Socket socket){
        this.socket = socket;
    }
    @Override
    public void run(){
        try {

            DataInputStream dis=new DataInputStream(socket.getInputStream());
            DataOutputStream dos=new DataOutputStream(socket.getOutputStream());


            String request=dis.readUTF();

            Scanner sc=new Scanner(request);

            String command=sc.nextLine();
            String data=sc.nextLine();

            String response=new controller().run(command,data);

            dos.writeUTF(response);
            dos.flush();
            dos.close();
            dis.close();

            socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
}
