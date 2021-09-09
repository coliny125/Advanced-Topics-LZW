import java.util.*;
import java.io.*;
public class LZW {
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
	private HashMap <String, Integer> dict;//dict has no max bound. No set byte length;
	private File orginalFile;
	private int dictLength;
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
	
	public HashMap<String,Integer> getMap ()
	{
		return dict;
	}
	
	public void compress () throws IOException
	{
		try 
		{
			BufferedReader br = new BufferedReader (new FileReader(orginalFile));
			FileOutputStream fileWriter = new FileOutputStream("/Users/Alex/Desktop/Advanced-Topics-CS/LZW/compressedFile.bin");
			String current = ""+ (char)br.read();
			int asciiVal;
			String binaryString = "";
			br.mark(100);
			while (br.read() != -1)
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
					binaryString += LZW.toBinary(asciiVal, 8);
					dict.put (current+next,dictLength);//not dictLength+1 because dictLength is always one value greater than dictionary index.
					current = next;
					dictLength++;
				}
				br.mark(100);
			}
			asciiVal = dict.get(current);
			binaryString += LZW.toBinary(asciiVal, 8);
			char[] encodedChars = binaryString.toCharArray();
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
	
	public static String toBinary(int x, int len)
    {
        if (len > 0)
        {
            return String.format("%" + len + "s",
                            Integer.toBinaryString(x)).replaceAll(" ", "0");
        }
 
        return null;
    }
	
	//method below taken from internet;
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
	
	public static void main (String [] args) throws IOException
	{
		File f = new File ("/Users/Alex/Desktop/Advanced-Topics-CS/LZW/lzw-file3.txt");
		LZW l = new LZW(f);
		l.compress();
		
	}
}