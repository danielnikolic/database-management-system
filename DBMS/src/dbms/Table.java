package dbms;

import java.util.ArrayList;

/**
 * Constructs a Table that holds records
 */
public class Table 
{
	private ArrayList<Attribute> attributeList;
	private String name;
	private int recordCount;
	
	public Table(String name)
	{
		this.name = name;
		attributeList = new ArrayList<Attribute>();
		recordCount = 0;
	}
	
	public String getName()
	{
		return name;
	}
	
	public ArrayList<Attribute> getAttributeList()
	{
		return attributeList;
	}
	
	public Attribute getAttribute(int i)
	{
		return attributeList.get(i);
	}
	
	public void addAttribute(Attribute a)
	{
		attributeList.add(a);
	}
	
	public int getRecordCount()
	{
		return recordCount;
	}
}
