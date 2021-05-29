package server;

public class Tuple {
    public String filePath;
    public int fileNo;
    public int position;

    public Tuple(String filePath, int fileNo, int position) {
        this.filePath = filePath;
        this.fileNo = fileNo;
        this.position = position;
    }
}
