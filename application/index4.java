package application;


import java.io.*;
import java.util.Scanner;
import java.util.concurrent.ThreadLocalRandom;

class Index4 implements OverIndex
{
    public static int memoryuse = 0;
    WikiItem start;
    int [] hashvals;
    WikiItem [] hashTable;
    private class WikiItem {
        String str;
        DocItem docs;
        WikiItem next;
 
        WikiItem(String s, DocItem d, WikiItem n) 
        {
            str = s;
            docs = d;
            next = n;
        }
    }

    public Index4()
    {
        
    }
    
    public int memoryuse()
    {
        return memoryuse;
    }

    public void Build(String filename, int[] hashvals) 
    {
        String word;
        this.hashvals = hashvals;
        memoryuse += 4;
        hashTable = new WikiItem[hashvals[0]];
        memoryuse += hashvals[0];
        try {
            Scanner input = new Scanner(new File(filename), "UTF-8");
            memoryuse += 1;
            word = input.next();
            start = new WikiItem(word, null, null);
            memoryuse += 1;
            DocItem current_doc = new DocItem(word, null);
            memoryuse += 1;
            while (input.hasNext()) 
            {   
                // Read all words in input
                word = input.next();
                //System.out.println(word);
                memoryuse += 1;
                if (word.equals("---END.OF.DOCUMENT---"))
                {
                    if(input.hasNext())
                    {
                        String docNameString = input.next();
                        while (docNameString.charAt(docNameString.length() - 1) !=   '.' && input.hasNext()) 
                        {
                            docNameString = docNameString + " " + input.next();
                        }
                        current_doc = new DocItem(docNameString, null);
                        memoryuse += 1;
                        String[] words = docNameString.split(" ");
                        for(int i = 0; i < words.length; i++)
                        {
                            insertDocItem(words[i], current_doc);
                        }
                    }
                    continue;
                }
                insertDocItem(word, current_doc);
            }
            input.close();
        } catch (FileNotFoundException e) 
        {
            System.out.println("Error reading file " + filename);
        }
    }

    public void insertDocItem(String str, DocItem item)
    {
        int hash_int = hash(str);
        WikiItem word = hashTable[hash_int];
        DocItem item_copy = new DocItem(item.str, null);
        memoryuse += 1;
        while (word != null) 
        {
            if(word.str.equals(str))
            {
                //System.out.println(item.str);
                DocItem cur = word.docs;
                memoryuse += 1;

                if(cur.equals(item))
                {
                    return;
                }
                item_copy.next = word.docs;
                memoryuse += 1;
                word.docs = item_copy;
                return;
            }
            word = word.next;
        }
        WikiItem new_word = new WikiItem(str, item_copy, hashTable[hash_int]);
        memoryuse += 1;
        hashTable[hash_int] = new_word;
        memoryuse += 1;   
    }
 
    public DocItem search(String searchstr) 
    {
        int hashint = hash(searchstr);
        WikiItem curr = hashTable[hashint];  
        while (curr != null) 
        {
            if(curr.str.equals(searchstr)){return curr.docs;}
            curr = curr.next;    
        }
        return null;
    }

    public int hash(String x)
        {
            int p = hashvals[0];
            int a1 = hashvals[1];
            int a2 = hashvals[2];
            int b = hashvals[3];
            int result = 0;
            int l = x.length();
            for(int i = 0; i < l; i++)
            {
                char c = x.charAt(i);
                result += ((int) c) * Math.pow(a1, (l - 1 - i));
                result = (result % p + p) % p;
            }
            return ((a2*result + b) % p + p) % p;
        }
 
    public String toString()
    {
        return "Index4";
    }

    public static void main(String[] args) 
    {    
        int [] hashvals = new int[4];
        hashvals[0] = 98317;
        hashvals[1] = ThreadLocalRandom.current().nextInt(1, hashvals[0]);
        hashvals[2] = ThreadLocalRandom.current().nextInt(1, hashvals[0]);
        hashvals[3] = ThreadLocalRandom.current().nextInt(1, hashvals[0]);
        System.out.println("Preprocessing " + args[0]);
        Index4 i = new Index4();
        i.Build(args[0], hashvals);
        Scanner console = new Scanner(System.in);
        for (;;) {
            System.out.println("Input search string or type exit to stop");
            String searchstr = console.nextLine();
            if (searchstr.equals("exit")) 
            {
                break;
            }
            DocItem output = i.search(searchstr);
            if (output == null) 
            {
                System.out.println(searchstr + " does not exist");
            } else 
            {
                System.out.println(searchstr + " exists in the following documents:");
                while (output != null)
                {
                    System.out.println(output.str);
                    output = output.next;
                }
            } 
        }
        console.close();
    }
}