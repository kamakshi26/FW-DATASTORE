package filengine;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Scanner;

import org.json.JSONException;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

public class DataStore 
{
	@SuppressWarnings("unchecked")
	public static void main(String args[]) throws JSONException
	{
		int ch,check,size;
		String key = new String();
		Scanner in = new Scanner(System.in);
		while(true)
		{
			System.out.print("\n1.Createdata\n2.Read\n3.Delete\n4.Exit\nEnter option:");
			ch = in.nextInt();
			switch(ch)
			{
				case 1: check = checkFileSize();
						size=0;
						if(check==0)
						{
							System.out.println("Enter key:");
							key = in.next();
							if(key.length()<=32)
							{
							check = checkKeyExist(key);
							if(check==1)
							{
								JSONObject value = new JSONObject();
								System.out.println("Enter number of elements in the value JSON object");
								int n = in.nextInt();
								for(int i=0;i<n;i++)
								{
									System.out.println("Enter the key-value pair:");
									String k = in.next();
									String val = in.next();
									value.put(k,val);
									size += k.length() + val.length();
								}
								if(size<=16384) //checking JSON object size 
								{
									System.out.println(" want to set a Time-To-Live property?(y/n)");
									char ch = in.next().charAt(0);
									long ttl=-1; //if ttl is -1 then no time-to-live is mentioned
									Date date = new Date();
									long time = date.getTime();
									if((ch=='y')||(ch=='Y'))
									{
										System.out.println("Enter Time-To-Live:");
										ttl = in.nextLong();
										time += (ttl * 1000);
									}
									Timestamp ts = new Timestamp(time);
									String timestamp = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(ts);//format(new Date());
									create(key,value,timestamp,ttl);
								}
							}
							else
								System.err.println("Key already exist");
							}
							else
								System.err.println("Size of the string is greater than 32 characters");
						}
						else
							System.err.println("File size is exceeding 1GB");
						break;
				case 2:System.out.println("Enter the key to read the value:");
						key = in.next();
						check = checkKeyExist(key);
						if(check==1)
							read(key);
						else
							System.err.println("Key does not exist");
						break;
				case 3:System.out.println("Enter the key to delete:");
						key = in.next();
						delete(key);
						break;
				case 4:System.out.println("Exit");
						System.exit(0);
						break;
				default: System.out.println("Invalid choice");
						break;
			}
		}
	}
	
	//Checks if key exist
	public static int checkKeyExist(String key)
	{
		File data = new File("data.txt");
		String line = new String();
		int flag=1;
		try(BufferedReader reader = new BufferedReader(new FileReader(data));)
		{
			Date date = new Date();
			long ts = date.getTime();
				while((line=reader.readLine())!=null)
				{
					JSONObject jobj =  (JSONObject) new JSONParser().parse(line);
					String key1 = (String) jobj.get("key");
					String timestamp = (String) jobj.get("time");
					long ttl = (long) jobj.get("ttl");
					Timestamp t = Timestamp.valueOf(timestamp);
					long ts1 = t.getTime();
					//System.out.println(key + " " + key1 + " " +ts + " " + ts1 + " " + ttl);
					if((key.equals(key1))&&(ts>ts1)&&(ttl==-1))
					{
						flag=0; //flag states that key exist
						System.out.println("hiii");
						break;
					}
				}
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		return flag;
	}
	
	//check the size of the file
	public static int checkFileSize()
	{
		int flag=1;
		File data = new File("data.txt");
		try(BufferedReader reader = new BufferedReader(new FileReader(data));)
		{
			if(data.length()<1073741824)//size of the file should be less than 1GB 
				flag=0;
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		return flag;
	}
	
	//creating a key and value pair
	@SuppressWarnings("unchecked")
	public static void create(String key,JSONObject value,String ts,long ttl)
	{
		File data = new File("data.txt");
		try(FileWriter writer = new FileWriter(data,true);
			BufferedReader reader = new BufferedReader(new FileReader(data));)
		{
					JSONObject obj = new JSONObject();
					obj.put("key", key);
					obj.put("value", value);
					obj.put("time", ts);
					obj.put("ttl", ttl);
					writer.write(obj.toString());
					writer.flush();
					writer.write("\n");
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
	}
	
	
	//given a key, display the value of the key
	public static void read(String key)
	{
		File data = new File("data.txt");
		String line = new String();
		int flag=0;
		try(BufferedReader reader = new BufferedReader(new FileReader(data));)
		{
			while((line=reader.readLine())!=null)
			{
				JSONObject jobj =  (JSONObject) new JSONParser().parse(line);
				String key1 = (String) jobj.get("key");
				if(key.equals(key1))
				{
					JSONObject value = (JSONObject) jobj.get("value");
					System.out.println("Key: " + key +"\nValue: " + value);
					break;
				}
			}
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
	}
	
	
	//given a key, delete the particular key
	public static void delete(String key)
	{
		File data = new File("data.txt");
		File temp = new File("temp.txt");
		String line = new String();
		int flag=0;
		try(BufferedReader reader = new BufferedReader(new FileReader(data));
			BufferedWriter writer = new BufferedWriter(new FileWriter(temp));)
		{
			while((line=reader.readLine())!=null)
			{
				JSONObject jobj =  (JSONObject) new JSONParser().parse(line);
				String key1 = (String) jobj.get("key");
				if(!key.equals(key1))
				{
					writer.write(line);
					writer.write("\n");
					flag=1;
				}
			}
			writer.close();
			reader.close();
			data.delete();
			temp.renameTo(data);
			if(flag==0)
				System.err.println("The given key does not exist.");
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
	}
}
