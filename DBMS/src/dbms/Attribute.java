package dbms;

/**
 * Constructs an Attribute object to be used within a Table
 */
public class Attribute 
{
	private String name;
	private int type;
	
	public Attribute(String name, int type)
	{
		this.name = name;
		this.type = type;
	}
	
	public String getName()
	{
		return name;
	}
	
	public int getType()
	{
		return type;
	}
	
	public String getType(int type)
	{
		if (type == 1)
		{
			return "Integer";
		}
		else if (type == 2)
		{
			return "Double";
		}
		else if (type == 3)
		{
			return "Boolean";
		}
		else
		{
			return "String";
		}
	}
}
