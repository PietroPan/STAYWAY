package Client;

public class Response {
    private int tag;

    public Response(){
        this.tag=-1;
    }

    public Response(int tag){
        this.tag=tag;
    }

    public int getTag() {
        return tag;
    }

    public void setTag(int t) {
        this.tag = t;
    }



}
