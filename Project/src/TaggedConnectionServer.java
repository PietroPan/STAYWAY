import Client.*;

import java.io.IOException;
import java.net.Socket;

public class TaggedConnectionServer extends TaggedConnection {

    public TaggedConnectionServer(Socket s) throws IOException {
        super(s);
    }

    public void loginReply(int i) {
        writeLock.lock();
        try {
            out.writeInt(0);
            out.writeInt(i);
            out.flush();

        } catch (IOException e) {
            // caso algo corra mal
        }
        finally {
            writeLock.unlock();
        }
    }

    public void registerReply(boolean bool) {
        try {
            writeLock.lock();
            out.writeInt(1);
            out.writeBoolean(bool);
            out.flush();
        } catch (IOException e) {}
        finally {
            writeLock.unlock();
        }
    }

    // O server responde quando o user muda de posição?
    public void setLocationReply(int x,int y) {

    }

    public void sendNUsersLoc(int res) {
        try {
            writeLock.lock();
            out.writeInt(3);//Pedido com tag getNUsersLoc
            out.writeInt(res);
            out.flush();
        } catch (IOException e) {}
        finally {
            writeLock.unlock();
        }
    }

    //
    public void locationAvailable(int x,int y){
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

    public void warningIsInfected(){
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

    public void showMap(int[][][] matrix, boolean res){
        try {
            writeLock.lock();
            out.writeInt(6);//Pedido com tag showMap
            out.writeBoolean(res);
            if (res)
                for (int i = 0; i < 10; i++) {
                    for (int j = 0; j < 10; j++) {
                        out.writeInt(matrix[0][i][j]);
                        out.writeInt(matrix[1][i][j]);
                    }
                }
            //out.writeUTF(this.name);
            out.flush();
        } catch (IOException e) {}
        finally {
            writeLock.unlock();
        }
    }


    public Response receive() throws IOException {
        Response res = null;
        readLock.lock();

        try {
            int op = in.readInt();

            switch (op) {
                case 0:   // Login
                    res = new ResponsePairString(0, in.readUTF(), in.readUTF());
                    break;
                case 1:   //Register
                    res = new ResponseStringsInts(1,in.readUTF(),in.readUTF(),in.readInt(),in.readInt());
                    //res = new ResponsePairString(1, in.readUTF(), in.readUTF());
                    break;
                case 2: // Update Location
                    res = new ResponsePair(2, in.readInt(), in.readInt());
                    break;
                case 3:   //People at location
                    res = new ResponsePair(3, in.readInt(), in.readInt());
                    break;
                case 4:   //Location available
                    res = new ResponsePair(4,in.readInt(), in.readInt());
                    break;
                case 5:   //Infected
                    res = new ResponseString(5, null);//Não precisa de nada
                    break;
                case 6:   //Map
                    res = new ResponseString(6, null);//Não precisa de nada
                    break;
                case 7:
                    res = new ResponseString(7,null);
                    break;
                case 8:
                    res = new ResponseString(8, null);
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
