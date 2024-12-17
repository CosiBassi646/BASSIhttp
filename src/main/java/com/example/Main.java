package com.example;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class Main {
    public static void main(String[] args) throws IOException {   
        ServerSocket ss = new ServerSocket(8080);
        System.out.println("SERVER AVVIATO!");

        try {
            while (true) {
                Socket s = ss.accept();
                System.out.println("Nuova connessione da " + s.getInetAddress());

                myThread thread = new myThread(s);
                thread.start();
            }
        } catch (IOException e) {
            System.err.println("Errore nel server: " + e.getMessage());
        }
    }
}
