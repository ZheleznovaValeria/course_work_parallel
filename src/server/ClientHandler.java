package server;

import java.net.*;
import java.io.*;
import java.util.Arrays;

public class ClientHandler extends Thread{
    private static BufferedReader in;
    private static BufferedWriter out;
    private final Socket clientSocket;
    private final InvertedIndex idx;


    public ClientHandler (Socket clientSocket, InvertedIndex idx){
        this.clientSocket = clientSocket;
        this.idx = idx;
    }

    private static void serverLoop(BufferedReader in, BufferedWriter out, InvertedIndex idx) throws IOException {
        String clientWord;
        do {
            out.write("All files are indexed\n");
            out.flush();
            out.write("Write words you wanna search separated by commas: \n");
            out.flush();
            clientWord = in.readLine();
            String result = idx.search(Arrays.asList(clientWord.split(",")));
            out.write("RESULT OF SEARCHING: \n");
            out.flush();
            out.write(result + "\n");
            out.flush();
            out.write("Wanna quit? 1 - Yes, 2 - No\n");
            out.flush();
            clientWord = in.readLine();
        } while (!clientWord.equals("1"));
    }

    @Override
    public void run() {
        try{
            try{
                in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
                out = new BufferedWriter(new OutputStreamWriter(clientSocket.getOutputStream()));
                serverLoop(in, out, idx);
            } finally {
                clientSocket.close();
                in.close();
                out.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
