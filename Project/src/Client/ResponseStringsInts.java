package Client;

public class ResponseStringsInts extends Response {
    private String str1,str2;
    private int i1,i2;

    public ResponseStringsInts(int tag, String str1, String str2, int i1, int i2){
        this.setTag(tag);
        this.str1=str1;
        this.str2=str2;
        this.i1=i1;
        this.i2=i2;
    }

    public String getFirstStr() {
        return this.str1;
    }

    public String getSecondStr() {
        return this.str2;
    }

    public int getFirstInt() {return  this.i1;}

    public int getSecondInt() {return  this.i2;}
}
