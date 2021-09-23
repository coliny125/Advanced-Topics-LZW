import java.util.*;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.security.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Date;
public class LZW {
	//All static variables used for fromAscii method, which was taken from http://www.java2s.com/Tutorial/Java/0180__File/Translatesbetweenbytearraysandstringsof0sand1s.htm
	private static final byte[] EMPTY_BYTE_ARRAY = new byte[0];
	private static final int BIT_0 = 1;
	/** Mask for bit 1 of a byte. */
	private static final int BIT_1 = 0x02;
	/** Mask for bit 2 of a byte. */
	private static final int BIT_2 = 0x04;
	/** Mask for bit 3 of a byte. */
	private static final int BIT_3 = 0x08;
	/** Mask for bit 4 of a byte. */
	private static final int BIT_4 = 0x10;
	/** Mask for bit 5 of a byte. */
	private static final int BIT_5 = 0x20;
	/** Mask for bit 6 of a byte. */
	private static final int BIT_6 = 0x40;
	/** Mask for bit 7 of a byte. */
	private static final int BIT_7 = 0x80;
	private static final int[] BITS = { BIT_0, BIT_1, BIT_2, BIT_3, BIT_4, BIT_5, BIT_6, BIT_7 };
	
	private HashMap <String, Integer> dict;//dictionary to store ascii characters. Has no max bound and no set byte length;
	private File orginalFile;//input file that user wants to read in and compress
	private int dictLength;//length of dictionary
	
	//constructor takes in a file to be compressed and intializes the dictionary of ascii characters.
	public LZW (File original)
	{
		orginalFile = original;
		this.dict = new HashMap <String,Integer>();
		dictLength = 256;
		for (int k = 0; k < dictLength; k++)
		{
			dict.put("" + (char)k, k);
		}
	}

	
	public void compress () throws IOException
	{
		try 
		{
			//This reader is used to read input file
			BufferedReader br = new BufferedReader (new FileReader(orginalFile));
			//This stream is used to write to binary file
			FileOutputStream fileWriter = new FileOutputStream("/Users/apple/eclipse-workspace/Advanced-Topics-LZW/compressedFile.txt");
			//current tracks the current character or string being checked in the algorithm
			String current = ""+ (char)br.read();
			//asciiVal is used to store the ascii value of the variable "current"
			int asciiVal;
			//binaryString is a concatentation of all the different asciiVal values in the while loop. The string contains only 1's and 0's
			String binaryString = "";
			//the mark() and reset() methods are used to make sure no characters are skipped in the buffered stream. This all works so no need to worry about it.
			br.mark(100);
			while (br.read() != -1)//checks that the input file still has things to read
			{
				br.reset();
				String next = "" + (char)br.read();
				if (dict.containsKey(current+next))
				{
					current = current + next;
				}
				else
				{
					asciiVal = dict.get(current);
					binaryString += LZW.toBinary(asciiVal, 8);//toBinary(int number, int length) number is turned into a string of 1's and 0's. length is length of binary string made by this method
					dict.put (current+next,dictLength);//not dictLength+1 because dictLength is always one value greater than dictionary index.
					current = next;
					dictLength++;
				}
				br.mark(100);
			}
			
			//these next two lines are neccessary to capture the bits of the last character of the file.
			asciiVal = dict.get(current);
			binaryString += LZW.toBinary(asciiVal, 8);
			
			//initializes charArray needed for fromAscii method
			char[] encodedChars = binaryString.toCharArray();
			//see fromAscii method. Resulting byte[] can be used to write to .bin file
			byte[] encodedBytes = LZW.fromAscii(encodedChars);
			fileWriter.write(encodedBytes);
			fileWriter.close();
			br.close();
			System.out.println ("File compressed");
		} 
		catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}
	
	//method also taken from internet.
	public static String toBinary(int x, int len)
    {
        if (len > 0)
        {
            return String.format("%" + len + "s",
                            Integer.toBinaryString(x)).replaceAll(" ", "0");
        }
 
        return null;
    }
	
	//method taken from http://www.java2s.com/Tutorial/Java/0180__File/Translatesbetweenbytearraysandstringsof0sand1s.htm
	public static byte[] fromAscii(char[] ascii) {
	    if (ascii == null || ascii.length == 0) {
	      return EMPTY_BYTE_ARRAY;
	    }
	    // get length/8 times bytes with 3 bit shifts to the right of the length
	    byte[] l_raw = new byte[ascii.length >> 3];
	    /*
	     * We decrease index jj by 8 as we go along to not recompute indices using
	     * multiplication every time inside the loop.
	     */
	    for (int ii = 0, jj = ascii.length - 1; ii < l_raw.length; ii++, jj -= 8) {
	      for (int bits = 0; bits < BITS.length; ++bits) {
	        if (ascii[jj - bits] == '1') {
	          l_raw[ii] |= BITS[bits];
	        }
	      }
	    }
	    return l_raw;
	  }
	
	public static final int BUFFER_SIZE = 8;//number of bits placed at a time into binaryString
	public static String binaryString = "";//String of 1s and 0s
	public static int[] decimalArray;//array of decimals corresponding to each symbol, or 9 bits
	public static Map <Integer, String> decodeDict = new HashMap<Integer,String>(); //same as dict but with decimal Integer as key and String as value.
	public static String decodedString = "";//final original String
	
	public void decompress () throws IOException
	{
		output();
		makeDecimalArray();
		buildDictionary();
		
		//writing decodedString to .txt file
		try (PrintWriter out = new PrintWriter("decompressedFile.txt")) {
		    //out.println(text);
		}
	}
	
	//takes in binary file and returns String of 0's and 1's.
	//taken from https://www.codejava.net/java-se/file-io/how-to-read-and-write-binary-files-in-java
	
	public static String output()
	{
		try (
				InputStream binaryStream = new FileInputStream("/Users/ava/eclipse-workspace/Alex's LZW/Advanced-Topics-LZW-main/Advanced-Topics-LZW/compressedFile.bin");				
	        ) {
	            byte[] buffer = new byte[BUFFER_SIZE];
	 
	            while (binaryStream.read(buffer) != -1) {
	            	
	            		String s = new String(buffer, StandardCharsets.UTF_8);
	            		binaryString = s;
	            		return binaryString;
	            }
	 
	        } catch (IOException ex) {
	        	return binaryString;
	        }
		return binaryString;
	}
	
	public static void makeDecimalArray ()
	{
		decimalArray = new int [binaryString.length()/9];
		
		for (int i = 0; i < binaryString.length()-9; i+=9)
		{
			String binSubString = binaryString.substring(i, i+9);
			decimalArray[i/9] = Integer.parseInt(binSubString,2);
		}
	}
	
	public static void buildDictionary()//changes decodeDict and decodedString
	{
		for (int j = 0; j < 256; j++)
		{
			decodeDict.put(j, "" + (char)j);
		}
		
		if (decimalArray.length != 0){
			String current = ""+ decodeDict.get(decimalArray[0]);
			for (int i = 1; i < decimalArray.length; i++)
			{
				String next = ""+decodeDict.get(decimalArray[i]);
				String firstNextChar = ""+next.charAt(0);
				String cAndF = current + firstNextChar;
				
				decodeDict.put(255+i, cAndF);
				
				decodedString += current;
			}
		}
	}
	
	public static void main (String [] args) throws IOException
	{
		File f = new File ("/Users/apple/eclipse-workspace/Advanced-Topics-LZW/lzw-file1.txt");
		LZW l = new LZW(f);
		System.out.println(System.currentTimeMillis()); 
		l.compress();
		System.out.println(System.currentTimeMillis()); 
		l.decompress();
		
	}
}