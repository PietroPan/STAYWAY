package Connection;

import Util.*;

import java.io.*;
import java.net.Socket;

public class TaggedConnectionClient extends TaggedConnection {

    public TaggedConnectionClient(Socket s) throws IOException {
        super(s);
    }


    public void login(String name, String pass) throws IOException, InterruptedException {
        writeLock.lock();
        try {
            out.writeInt(0); //Pedido com tag login
            out.writeUTF(name);
            out.writeUTF(pass);
            out.flush();
        }
        catch (IOException e) {

        }
        finally {
            writeLock.unlock();
        }
    }


    public void register(String name,String pass, int x, int y) {
        try {
            writeLock.lock();
            out.writeInt(1); //Pedido com tag register
            out.writeUTF(name);
            out.writeUTF(pass);
            out.writeInt(x);
            out.writeInt(y);
            out.flush();
        } catch (IOException e) {}
        finally {
            writeLock.unlock();
        }
    }

    public void setLocation(int x,int y) {
        try {
            writeLock.lock();
            out.writeInt(2);//Pedido com tag setLocation
            //out.writeUTF(this.name);
            out.writeInt(x);
            out.writeInt(y);
            out.flush();
        } catch (IOException e) {}
        finally {
            writeLock.unlock();
        }
    }

    public void getNUsersLoc(int x,int y) {
        try {
            writeLock.lock();
            out.writeInt(3);//Pedido com tag getNUsersLoc
            out.writeInt(x);
            out.writeInt(y);
            out.flush();
        } catch (IOException e) {}
        finally {
            writeLock.unlock();
        }
    }

    public void waitLocation(int x,int y){
        try {
            writeLock.lock();
            out.writeInt(4);//Pedido com tag waitLocation
            out.writeInt(x);
            out.writeInt(y);
            out.flush();
        } catch (IOException e) {}
        finally {
            writeLock.unlock();
        }
    }

    public void isInfected(){
        try {
            writeLock.lock();
            out.writeInt(5);//Pedido com tag isInfected
            //out.writeUTF(this.name);
            out.flush();
        }catch (IOException e) {}
        finally {
            writeLock.unlock();
        }
    }

    public void showMap(){
        try {
            writeLock.lock();
            out.writeInt(6);//Pedido com tag showMap
            //out.writeUTF(this.name);
            out.flush();
        } catch (IOException e) {}
        finally {
            writeLock.unlock();
        }
    }

    public void changeVip(){
        try{
            writeLock.lock();
            out.writeInt(7);
            out.flush();
            writeLock.unlock();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void waitInfected(){
        try {
            writeLock.lock();
            out.writeInt(8);
            out.flush();
            writeLock.unlock();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void logout(){
        try{
            writeLock.lock();
            out.writeInt(9);
            out.flush();
            writeLock.unlock();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void quit(){
        try{
            writeLock.lock();
            out.writeInt(10);
            out.flush();
            writeLock.unlock();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }




    public Response receive() throws IOException {
        Response res = null;

        readLock.lock();
        try {
            switch (in.readInt()) {
                case 0:   // Login
                    res = new ResponseInt(0, in.readInt());
                    break;
                case 1:   //Register
                    res = new ResponseBool(1, in.readBoolean());
                    break;
                case 2: // Update Location
                    break;
                case 3:   //People at location
                    res = new ResponseInt(3, in.readInt());
                    break;
                case 4:   //Location available
                    res = new ResponsePair(4,in.readInt(), in.readInt());
                    break;
                case 5:   //Infected
                    res = new ResponseString(5, in.readUTF());
                    break;
                case 6:   //Map
                    int [][][] matrix = null;
                    if (in.readBoolean()) {
                        matrix = new int [2][10][10];
                        for (int i=0;i<10;i++){
                            for (int j=0;j<10;j++){
                                matrix[0][i][j] = in.readInt();
                                matrix[1][i][j] = in.readInt();
                            }
                        }
                    }
                    res = new ResponseIntMatrix(6, matrix);
                    break;

                default:
                    break;


            }
            return res;
        }
        finally {
            readLock.unlock();
        }
    }
}
