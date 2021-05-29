package client;

import server.ClientHandler;

import java.io.*;
import java.net.*;
import java.util.ArrayList;

public class Client {

    private static Socket clientSocket;
    private static BufferedReader reader;
    private static BufferedReader in;
    private static BufferedWriter out;

    private static void clientLoop(BufferedReader reader, BufferedReader in, BufferedWriter out) throws IOException {
        while (true){
            String serverWord = in.readLine();
            System.out.println(serverWord);
            serverWord = in.readLine();
            System.out.println(serverWord);
            String clientWord = reader.readLine();
            out.write(clientWord + "\n");
            out.flush();
            serverWord = in.readLine();
            System.out.println(serverWord);
            serverWord = in.readLine();
            new ArrayList<String>().toArray(new String[0]);
            String[] serverWords;
            serverWords = serverWord.split(" ");
            for (String _word : serverWords) {
                System.out.println(_word);
            }
            serverWord = in.readLine();
            System.out.println(serverWord);
            clientWord = reader.readLine();
            out.write(clientWord + "\n");
            out.flush();
            if (clientWord.equals("1")){
                break;
            }
        }
    }


    public static void main(String[] args){
        try{
            try{
                clientSocket = new Socket("localhost", 3000);
                reader = new BufferedReader(new InputStreamReader(System.in));
                in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
                out = new BufferedWriter(new OutputStreamWriter(clientSocket.getOutputStream()));
                clientLoop(reader, in, out);
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
