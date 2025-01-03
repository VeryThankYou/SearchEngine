package application;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Scanner;
import java.util.concurrent.ThreadLocalRandom;

public class HashTest 
{
    WikiItem [] hashTable;
    int [] hashTableSize;
    int [] hashvals;
    int uniqueWords;
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

    public HashTest()
    {
        
    }
    
 
    public void Build(String filename, int[] hashvals) 
    {
        String word;
        this.hashvals = hashvals;
        hashTable = new WikiItem[hashvals[0]];
        hashTableSize = new int[hashvals[0]];
        uniqueWords = 0;
        try {
            Scanner input = new Scanner(new File(filename), "UTF-8");
            word = input.next();
            int current_doc = 0;
            while (input.hasNext()) 
            {   
                // Read all words in input
                word = input.next();
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
                return;
            }
            word = word.next;
        }
        WikiItem new_word = new WikiItem(str, null, null, hashTable[hash_int]);
        hashTable[hash_int] = new_word;
        hashTableSize[hash_int] += 1;
        uniqueWords += 1;
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

    public static void main(String[] args) 
    {
        int [] hashvals = new int[4];
        hashvals[0] = 98317;
        hashvals[1] = ThreadLocalRandom.current().nextInt(1, hashvals[0]);
        hashvals[2] = ThreadLocalRandom.current().nextInt(1, hashvals[0]);
        hashvals[3] = ThreadLocalRandom.current().nextInt(1, hashvals[0]);
        System.out.println("Preprocessing " + args[0]);
        HashTest i = new HashTest();
        i.Build(args[0], hashvals);
        try 
        {
            File myObj = new File("HashSize.txt");
            if (myObj.createNewFile()) 
            {
                System.out.println("File created: " + myObj.getName());
            } else 
            {
                System.out.println("File already exists.");
            }
        } catch (IOException e) 
        {
            System.out.println("An error occurred.");
        }
        try 
        {
            FileWriter myWriter = new FileWriter("HashSize" + args[0].charAt(args[0].length() - 5) + ".txt");
            myWriter.write(i.uniqueWords + "\n");
            for(int j = 0; j < hashvals[0]; j++)
            {
                myWriter.write(i.hashTableSize[j] + "|");
            }
            myWriter.close();
            System.out.println("Successfully wrote to the file.");
        } catch (IOException e) 
        {
            System.out.println("An error occurred.");
            e.printStackTrace();
        }
    }
}
