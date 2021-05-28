package client;

import java.io.*;
import java.net.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Client {

    private static Socket clientSocket;
    private static BufferedReader reader;
    private static BufferedReader in;
    private static BufferedWriter out;


    public static void main(String[] args){
        try{
            try{
                clientSocket = new Socket("localhost", 3000);
                reader = new BufferedReader(new InputStreamReader(System.in));
                in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
                out = new BufferedWriter(new OutputStreamWriter(clientSocket.getOutputStream()));
                String serverWord;
                String clientWord;
                serverWord = in.readLine();
                System.out.println(serverWord);
                serverWord = in.readLine();
                System.out.println(serverWord);
                clientWord = reader.readLine();
                out.write(clientWord + "\n");
                out.flush();
                serverWord = in.readLine();
                System.out.println(serverWord);
                serverWord = in.readLine();
                String[] serverWords = new ArrayList<String>().toArray(new String[0]);
                serverWords = serverWord.split(" ");
                for (String _word : serverWords) {
                    System.out.println(_word);
                }

            } finally {
                System.out.println("Client was closed");
                clientSocket.close();
                in.close();
                out.close();
                reader.close();
            }
        } catch (IOException e) {
            System.err.println(e);
        }

    }
}
