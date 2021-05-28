package server;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.io.*;
import java.net.*;

public class Server {

    private static ServerSocket server;

    static ArrayList<String> pathList = new ArrayList<>();

    public static void parallelIndexing(int threadsNumber, List<String> fileNamesList, InvertedIndex index) throws InterruptedException {
        var size = fileNamesList.size();
        List<ThreadIndex> threadArray = Arrays.asList(new ThreadIndex[threadsNumber]);
        for (int i = 0; i < threadsNumber; i++){
            threadArray.set(i, new ThreadIndex(fileNamesList, index, (size / threadsNumber) * i, i == (threadsNumber - 1) ? size : size / threadsNumber * (i + 1)));
            threadArray.get(i).start();
        }
        for (int i = 0; i < threadsNumber; i++){
            threadArray.get(i).join();
        }
    }

    public static void main(String[] args) {
        pathList.add("src/resources/test/neg/");
        pathList.add("src/resources/test/pos/");
        pathList.add("src/resources/train/neg/");
        pathList.add("src/resources/train/pos/");
        pathList.add("src/resources/train/unsup/");

        List<String> files = InvertedIndex.getFileNamesList(pathList);
        int threadsNumber = 10;
        InvertedIndex idx = new InvertedIndex();

        try {
            parallelIndexing(threadsNumber, files, idx);
            System.out.println("All files are indexed:)");
        } catch (Exception e) {
            e.printStackTrace();
        }
        try{
            try{
                server = new ServerSocket(3000);
                System.out.println("Server connected");
                while (true) {
                    try{
                        Socket clientSocket = server.accept();
                        System.out.println("Client is connected");

                        var thread = new ClientHandler(clientSocket, idx);
                        thread.start();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            } finally {
                System.out.println("Server is closed!");
                server.close();
            }
        } catch (IOException e){
            System.err.println("Client has not connected");
        }


    }

}
