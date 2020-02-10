Steps to run the BloomFilter

1)Extract the BloomFilter.zip file

2)makesure you these files in the same folder.
	BloomFilterConfig.txt
	BloomFilterInput.txt
	BloomFilterClient.java
	BloomFilterInterface.java
	BloomFilterServer.java
	
3) open terminal and navigate to the same folder and run the following commands.
	To run the server enter the following command:
	javac BloomFilterServer.java
	java BloomFilterServer BloomFilterConfig.txt localhost 8080
	javac BloomFilterClient.java
	java BloomfilterClient BloomFilterInput.txt localhost 8080
	
You can see the result for the input file.	