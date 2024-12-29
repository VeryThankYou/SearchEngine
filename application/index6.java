package application;

import java.util.ArrayList;
import java.util.Arrays;
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
    private class WikiItem 
    {
        String str;
        ArrayList<Integer> docs;
        ArrayList<Integer> numInDocs;
        WikiItem next;
 
        WikiItem(String s, ArrayList<Integer> d, ArrayList<Integer> nd, WikiItem n) 
        {
            str = s;
            docs = d;
            numInDocs = nd;
            next = n;
        }
    }

    private class TupleIF 
    {
        int i;
        double d;
        TupleIF(int newi, double newd) 
        {
            i = newi;
            d = newd;
        }
        public String toString()
        {
            return "(" + Integer.toString(i) + "," + Double.toString(d) + ")";
        }
    }

    public Index6()
    {
        
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
            numDocs = 1;
            start = new WikiItem(word, null, null, null);
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
                        docLength.add(0);
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
        WikiItem word = hashTable[hash_int];
        boolean not_added_yet_word = true;
        while (word != null) 
        {
            if(word.str.equals(str))
            {
                //System.out.println(item.str);
                if(word.docs.contains(docNumber))
                {
                    int indexOfDoc = word.docs.indexOf(docNumber);
                    word.numInDocs.set(indexOfDoc, word.numInDocs.get(indexOfDoc) + 1);
                    not_added_yet_word = false;
                }
                else
                {
                    //System.out.println(item.str);
                    word.docs.add(docNumber);
                    word.numInDocs.add(1);
                    memoryuse += 1;
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
                memoryuse += 1;
                ArrayList<Integer> newNumInDocs = new ArrayList<Integer>();
                newNumInDocs.add(1);
                memoryuse += 1;
                WikiItem new_word = new WikiItem(str, newDocs, newNumInDocs, hashTable[hash_int]);
                memoryuse += 1;
                hashTable[hash_int] = new_word;
                memoryuse += 1;
            }
    }

    public double specificLog(double x, int base)
    {
        return Math.log(x) / ((double) Math.log(base));
    }
 
    public DocItem search(String searchstr) 
    {
        int hashint = hash(searchstr);
        WikiItem curr = hashTable[hashint];  
        ArrayList<Integer> docs = null;
        ArrayList<Integer> tfs = null;
        while (curr != null) 
        {
            if(curr.str.equals(searchstr))
            {
                docs = curr.docs;
                tfs = curr.numInDocs;
                break;
            }
            curr = curr.next;    
        }
        if(docs != null)
        {
            TupleIF[] tfidfs = new TupleIF[docs.size()];
            for(int i = 0; i < docs.size(); i++)
            {
                double tfidf = (double) tfs.get(i) * specificLog((double) (numDocs/docs.size()), 2);
                tfidfs[i] = new TupleIF(docs.get(i), tfidf);
            }
            TupleIF[] sortedTfidfs = mergeSort(tfidfs);
            DocItem current = null;
            for(int i = 0; i < sortedTfidfs.length; i++)
            {
                System.out.println(sortedTfidfs[i]);
                DocItem temp = new DocItem(docNames.get(sortedTfidfs[i].i), current);
                current = temp;
            }
            return current;
        }
        return null;
    }

    public TupleIF[] mergeSort(TupleIF[] list)
    {
        if(list.length < 2)
        {
            return list;
        }
        TupleIF[][] splits = split(list);
        TupleIF[] l1 = mergeSort(splits[0]);
        TupleIF[] l2 = mergeSort(splits[1]);
        splits[0] = l1;
        splits[1] = l2;
        TupleIF[] merged = merge(splits);
        return merged;
    }

    public TupleIF[][] split(TupleIF[] list)
    {
        if(list.length < 2)
        {
            TupleIF array[] = {};
            TupleIF[] array2[] = {list, array};
            return array2;
        }
        int L = list.length;
        int cut = (int) Math.ceil(((double) L)/2);
        TupleIF array1[] = Arrays.copyOfRange(list, 0, cut);
        TupleIF array2[] = Arrays.copyOfRange(list, cut, list.length);
        TupleIF[] result[] = {array1, array2};
        return result;
    }
    public TupleIF[] merge(TupleIF[][] lists)
    {
        TupleIF[] l1 = lists[0];
        TupleIF[] l2 = lists[1];
        TupleIF[] res = new TupleIF[l1.length + l2.length];
        int i1 = 0;
        int i2 = 0;
        while(i1 < l1.length && i2 < l2.length)
        {
            if(l1[i1].d <= l2[i2].d)
            {
                res[i1 + i2] = l1[i1];
                i1 += 1;
                continue;
            }
            res[i1 + i2] = l2[i2];
            i2 += 1;
        }
        while(i1 < l1.length)
        {
            res[i1 + i2] = l1[i1];
            i1 += 1;
        }
        while(i2 < l2.length)
        {
            res[i1 + i2] = l2[i2];
            i2 += 1;
        }
        return res;
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
                result = (result % p + p) % p;
            }
            return ((a2*result + b) % p + p) % p;
        }
 
    public String toString()
    {
        return "Index6";
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