package Util;

import Util.Response;

public class ResponseInt extends Response {
    private int nr;

    public ResponseInt(int tag, int nr) {
        this.setTag(tag);
        this.nr = nr;
    }

    public int getInt() {
        return nr;
    }

}
