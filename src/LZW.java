import java.util.*;
import java.io.*;
public class LZW {
	
	private HashMap <String, Integer> dict;
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
			BufferedWriter fileWriter = new BufferedWriter(new FileWriter("/Users/Alex/Desktop/Advanced-Topics-CS/LZW/compressedFile.txt"));
			String current = ""+ (char)br.read();
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
					fileWriter.write(dict.get(current) + " ");
					System.out.println (current);
					dict.put (current+next,dictLength);//not dictLength+1 because dictLength is always one value greater than dictionary index.
					current = next;
					dictLength++;
				}
				br.mark(100);
			}
			System.out.println (dict.get(current));
			fileWriter.write(""+dict.get(current));
			fileWriter.close();
			br.close();
			System.out.println ("File successfully compressed");
		} 
		catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}
	
	public static void main (String [] args) throws IOException
	{
		File f = new File ("/Users/Alex/Desktop/Advanced-Topics-CS/LZW/lzw-file1.txt");
		LZW l = new LZW(f);
		l.compress();
		
	}
}