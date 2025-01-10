package application;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Arrays;
import java.util.Scanner;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.IntStream;

public class Testing 
{
    public static float[] testImplementation(String filename, OverIndex i, String[] queries)
    {
        int [] hashvals = new int[5];
        hashvals[0] = 100663319;
        hashvals[1] = 98317;
        hashvals[2] = ThreadLocalRandom.current().nextInt(1, hashvals[0]);
        hashvals[3] = ThreadLocalRandom.current().nextInt(1, hashvals[1]);
        hashvals[4] = ThreadLocalRandom.current().nextInt(0, hashvals[1]);
        long startTime = System.nanoTime();
        i.Build(filename, hashvals);
        long endTime = System.nanoTime();
        long duration = (endTime - startTime);
        float durSeconds = ((float) duration)/1000000000;
    
        
        float[] times = new float[queries.length + 1];
        times[0] = durSeconds;
        
        for(int i2 = 0; i2 < queries.length; i2++)
        {
            startTime = System.nanoTime();
            DocItem result = i.search(queries[i2]);
            endTime = System.nanoTime();
            duration = (endTime - startTime);
            durSeconds = ((float) duration)/1000000000;
            times[i2+1] = durSeconds;
        } 
        
        return times;
    }

    public static void writeTests(OverIndex index)
    {
        int n = 11;
        String tests;
        float bdurations;
        
        int mem;
        String[] words = {"long&&(and||(or&&(if||(is&&(for||(can&&(the||(to&&(this||(that&&(an||(a&&(was||(would&&want))))))))))))))", "and", "Claus", "anarchy", "president", "machine", "basic", "probability", "qzx", "is", "president&&state", "Apollo&&NASA", "astronaut||Apollo"};
        String[] words2 = new String[15 + words.length];
        String wtemp = words[0];
        int iteration = 0;
        while(wtemp.contains("("))
        {
            words2[iteration] = wtemp;
            int cut = Math.min(wtemp.indexOf("&&"), wtemp.indexOf("||"));
            wtemp = wtemp.substring(cut+3, wtemp.length()-1);
            iteration += 1;
        }
        words2[iteration] = wtemp;
        words2[iteration + 1] = "want";
        for(int i2 = 1; i2 < words.length; i2++)
        {
            words2[iteration + i2+1] = words[i2];
        }
        words = words2;
        float[] sdurations = new float[words.length];
        for(int i = 10; i < n; i++)
        {
            String name = "application/documents/Wiki" + Integer.toString(i + 1) + ".txt";
            tests = name;
            float[] times = testImplementation(name, index, words);
            bdurations = times[0];
            for(int h = 1; h < words.length; h++)
            {
                sdurations[h-1] = times[h];
            }
            
            mem = index.memoryuse();
            index.resetMemoryuse();
            try 
            {
                File myObj = new File("Testing/TimeTesting" + index.toString() + "f" + Integer.toString(i + 1) + "x.txt");
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
                FileWriter myWriter = new FileWriter("Testing/TimeTesting" + index.toString() + "f" + Integer.toString(i + 1) + "x.txt");
                myWriter.write("Buildtime:\n");
                myWriter.write(bdurations + "\n");
                myWriter.write("Memory use: \n");
                myWriter.write(mem + "\n");
                myWriter.write("Searchtime " + index.toString() + ":\n");
                for(int j=0; j < words.length; j++)
                {
                    myWriter.write(words[j] + ":" + sdurations[j] + "\n");
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

    public static void main(String[] args)
    {
        OverIndex i = new Index5();
        writeTests(i);
    }
}
