package Client;

public class ResponsePair extends Response {
    private int x, y;

    public ResponsePair(int tag, int x, int y) {
        this.setTag(tag);
        this.x = x;
        this.y = y;
    }
}
