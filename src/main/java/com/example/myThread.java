package com.example;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.Socket;

public class myThread extends Thread{
    
    Socket s;
    myThread(Socket s){
        this.s = s;
    }
    
    //versione stato desc content-type

    public void run() {
        try {
            BufferedReader in = new BufferedReader(new InputStreamReader(s.getInputStream()));
            DataOutputStream out = new DataOutputStream(s.getOutputStream());
            String tmp;

            String firstLine = in.readLine();
            String[] request = firstLine.split(" ");
            
            String method = request[0];
            String resource = request[1];
            String version = request[2];

            String header;

            do{
                tmp = in.readLine();
                System.out.println(tmp + "\n");
            }while(!tmp.isEmpty());

            if (resource.endsWith("/")) {
                resource = resource + "/index.html";
            }

            File file = new File("httdocs" + resource);
            if(file.isDirectory()){
                out.writeBytes("HTTP/1.1 301 Moved Permanently\n");
                    out.writeBytes("Content-Length: 0\n");
                    out.writeBytes("Location: " + resource + "/\n");
                    out.writeBytes("\n");
            }else{
                if (file.exists()){ //controllo se il file esiste

                    out.writeBytes("HTTP/1.1 200 ok\n");
                    out.writeBytes("content-type" + getContentType(file));
                    out.writeBytes("content-lenght: " + file.length() + "\n");
                    out.writeBytes("\n");
    
                    InputStream input = new FileInputStream(file);
                    byte[] buf = new byte[8192];
                    int n;
                    while ((n=input.read(buf)) != -1) {
                        out.write(buf,0,n);
                    }
                    input.close();
    
    
                }else{
                    //Error404
                    String responsebody = "ERRORE file non trovato";
                    out.writeBytes("HTTP/1.1 404 not found\n");
                    out.writeBytes("content-type: text/plain\n");
                    out.writeBytes("content-lenght: " + responsebody.length() + "\n");
                    out.writeBytes("\n"); //riga vuota per http
                    out.writeBytes(responsebody);
    
                }
            }
          
            s.close();
            
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }      
    }

    private static String getContentType(File f){
            String[] s = f.getName().split("\\.");
            String ext = s[s.length - 1];
            switch (ext) {
                case "html":
                    return "text/html";    

                case "jpg":
                    return "image/jpg";

                case "css":
                    return "text/css";

                default:
                    return "";
            }
    }





}
