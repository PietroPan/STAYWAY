package Client;

public class ResponseString extends Response {
    String str;

    public ResponseString (int tag, String str) {
        this.setTag(tag);
        this.str = str;
    }
}
