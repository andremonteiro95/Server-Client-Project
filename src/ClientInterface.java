/**
 * Created by André Monteiro on 05/04/2016.
 */
public interface ClientInterface extends java.rmi.Remote {
    public String readString() throws java.rmi.RemoteException;
    public int readInt() throws java.rmi.RemoteException;

}
