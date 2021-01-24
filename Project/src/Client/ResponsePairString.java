package Client;

public class ResponsePairString extends Response {
    private String x, y;

    public ResponsePairString(int tag, String x, String y) {
        this.setTag(tag);
        this.x = x;
        this.y = y;
    }

    public String getFirst() {
        return this.x;
    }

    public String getSecond() {
        return this.y;
    }
}
