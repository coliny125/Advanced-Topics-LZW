import java.io.*;
public class byteTester {
	private static String s;
	byte[] b;
	public byteTester(String s)
	{
		this.s = s;
	}
	
	public static void main (String[] args) throws IOException
	{
		FileOutputStream writer = new FileOutputStream("/Users/Alex/Desktop/Advanced-Topics-CS/LZW/test.bin");
		byteTester bt = new byteTester("a");
		byte[] array = s.getBytes();
		writer.write(array);
		writer.close();
	
}
}
