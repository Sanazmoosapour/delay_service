package network;

import java.io.IOException;
import java.net.*;

public class service {
    public void start(){
        int serverPort = 8080;

        try {

            ServerSocket serverSocket = new ServerSocket(serverPort);
            System.out.println("Server listening on port " + serverPort);

            while (true) {

                Socket clientSocket = serverSocket.accept();
                System.out.println("accept");
                new requestHandler(clientSocket).run();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }



    }
}
