import java.util.*;
public class LZW {
	
	private HashMap <String, Integer> dict;
	
	public LZW ()
	{
		this.dict = new HashMap <String,Integer>();
		for (int k = 0; k < 256; k++)
		{
			dict.put("" + (char)k, k);
		}
	}
	
	public HashMap<String,Integer> getMap ()
	{
		return dict;
	}
	
	public static void main (String [] args)
	{
		LZW l = new LZW();
		HashMap<String,Integer> test = l.getMap();
		for (int i = 0; i < 256; i++)
		{
			System.out.println (""+(char)i + ", " + test.get("" + (char)i)); 
		}
		
	}
}