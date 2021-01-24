package Client;

public class ResponseIntMatrix extends Response {
    int [][][] matrix;


    public ResponseIntMatrix (int tag, int [][][] matrix) {
        this.setTag(tag);
        this.matrix = matrix;
    }

}
