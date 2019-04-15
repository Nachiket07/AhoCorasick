import java.util.*;
import java.lang.*;
class Trie
{
	boolean match;
	HashMap<String,Trie> pair;
	Trie failure;
}
class Fail
{
	Trie current;
	
	Trie prefix;
	Fail(Trie x,Trie y)
	{
		current=x;
		prefix=y;
	}
}
class AhoCorasick
{
	static int count=1;
	static String current="";
	static HashMap<Trie,ArrayList<String>> output;
	public static void construct(Trie head,String pattern)
	{
		Trie temp=null;
		if(pattern.compareTo("")!=0)
		{
			if(!head.pair.containsKey(pattern.substring(0,1)))
			{
				temp=new Trie();
				temp.match=false;
				temp.pair=new HashMap<String,Trie>();
				head.pair.put(pattern.substring(0,1),temp);
				count+=1;
			}
			construct(head.pair.get(pattern.substring(0,1)),pattern.substring(1));
		}
		else
		{
			if(!output.containsKey(head))
			{
				output.put(head,new ArrayList<String>());
				
			}
			output.get(head).add(current);
			head.match=true;
		}
	}
	
	public static void main(String args[])
	{
		Trie head=new Trie();
		head.match=false;
		head.pair=new HashMap<String,Trie>();
		Scanner sc=new Scanner(System.in);
		System.out.println("Enter no of patterns to match with given text do not enter \"\" ");
		int n=-1;
		n=sc.nextInt();
		while(n==-1)
		{
			n=sc.nextInt();
		}
		String pattern="";
		int i,j;
		System.out.println("Enter patterns now");
		HashMap<String,Integer> pattern_count=new HashMap<String,Integer>();
		output=new HashMap<Trie,ArrayList<String>>();
		for(i=0;i<n;i++)
		{
			while(pattern.compareTo("")==0)
			{
				pattern=sc.nextLine();
			}
			pattern_count.put(pattern,0);
			current=pattern;
			construct(head,pattern);
			pattern="";
		}
		System.out.println("No of nodes created are "+count);
		count=0;
		ArrayList<Fail> queue =new ArrayList<Fail>();
		head.failure=head;
		Fail f=null;
		Trie temp_prefix=null;
		for(Map.Entry<String,Trie> m:head.pair.entrySet())
		{
			f=new Fail(m.getValue(),head);
			m.getValue().failure=head;
			queue.add(f);
		}
		while(queue.size()!=0)
		{
			f=queue.get(0);
			f.current.failure=f.prefix.failure;
			for(Map.Entry<String,Trie> m:f.current.pair.entrySet())
			{
				temp_prefix=f.prefix;
				while(temp_prefix!=head && !temp_prefix.pair.containsKey(m.getKey()))
				{
					temp_prefix=temp_prefix.failure;
				}
				if(temp_prefix.pair.containsKey(m.getKey()))
				{
					temp_prefix=temp_prefix.pair.get(m.getKey());
				}
				if(temp_prefix.match==true) // for the output
				{
					count+=1;
					if(!output.containsKey(m.getValue()))
					{
						output.put(m.getValue(),new ArrayList<String>());
					}
					output.get(m.getValue()).addAll(output.get(temp_prefix));
				}
				Fail Ftemp=new Fail(m.getValue(),temp_prefix); 
				queue.add(Ftemp);
			}
			queue.remove(0);
		}
		System.out.println("Enter the text now do not enter \"\"");
		String text="";
		while(text.compareTo("")==0)
		{
			text=sc.nextLine();
		}
		temp_prefix=head;
		for(i=0;i<text.length();i++)
		{
			while(temp_prefix!=head && !temp_prefix.pair.containsKey(text.substring(i,i+1)))
			{
				temp_prefix=temp_prefix.failure;
			}
			if(temp_prefix.pair.containsKey(text.substring(i,i+1)))
			{
				temp_prefix=temp_prefix.pair.get(text.substring(i,i+1));
			}
			if(temp_prefix.match)
			{
				for(j=0;j<output.get(temp_prefix).size();j++)
				{
					pattern_count.replace(output.get(temp_prefix).get(j),pattern_count.get(output.get(temp_prefix).get(j))+1);
				}
				//System.out.println("pattern "+output.get(temp_prefix)+" detected !");
			}
		}
		System.out.println("Patterns found in text with their frequencies are as follows");
		for(Map.Entry<String,Integer> m:pattern_count.entrySet())
		{
			System.out.println("Pattern "+m.getKey()+" occured "+m.getValue()+" times.");
		}
	}
}