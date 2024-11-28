package application;

import java.util.ArrayList;
import java.io.*;
import java.util.Scanner;
import java.util.concurrent.ThreadLocalRandom;

class Index6 implements OverIndex
{
    public static int memoryuse = 0;
    WikiItem start;
    int [] hashvals;
    ArrayList<String> docNames;
    ArrayList<Integer> docLength;
    int numDocs;
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

    public Index6()
    {
        
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
            memoryuse += 1;
            word = input.next();
            numDocs = 1;
            start = new WikiItem(word, null, null);
            memoryuse += 1;
            int current_doc = 0;
            docLength = new ArrayList<Integer>();
            docLength.add(1);
            docNames.add(word);
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
                        numDocs += 1;
                        String docNameString = input.next();
                        while (docNameString.charAt(docNameString.length() - 1) !=   '.' && input.hasNext()) 
                        {
                            docNameString = docNameString + " " + input.next();
                        }
                        docNames.add(docNameString);
                        current_doc += 1;
                        memoryuse += 1;
                        String[] words = docNameString.split(" ");
                        memoryuse += words.length;
                        for(int i = 0; i < words.length; i++)
                        {
                            docLength.set(current_doc, docLength.get(current_doc) + 1);
                            insertDocItem(words[i], current_doc);
                        }
                    }
                    continue;
                }
                docLength.set(current_doc, docLength.get(current_doc) + 1);
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
        memoryuse += 1;
        WikiItem word = hashTable[hash_int];
        memoryuse += 1;
        boolean not_added_yet_word = true;
        memoryuse += 1;
        memoryuse += 1;
        while (word != null) 
        {
            if(word.str.equals(str))
            {
                //System.out.println(item.str);
                if(word.docs.contains(docNumber))
                {
                    not_added_yet_word = false;
                }
                else
                {
                    //System.out.println(item.str);
                    word.docs.add(docNumber);
                }
                not_added_yet_word = false;
                break;
            }
            word = word.next;
        }
        if(not_added_yet_word)
            {
                //System.out.println(item.str);
                ArrayList<Integer> newDocs = new ArrayList<Integer>();
                newDocs.add(docNumber);
                WikiItem new_word = new WikiItem(str, newDocs, hashTable[hash_int]);
                memoryuse += 1;
                hashTable[hash_int] = new_word;
            }
    }

    public DocItem findDocuments(String searchstr)
    {
        WikiItem current = start;
        DocItem docName = new DocItem(current.str, null);
        DocItem docNames = null;
        while (current != null) {
            if (current.str.equals("---END.OF.DOCUMENT---") && current.next != null)
            {
                String docNameString = current.next.str;
                WikiItem docNameStart = current.next;
                WikiItem temp = docNameStart.next;
                while (docNameString.charAt(docNameString.length() - 1) !=   '.') 
                {
                    // System.out.println(docNameString);
                    docNameString = docNameString + " " + temp.str;
                    temp = temp.next;
                }
                docName = new DocItem(docNameString, null);
            }
            if ((docNames == null || !(docNames.str.equals(docName.str))) && current.str.equals(searchstr)) {
                DocItem temp = new DocItem(docName.str, docNames);
                docNames = temp;
            }
            current = current.next;
        }
        return docNames;
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
            ArrayList<Integer> listOfDocs = null;
            while (curr != null) 
            {
                if(curr.str.equals(searchstr))
                {
                    listOfDocs = curr.docs;
                }
                curr = curr.next;    
            }
            return listOfDocs;
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
        System.out.println(searchstr);
        System.out.println(operator_start);
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
            int a1 = hashvals[1];
            int a2 = hashvals[2];
            int b = hashvals[3];
            int result = 0;
            int l = x.length();
            for(int i = 0; i < l; i++)
            {
                char c = x.charAt(i);
                result += ((int) c) * Math.pow(a1, (l - 1 - i));
            }
            result = (result % p + p) % p;
            return ((a2*result + b) % p + p) % p;
        }
 
    public String toString()
    {
        return "Index 6";
    }

    public static void main(String[] args) 
    {
        int [] hashvals = new int[4];
        hashvals[0] = 98317;
        hashvals[1] = ThreadLocalRandom.current().nextInt(1, hashvals[0]);
        hashvals[2] = ThreadLocalRandom.current().nextInt(1, hashvals[0]);
        hashvals[3] = ThreadLocalRandom.current().nextInt(1, hashvals[0]);
        System.out.println("Preprocessing " + args[0]);
        Index6 i = new Index6();
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