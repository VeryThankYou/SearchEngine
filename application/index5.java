package application;

import java.util.ArrayList;
import java.io.*;
import java.util.Scanner;
import java.util.concurrent.ThreadLocalRandom;

class Index5 implements OverIndex
{
    public static int memoryuse = 0;
    WikiItem start;
    int [] hashvals;
    ArrayList<String> docNames;
    WikiItem [] hashTable;
    private class WikiItem {
        String str;
        ArrayList<Integer> docs;
        WikiItem next;
 
        WikiItem(String s, ArrayList<Integer> d, WikiItem n) 
        {
            str = s;
            docs = d;
            next = n;
        }
    }

    public Index5()
    {
        
    }

    public void resetMemoryuse()
    {
        memoryuse = 0;
    }
    
    public int memoryuse()
    {
        return memoryuse;
    }
 
    public void Build(String filename, int[] hashvals) 
    {
        String word;
        docNames = new ArrayList<>();
        this.hashvals = hashvals;
        memoryuse += 4;
        hashTable = new WikiItem[hashvals[0]];
        memoryuse += hashvals[0];
        try {
            Scanner input = new Scanner(new File(filename), "UTF-8");
            word = input.next();
            start = new WikiItem(word, null, null);
            memoryuse += 1;
            int current_doc = 0;
            docNames.add(word);
            memoryuse += 1;
            while (input.hasNext()) 
            {   
                // Read all words in input
                word = input.next();
                //System.out.println(word);
                if (word.equals("---END.OF.DOCUMENT---"))
                {
                    if(input.hasNext())
                    {
                        String docNameString = input.next();
                        while (docNameString.charAt(docNameString.length() - 1) !=   '.' && input.hasNext()) 
                        {
                            docNameString = docNameString + " " + input.next();
                        }
                        docNames.add(docNameString);
                        current_doc += 1;
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

    public void insertDocItem(String str, int docNumber)
    {
        int hash_int = hash(str);
        WikiItem word = hashTable[hash_int];
        while (word != null) 
        {
            if(word.str.equals(str))
            {
                //System.out.println(item.str);
                if(word.docs.get(word.docs.size() - 1) == docNumber)
                {
                    return;
                }
                //System.out.println(item.str);
                word.docs.add(docNumber);
                memoryuse += 1;
                return;
            }
            word = word.next;
        }
        //System.out.println(item.str);
        ArrayList<Integer> newDocs = new ArrayList<Integer>();
        newDocs.add(docNumber);
        memoryuse += 1;
        WikiItem new_word = new WikiItem(str, newDocs, hashTable[hash_int]);
        memoryuse += 1;
        hashTable[hash_int] = new_word;
        memoryuse += 1;
    }

    
 
    public DocItem search(String searchstr) 
    {
        ArrayList<Integer> listOfDocs = and_or_search(searchstr);
        DocItem currDocItem = null;
        for(int i = 0; listOfDocs != null && i < listOfDocs.size(); i++)
        {
            DocItem temp = new DocItem(docNames.get(listOfDocs.get(i)), currDocItem);
            currDocItem = temp;
        }
        return currDocItem;
    }

    public ArrayList<Integer> and_or_search(String searchstr) 
    {
        int operator_start = 0;
        int andm1_or1_none0 = 0;
        boolean start_with_parenthesis = false;
        if(searchstr.charAt(0) == '(')
        {
            start_with_parenthesis = true;
            int par_counter = 1;
            for(int i = 1; i < searchstr.length(); i++)
            {
                if(searchstr.charAt(i) == '(')
                {
                    par_counter += 1;
                }
                else if(searchstr.charAt(i) == ')')
                {
                    par_counter -= 1;
                }
                if(par_counter == 0)
                {
                    operator_start = i+1;
                    break;
                }
            }
        } else
        {
            for(int i = 0; i < searchstr.length(); i++)
            {
                if(searchstr.charAt(i) == '&' || searchstr.charAt(i) == '|')
                {
                    if(i + 1 < searchstr.length() && searchstr.charAt(i+1) == searchstr.charAt(i+1))
                    {
                        operator_start = i;
                        break;
                    }
                }
            }
        }
        
        if(searchstr.charAt(operator_start) == '&')
        {
            andm1_or1_none0 = -1;
        }
        else if(searchstr.charAt(operator_start) == '|')
        {
            andm1_or1_none0 = 1;
        } 
        if(andm1_or1_none0 == 0)
        {
            int hashint = hash(searchstr);
            WikiItem curr = hashTable[hashint];  
            while (curr != null) 
            {
                if(curr.str.equals(searchstr))
                {
                    return curr.docs;
                }
                curr = curr.next;    
            }
            return new ArrayList<Integer>();
        }
        if(searchstr.charAt(operator_start + 2) == '(')
        {
            searchstr = searchstr.substring(0, operator_start+2) + searchstr.substring(operator_start + 3, searchstr.length()-1);
        }
        if(start_with_parenthesis)
        {
            searchstr = searchstr.substring(1, operator_start - 1) + searchstr.substring(operator_start);
            operator_start -= 2;
        }
        ArrayList<Integer> searchresult1 = and_or_search(searchstr.substring(0, operator_start));
        ArrayList<Integer> searchresult2 = and_or_search(searchstr.substring(operator_start + 2));
        if(andm1_or1_none0 == -1)
        {
            return and_search(searchresult1, searchresult2);
        }
        return or_search(searchresult1, searchresult2);
    }

    public ArrayList<Integer> and_search(ArrayList<Integer> res1, ArrayList<Integer> res2)
    {
        ArrayList<Integer> res = new ArrayList<Integer>();
        int i1 = 0;
        int i2 = 0;
        while(i1 < res1.size() && i2 < res2.size())
        {
            int e1 = res1.get(i1);
            int e2 = res2.get(i2);
            if(e1 == e2)
            {
                res.add(e1);
                i1 += 1;
                i2 += 1;
            }
            else if(e1 > e2)
            {
                i2 += 1;
            }
            else if(e1 < e2)
            {
                i1 += 1;
            }
        }
        return res;
    }

    public ArrayList<Integer> or_search(ArrayList<Integer> res1, ArrayList<Integer> res2)
    {
        ArrayList<Integer> res = new ArrayList<Integer>();
        int i1 = 0;
        int i2 = 0;
        while(i1 < res1.size() && i2 < res2.size())
        {
            int e1 = res1.get(i1);
            int e2 = res2.get(i2);
            if(e1 == e2)
            {
                res.add(e1);
                i1 += 1;
                i2 += 1;
            }
            else if(e1 > e2)
            {
                res.add(e2);
                i2 += 1;
            }
            else if(e1 < e2)
            {
                res.add(e1);
                i1 += 1;
            }
        }
        if(i1 < res1.size())
        {
            for(int i = i1; i < res1.size(); i++)
            {
                res.add(res1.get(i));
            }
        }
        if(i2 < res2.size())
        {
            for(int i = i2; i < res2.size(); i++)
            {
                res.add(res2.get(i));
            }
        }
        return res;
    }

    public int hash(String x)
        {
            int p = hashvals[0];
            int q = hashvals[1];
            int z = hashvals[2];
            int a = hashvals[3];
            int b = hashvals[4];
            int result = 0;
            int l = x.length();
            for(int i = 0; i < l; i++)
            {
                char c = x.charAt(i);
                result += ((int) c) * Math.pow(z, (l - 1 - i));
                result = (result % p + p) % p;
            }
            return ((a*result + b) % q + q) % q;
        }
 
    public String toString()
    {
        return "Index5";
    }

    public static void main(String[] args) 
    {    
        int [] hashvals = new int[5];
        hashvals[0] = 100663319;
        hashvals[1] = 98317;
        hashvals[2] = ThreadLocalRandom.current().nextInt(1, hashvals[0]);
        hashvals[3] = ThreadLocalRandom.current().nextInt(1, hashvals[1]);
        hashvals[4] = ThreadLocalRandom.current().nextInt(0, hashvals[1]);
        System.out.println("Preprocessing " + args[0]);
        Index5 i = new Index5();
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