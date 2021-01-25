package Util;

import Util.Response;

public class ResponseString extends Response {
    String str;

    public ResponseString (int tag, String str) {
        this.setTag(tag);
        this.str = str;
    }

    public String getStr() {
        return str;
    }

    public void setStr(String str) {
        this.str = str;
    }
}
