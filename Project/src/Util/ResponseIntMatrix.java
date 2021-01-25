package Util;

import Util.Response;

public class ResponseIntMatrix extends Response {
    int [][][] matrix;


    public ResponseIntMatrix (int tag, int [][][] matrix) {
        this.setTag(tag);
        this.matrix = matrix;
    }

    public int[][][] getMatrix() {
        return matrix;
    }

    public void setMatrix(int[][][] matrix) {
        this.matrix = matrix;
    }
}
