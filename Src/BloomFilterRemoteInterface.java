
import java.rmi.Remote;
import java.rmi.RemoteException;

public interface BloomFilterRemoteInterface extends Remote {
	
	public void add(String s) throws RemoteException;
	
	public Boolean isPresent(String s) throws RemoteException;
	
	public void Reset() throws RemoteException ;

	public int[] getBloomFilter() throws RemoteException;
	
}
