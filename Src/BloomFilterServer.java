import java.io.File;
import java.io.FileNotFoundException;
import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.server.UnicastRemoteObject;
import java.util.*;

public class BloomFilterServer extends UnicastRemoteObject implements BloomFilterRemoteInterface{

	private static final long serialVersionUID = 1L;
	private int length;
	private int numberOfHashFunctions;
	private int bloomFilter[];
	

	protected BloomFilterServer(String s) throws RemoteException, FileNotFoundException {
		super();
		String slash = "";
		String OS = System.getProperty("os.name").toLowerCase();
		if(OS.indexOf("win") >= 0)
			slash ="\\";
		else if((OS.indexOf("mac") >= 0 || OS.indexOf("nux") >= 0))
			slash="/";
		File file = new File(System.getProperty("user.dir") + slash +  s);
		Scanner sc = new Scanner(file);
		this.length = Integer.parseInt(sc.next().split("=")[1]);
		this.numberOfHashFunctions = Integer.parseInt(sc.next().split("=")[1]);
		sc.close();
		bloomFilter = new int[length]; 
	}

	@Override
	public int[] getBloomFilter() {
		return this.bloomFilter;
	}

	@Override
	public void add(String s) throws RemoteException {
		long[] pos = getHashValues(s);
		for(int i = 0; i < pos.length; i++) 
		{	
			this.bloomFilter[(int) pos[i]] = 1;
		}
	}

	@Override
	public Boolean isPresent(String s) throws RemoteException {
		Boolean ans = false;
		long[] pos = getHashValues(s);
		for(int i = 0; i < pos.length; i++) 
		{	
			if(this.bloomFilter[(int) pos[i]] == 1) {
				ans = true;
			}
		}
		return ans;
	}

	@Override
	public void Reset() throws RemoteException {
		this.bloomFilter = new int[length];
	}
	
	private long[] getHashValues(String s) {
		//rotation seeding technique
		int rotationFactor = 1;
		long[] result = new long[this.numberOfHashFunctions];
		
		for(int i = 0; i<this.numberOfHashFunctions; i++)
		{
			result[i] = sfold(s,this.length);
			s = s.substring(rotationFactor) + s.substring(0, rotationFactor); 
		}
		return result;
	}
	
	private long sfold(String s, int M) {
	     int intLength = s.length() / 4;
	     long sum = 0;
	     for (int j = 0; j < intLength; j++) {
	       char c[] = s.substring(j * 4, (j * 4) + 4).toCharArray();
	       long mult = 1;
	       for (int k = 0; k < c.length; k++) {
		 sum += c[k] * mult;
		 mult *= 256;
	       }
	     }

	     char c[] = s.substring(intLength * 4).toCharArray();
	     long mult = 1;
	     for (int k = 0; k < c.length; k++) {
	       sum += c[k] * mult;
	       mult *= 256;
	     }

	     return(Math.abs(sum) % M);
	   }
	
	public static void main(String[] args) throws RemoteException, MalformedURLException, FileNotFoundException {
		
		String fileName = args[0];
		int pnumber = Integer.parseInt(args[2]);
		String ip = args[1];
		LocateRegistry.createRegistry(pnumber);
		Naming.rebind("//"+ip+":"+pnumber+"/MyServer", new BloomFilterServer(fileName));
		System.out.println("Server ready");
		
	}

}
