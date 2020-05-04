package dbms;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Scanner;

/**
 * Database Management System (DBMS)
 * 
 * Run the program in your console to begin.
 * Keep track of files representing tables by refreshing the project.
 */
public class Database 
{
	public static void main(String[] args) throws FileNotFoundException
	{
		Scanner in = new Scanner(System.in);
		int errorCount = 0;
		
		System.out.println("Welcome to the Database Management System!\n");
		
		System.out.println("----------------------------------------");
		System.out.println("Command 1: Create Table");
		System.out.println("Command 2: Describe Table");
		System.out.println("Command 3: Insert Record");
		System.out.println("Command 4: Drop Table");
		System.out.println("Command 5: Delete Record");
		System.out.println("Command 6: Query");
		System.out.println("Command 7: Display Commands");
		System.out.println("Command 8: Exit");
		System.out.println("----------------------------------------\n");
		
		while (true)
		{
			System.out.print("Enter a command: ");
			String command = reduceSpaces(in.nextLine().trim());
			System.out.println();
			
			if (command.equalsIgnoreCase("Create Table"))
			{
				System.out.println("----------------------------------------");
				String tableName;
				while (true)
				{
					System.out.print("Enter a name for your table: ");
					tableName = eliminateSpaces(in.nextLine().trim());
					while (!isAlphaNumeric(tableName) || tableName.equals(""))
					{
						System.out.println("The table name must be alphanumeric\n");
						System.out.print("Enter a name for your table: ");
						tableName = eliminateSpaces(in.nextLine().trim());
					}
				
					File f = new File(tableName + ".tb");
				
					if (!f.exists())
					{
						break;
					}
					System.out.println("Table already exists!");
				}
				
				Table t = new Table(tableName);
				
				while (true)
				{
					System.out.print("\nEnter an attribute for " + tableName + ".tb: ");
					String attributeName = eliminateSpaces(in.nextLine().trim());
					while (!isAlphaNumeric(attributeName) || attributeName.equals(""))
					{
						System.out.println("An attribute name must be alphanumeric\n");
						System.out.print("Enter a name for an attribute: ");
						attributeName = eliminateSpaces(in.nextLine().trim());
					}
				
					System.out.println("\n1: Integer");
					System.out.println("2: Double");
					System.out.println("3: Boolean");
					System.out.println("4: String");
					System.out.print("Choose a data type for " + attributeName + ": ");
					String dataType = in.nextLine().trim();
					while (!isNumeric(dataType, true) || dataType.equals("") || dataType.length() > 1)
					{
						System.out.print("\nEnter 1-4 for one of the above data types: ");
						dataType = in.nextLine().trim();
					}
					
					t.addAttribute(new Attribute(attributeName, Integer.parseInt(dataType)));
					
					System.out.print("\nDo you want to add additional attributes? (y/n): ");
					String status = in.nextLine().trim();
					
					while (!status.equalsIgnoreCase("y") && !status.equalsIgnoreCase("n"))
					{
						System.out.print("(y/n): ");
						status = in.nextLine().trim();
					}
					
					if (status.equalsIgnoreCase("n"))
					{
						break;
					}
				}
				
				String header = "[Attribute-Count:" + t.getAttributeList().size() + "] ";
				for (int i = 0; i <= t.getAttributeList().size() - 1; i++)
				{
					header += "[" + t.getAttribute(i).getName() + ":" 
				           + t.getAttribute(i).getType(t.getAttribute(i).getType()) + "]";
				}
				
				header += " [Record-Count:" + t.getRecordCount() + "]";
				
				try
				{
					FileWriter file = new FileWriter(tableName + ".tb");
					file.write(header);
					file.close();
				}
				catch (IOException e)
				{
					e.printStackTrace();
				}
				
				System.out.println("\nTable successfully created!");
				System.out.println("----------------------------------------");
			}
			else if (command.equalsIgnoreCase("Describe Table"))
			{
				System.out.println("----------------------------------------");
				System.out.print("Choose a table to describe: ");
				String tableName = eliminateSpaces(in.nextLine().trim());
				
				File f = new File(tableName + ".tb");
				if (f.exists())
				{
					System.out.println("Table: " + tableName);
					
					Scanner fileReader = new Scanner(f);
					String header = fileReader.nextLine();
					fileReader.close();
					
					String attributeName = "";
					String attributeType = "";
					int attributeCount = 1;
					int j = 0;
					
					for (int i = 0; i <= header.length() - 1; i++)
					{
						if (header.charAt(i) == '[' && i != 0 && header.charAt(i + 1) != 'R')
						{
							j = i + 1;
							boolean flag = false;
							
							while (header.charAt(j) != ']')
							{	
								if (header.charAt(j) == ':')
								{
									flag = true;
									j++;
								}
								
								if (!flag)
								{
									attributeName = attributeName + header.charAt(j);
								}
								else
								{
									attributeType = attributeType + header.charAt(j);
								}
								j++;
							}
							
							System.out.print("Attribute " + attributeCount++ + ": ");
							System.out.print(attributeName + ", " + attributeType);
							System.out.println();
							
							attributeName = "";
							attributeType = "";
							i = j;
						}
					}
					
					String recordCount = getRecordCount(header);
					System.out.println("Record Count: " + recordCount);
				}
				else
				{
					System.out.println("Table does not exist!");
				}
				
				System.out.println("----------------------------------------");
			}
			else if (command.equalsIgnoreCase("Insert Record"))
			{
				System.out.println("----------------------------------------");
				System.out.print("Choose a table to insert a record into: ");
				
				String tableName = eliminateSpaces(in.nextLine().trim());
				
				File f = new File(tableName + ".tb");
				if (f.exists())
				{
					System.out.println("Table: " + tableName);
					Table t = new Table(tableName);
					
					Scanner fileReader = new Scanner(f);
					String header = fileReader.nextLine();
					fileReader.close();
					ArrayList<String> recordHolder = new ArrayList<String>();
					
					getAttributes(header, t);
					
					for (int i = 0; i <= t.getAttributeList().size() - 1; i++)
					{
						System.out.print(t.getAttribute(i).getName() + ": ");
						String value = eliminateSpaces(in.nextLine().trim());
						while (!isAlphaNumeric(value) || value.equals(""))
						{
							System.out.println("A record must be alphanumeric\n");
							System.out.print(t.getAttribute(i).getName() + ": ");
							value = eliminateSpaces(in.nextLine().trim());
						}
						recordHolder.add(value);
					}
					
					String record = "{";
					
					for (int i = 0; i <= recordHolder.size() - 1; i++)
					{
						if (i != recordHolder.size() - 1)
						{
							record += recordHolder.get(i) + " | ";
						}
						else
						{
							record += recordHolder.get(i);
						}
					}
					
					record += "}";
					
					Scanner input = new Scanner(f);
					ArrayList<String> list = new ArrayList<String>();

					while (input.hasNextLine()) 
					{
					    list.add(input.nextLine());
					}
					
					int pos = 0;
					for (int i = list.get(0).length() - 1; i >= 0; i--)
					{
						if (list.get(0).charAt(i - 1) == ':')
						{
							pos = i;
							break;
						}
					}
					
					String line1 = list.get(0).substring(0, pos) + list.size() + "]";
					input.close();
					PrintWriter printWriter = new PrintWriter(new FileOutputStream(tableName + ".tb"), false);
					printWriter.println(line1);
					for (int i = 1; i <= list.size() - 1; i++)
					{
						printWriter.println(list.get(i));
					}
					
					printWriter.println(record);
					printWriter.close();
				}
				else
				{
					System.out.println("Table does not exist!");
				}
				System.out.println("----------------------------------------");
			}
			else if (command.equalsIgnoreCase("Drop Table"))
			{
				System.out.println("----------------------------------------");
				System.out.print("Choose a table to drop: ");
				String tableName = eliminateSpaces(in.nextLine().trim());
				
				File f = new File(tableName + ".tb");
				if (f.exists())
				{
					if (f.delete())
					{
						System.out.println("Table " + tableName + " has been dropped");
					}
				}
				else
				{
					System.out.println("Table does not exist!");
				}
				
				System.out.println("----------------------------------------");
			}
			else if (command.equalsIgnoreCase("Delete Record"))
			{
				System.out.println("----------------------------------------");
				System.out.print("Choose a table to delete a record from: ");
				
				String tableName = eliminateSpaces(in.nextLine().trim());
				
				File f = new File(tableName + ".tb");
				if (f.exists())
				{
					System.out.println("Table: " + tableName);
					System.out.print("Choose a record to delete: ");
					String record = in.nextLine().trim();
					
					while (!isNumeric(record, false) || record.equals("") || record.charAt(0) == '0')
					{
						System.out.println("Record must be an integer such as 1 for 'Record 1'");
						System.out.print("Choose a valid record: ");
						record = in.nextLine().trim();
					}
					
					Scanner input = new Scanner(f);
					ArrayList<String> list = new ArrayList<String>();

					while (input.hasNextLine()) 
					{
					    list.add(input.nextLine());
					}
					input.close();
					
					boolean flag = false;
					if (Integer.parseInt(record) > list.size() - 1)
					{
						flag = true;
					}
					
					if (flag)
					{
						System.out.println("Record not found!");
					}
					else
					{
						int pos = 0;
						for (int i = list.get(0).length() - 1; i >= 0; i--)
						{
							if (list.get(0).charAt(i - 1) == ':')
							{
								pos = i;
								break;
							}
						}
						
						String line1 = list.get(0).substring(0, pos) + (list.size() - 2) + "]";
						PrintWriter printWriter = new PrintWriter(new FileOutputStream(tableName + ".tb"), false);
						printWriter.println(line1);
						for (int i = 1; i <= list.size() - 1; i++)
						{
							if (Integer.parseInt(record) != i)
							{
								printWriter.println(list.get(i));
							}
						}
						
						printWriter.close();
					}
				}
				else
				{
					System.out.println("Table does not exist!");
				}
				System.out.println("----------------------------------------");
			}
			else if (command.equalsIgnoreCase("Query"))
			{
				System.out.println("----------------------------------------");
				System.out.println("----------------------------------------");
			}
			else if (command.equalsIgnoreCase("Display Commands"))
			{
				System.out.println("----------------------------------------");
				System.out.println("Command 1: Create Table");
				System.out.println("Command 2: Describe Table");
				System.out.println("Command 3: Insert Record");
				System.out.println("Command 4: Drop Table");
				System.out.println("Command 5: Delete Record");
				System.out.println("Command 6: Query");
				System.out.println("Command 7: Display Commands");
				System.out.println("Command 8: Exit");
				System.out.println("----------------------------------------");
			}
			else if (command.equalsIgnoreCase("Exit"))
			{
				System.out.println("----------------------------------------");
				System.out.println("Goodbye for now!");
				System.out.println("----------------------------------------");
				break;
			}
			else
			{
				errorCount++;
				
				if (errorCount == 3)
				{
					errorCount = 0;
					System.out.println("----------------------------------------");
					System.out.println("Invalid Command!");
					System.out.println("Consider using the following command: Display Commands");
					System.out.println("----------------------------------------");
				}
				else
				{
					System.out.println("----------------------------------------");
					System.out.println("Invalid Command!");
					System.out.println("----------------------------------------");
				}
			}
			
			System.out.println();
		}
		
		in.close();
	}
	
	private static String reduceSpaces(String str)
	{
		return str.replaceAll("\\s+", " ");
	}
	
	private static String eliminateSpaces(String str)
	{
		return str.replaceAll("\\s+", "_");
	}
	
	private static boolean isAlphaNumeric(String str)
	{
		return str != null && str.matches("^[a-zA-Z0-9_]*$");
	}
	
	private static boolean isNumeric(String str, boolean flag)
	{
		if (flag)
		{
			return str != null && str.matches("^[1-4]*$");
		}
		else
		{
			return str != null && str.matches("^[0-9]*$");
		}
	}
	
	private static void getAttributes(String header, Table table)
	{
		String attributeName = "";
		String attributeType = "";
		int j = 0;
		
		for (int i = 0; i <= header.length() - 1; i++)
		{
			if (header.charAt(i) == '[' && i != 0 && header.charAt(i + 1) != 'R')
			{
				j = i + 1;
				boolean flag = false;
				
				while (header.charAt(j) != ']')
				{	
					if (header.charAt(j) == ':')
					{
						flag = true;
						j++;
					}
					
					if (!flag)
					{
						attributeName = attributeName + header.charAt(j);
					}
					else
					{
						attributeType = attributeType + header.charAt(j);
					}
					j++;
				}
				
				table.addAttribute(new Attribute(attributeName, getAttributeType(attributeType)));
				
				attributeName = "";
				attributeType = "";
				i = j;
			}
		}
	}
	
	private static String getRecordCount(String header)
	{
		int pos = 0;
		for (int i = header.length() - 1; i >= 0; i--)
		{
			if (header.charAt(i - 1) == ':')
			{
				pos = i;
				break;
			}
		}
		
		String recordCount = "";
		for (int k = pos; k <= header.length() - 2; k++)
		{
			recordCount = recordCount + header.charAt(k);
		}
		
		return recordCount;
	}
	
	private static int getAttributeType(String type)
	{
		if (type == "Integer")
		{
			return 1;
		}
		else if (type == "Double")
		{
			return 2;
		}
		else if (type == "Boolean")
		{
			return 3;
		}
		else
		{
			return 4;
		}
	}
}
