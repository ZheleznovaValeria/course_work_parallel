package server;

import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.io.*;
import java.net.*;
import java.util.Scanner;

public class Server {

    private static ServerSocket server;

    static ArrayList<String> pathList = new ArrayList<>();

    public static void testIndexing (List<String> fileNamesList, InvertedIndex index) throws InterruptedException {
        var size = fileNamesList.size();
        int maxNumThreads = 20;
        int step = 2;
        StringBuilder table = new StringBuilder("1\t");
        for (int num = 500; num <= size; num += 250){
            Instant start = Instant.now();
            parallelIndexing(1, fileNamesList, index, size);
            Instant finish = Instant.now();
            long timeElapsed = Duration.between(start, finish).toMillis();
            table.append(timeElapsed).append("\t");
        }
        table.append("\n");

        for (int i = 2; i <= maxNumThreads; i += step){
            table.append(i).append("\t");
            for (int num = 500; num <= size; num += 250){
                Instant start = Instant.now();
                parallelIndexing(i, fileNamesList, index, size);
                Instant finish = Instant.now();
                long timeElapsed = Duration.between(start, finish).toMillis();
                table.append(timeElapsed).append("\t");
            }
            table.append("\n");
        }
        System.out.println(table);
        System.out.println("Indexed with " + maxNumThreads + " threads with step " + step );

    }

    public static void parallelIndexing(int threadsNumber, List<String> fileNamesList, InvertedIndex index, int size) throws InterruptedException {
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
        InvertedIndex idx = new InvertedIndex();

        System.out.println("Enter number of threads:");
        Scanner consoleIn = new Scanner(System.in);
        int threadsNumber = consoleIn.nextInt();

        try {
            int size = files.size();
            parallelIndexing(threadsNumber, files, idx, size);
            /*testIndexing(files, idx);*/
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
