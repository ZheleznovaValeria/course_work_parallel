package server;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

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



    public static void main(String[] args) {
        pathList.add("src/resources/test/neg/");
        pathList.add("src/resources/test/pos/");
        pathList.add("src/resources/train/neg/");
        pathList.add("src/resources/train/pos/");
        pathList.add("src/resources/train/unsup/");

        List<String> files = InvertedIndex.getFileNamesList(pathList);
        int threadsNumber = 10;

        try {
            InvertedIndex idx = new InvertedIndex();
            parallelIndexing(threadsNumber, files, idx);
            idx.search(Arrays.asList(files.get(0).split(",")));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
