package server;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.io.*;
import java.net.*;

public class Server {

//    long start = System.nanoTime();
//    ThreadIndex[] ThreadArray = new ThreadIndex[NUMBER_THREADS];
//
//            for (int i = 0; i < NUMBER_THREADS; i++) {
//        ThreadArray[i] = new ThreadIndex(array,
//                ROWS / NUMBER_THREADS * i,
//                i == (NUMBER_THREADS - 1) ? ROWS : ROWS / NUMBER_THREADS * (i + 1));
//        ThreadArray[i].start();
//    }
//            for (int i = 0; i < NUMBER_THREADS; i++) {
//        ThreadArray[i].join();
//    }

//    public static void indexCreatingParallel(
//            threadsNumber: Int,
//            fileNamesList: MutableList<String>,
//            index: ConcurrentHashMap<String, MutableList<Location>>?
//    ) {
//        val threadArray: Array<ThreadIndex?> = arrayOfNulls(threadsNumber)
//        val size = fileNamesList.size
//
//        for (i in 0 until threadsNumber) {
//            threadArray[i] = ThreadIndex(
//                    fileNamesList,
//                    index,
//                    (size / threadsNumber) * i,
//            if (i == threadsNumber - 1) size else size / threadsNumber * (i + 1)
//        )
//            threadArray[i]!!.start()
//        }
//
//        for (i in 0 until threadsNumber) {
//            threadArray[i]!!.join()
//        }
//    }
    private static Socket clientSocket;
    private static ServerSocket server;
    private static BufferedReader in;
    private static BufferedWriter out;

    static ArrayList<String> pathList = new ArrayList<String>();

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

    private static void serverLoop(BufferedReader in, BufferedWriter out, String clientWord, InvertedIndex idx) throws IOException {
        while(true){
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
            if (clientWord.equals("1")){
                break;
            }
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
//            idx.search(Arrays.asList(files.get(0).split(",")));
        } catch (Exception e) {
            e.printStackTrace();
        }
        try{
            try{
                server = new ServerSocket(3000);
                System.out.println("Server connected");
                clientSocket = server.accept();
                System.out.println("Client is connected");
                try{
                    String clientWord = null;
                    in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
                    out = new BufferedWriter(new OutputStreamWriter(clientSocket.getOutputStream()));
                    serverLoop(in, out, clientWord, idx);
                } finally {
                    clientSocket.close();
                    in.close();
                    out.close();
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
