
import java.io.File;
import java.io.FileNotFoundException;
import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.util.Scanner;

public class BloomfilterClient {
	
	private static BloomFilterRemoteInterface remoteInterface;
	
	public static void main(String[] args) throws MalformedURLException, RemoteException, NotBoundException, FileNotFoundException {
		
		int pnumber = Integer.parseInt(args[2]);
		String ip = args[1];
		String s = args[0];		
		remoteInterface = (BloomFilterRemoteInterface)Naming.lookup("//"+ip+":"+pnumber+"/MyServer");
		String slash = "";
		String OS = System.getProperty("os.name").toLowerCase();
		if(OS.indexOf("win") >= 0)
			slash ="\\";
		else if((OS.indexOf("mac") >= 0 || OS.indexOf("nux") >= 0))
			slash="/";
		//Scanner in = new Scanner(System.in);
		//System.out.println("Enter the input file path ");
		//String s = in.nextLine();
		System.out.println("Reading and executing the commands");
		//in.close();
		File file = new File(System.getProperty("user.dir") + slash + s);
		Scanner sc = new Scanner(file);
		int fp = 0;
		int i = 0; 
		while(sc.hasNext()) {
			String[] line = sc.nextLine().split(" ");
			switch(line[0].toLowerCase()) {
			case"add":
				System.out.println("adding "+ line[1]);
				remoteInterface.add(line[1]);
				i++;
				break;
			case"test":
				System.out.println("testing "+ line[1]);
				Boolean b = remoteInterface.isPresent(line[1]);
				if(b==true) {
					fp++;
				}
				int[] filter = remoteInterface.getBloomFilter();
				System.out.println("result for string "+ line[1] +" is " + b.toString());
				System.out.println("Filter is:");
				for(int j = 0; j <filter.length;j++)
					System.out.print(filter[j]+" ");
				System.out.println("");
				break;
			case"reset":
				System.out.println("reseting the bloom filter");
				remoteInterface.Reset();
				break;
				
			}
		}
		//double fprate = ((fp*100)/(69903-i));
		//System.out.println("fp rate = " + fprate);

	}
}
