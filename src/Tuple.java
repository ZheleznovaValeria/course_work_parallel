public class Tuple {
    public String filePath;
    public int fileno;
    public int position;

    public Tuple(String filePath, int fileno, int position) {
        this.filePath = filePath;
        this.fileno = fileno;
        this.position = position;
    }
}
