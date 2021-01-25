package Util;

import Util.Response;

public class ResponseBool extends Response {
    private boolean bool;

    public ResponseBool(int tag, boolean bool) {
        this.setTag(tag);
        this.bool = bool;
    }

    public boolean getBool() {
        return bool;
    }

    public void setBool(boolean bool) {
        this.bool = bool;
    }
}
