package application;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Scanner;
import java.util.concurrent.ThreadLocalRandom;

public class HashTest2 
{
    int [] hashvals;
    

    public HashTest2(int[] integers)
    {
        hashvals = integers;
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
        int [] hashvals = new int[5];
        hashvals[0] = 100663319;
        hashvals[1] = 98317;
        hashvals[2] = ThreadLocalRandom.current().nextInt(1, hashvals[0]);
        hashvals[3] = ThreadLocalRandom.current().nextInt(1, hashvals[1]);
        hashvals[4] = ThreadLocalRandom.current().nextInt(0, hashvals[1]);
        HashTest2 i = new HashTest2(hashvals);
        int numTests = 10000;
        Long[] times = new Long[numTests];
        String testString = "";
        Long startTime;
        Long endTime;
        Long totalTime;
        for(int j = 0; j < numTests; j++)
        {
            testString = testString + "a";
            startTime = System.nanoTime();
            int o = i.hash(testString);
            endTime = System.nanoTime();
            totalTime = endTime - startTime;
            times[j] = totalTime;
        }
        try 
        {
            File myObj = new File("HashTime.txt");
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
            FileWriter myWriter = new FileWriter("HashTime.txt");
            for(int j = 0; j < numTests; j++)
            {
                myWriter.write(times[j] + "|");
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
