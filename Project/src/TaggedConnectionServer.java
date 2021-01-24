import Client.*;

import java.io.IOException;
import java.net.Socket;

public class TaggedConnectionServer extends TaggedConnection {

    public TaggedConnectionServer() throws IOException {
        super();
    }

    public TaggedConnectionServer(Socket s) throws IOException {
        super(s);
    }

    public void loginReply(boolean bool) {
        lock.lock();
        try {
            out.writeInt(0);
            out.writeBoolean(bool);
            out.flush();

        } catch (IOException e) {
            // caso algo corra mal
        }
        finally {
            lock.unlock();
        }
    }

    public void registerReply(boolean bool) {
        System.out.println("vou respinder");
        lock.lock();
        try {
            out.writeInt(1);
            out.writeBoolean(bool);
            out.flush();
        } catch (IOException e) {}
        finally {
            lock.unlock();
        }
    }

    // O server responde quando o user muda de posição?
    public void setLocationReply(int x,int y) {

    }

    public void sendNUsersLoc(int res) {
        try {
            lock.lock();
            out.writeInt(3);//Pedido com tag getNUsersLoc
            out.writeInt(res);
            out.flush();
        } catch (IOException e) {}
        finally {
            lock.unlock();
        }
    }

    //
    public void locationAvailable(int x,int y){
        try {
            lock.lock();
            out.writeInt(4);//Pedido com tag waitLocation
            out.writeInt(x);
            out.writeInt(y);
            out.flush();
        } catch (IOException e) {}
        finally {
            lock.unlock();
        }
    }

    public void warningIsInfected(){
        try {
            lock.lock();
            out.writeInt(5);//Pedido com tag isInfected
            out.writeUTF(this.name);
            out.flush();
        }catch (IOException e) {}
        finally {
            lock.unlock();
        }
    }

    public void showMap(){
        try {
            lock.lock();
            out.writeInt(6);//Pedido com tag showMap
            //out.writeUTF(this.name);
            out.flush();
        } catch (IOException e) {}
        finally {
            lock.unlock();
        }
    }


    public Response receive() throws IOException {
        Response res = null;
        System.out.println("estou no receive");
        lock.lock();
        try {
            int op = in.readInt();
            System.out.println(op);
            switch (op) {
                case 0:   // Login
                    res = new ResponsePairString(0, in.readUTF(), in.readUTF());
                    break;
                case 1:   //Register
                    System.out.println("register");
                    res = new ResponsePairString(1, in.readUTF(), in.readUTF());
                    break;
                case 2: // Update Location

                    break;
                case 3:   //People at location
                    res = new ResponsePair(3, in.readInt(), in.readInt());
                    break;
                case 4:   //Location available
                    res = new ResponsePair(4,in.readInt(), in.readInt());
                    break;
                case 5:   //Infected
                    res = new ResponseString(5, in.readUTF());
                    break;
                case 6:   //Map
                    res = new ResponseString(6, in.readUTF());
                    break;

                default:
                    break;


            }
            return res;
        }
        finally {
            lock.unlock();
        }
    }
}
