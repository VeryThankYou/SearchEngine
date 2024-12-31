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
    public static float[] testImplementation(String filename, OverIndex i)
    {
        float[] times = new float[11];
        int [] hashvals = new int[4];
        hashvals[0] = 98317;
        hashvals[1] = ThreadLocalRandom.current().nextInt(1, hashvals[0]);
        hashvals[2] = ThreadLocalRandom.current().nextInt(1, hashvals[0]);
        hashvals[3] = ThreadLocalRandom.current().nextInt(1, hashvals[0]);
        long startTime = System.nanoTime();
        i.Build(filename, hashvals);
        long endTime = System.nanoTime();
        long duration = (endTime - startTime);
        float durSeconds = ((float) duration)/1000000000;
        times[0] = durSeconds;
        String[] words = {"and", "Claus", "anarchy", "president", "machine", "basic", "probability", "q|z|x", "http://www.google.co.uk/imgres?imgurl=http://www.lesmills.co.nz/pt_photos/Chris%2520E1.jpg&imgrefurl=http://www.lesmills.co.nz/lesmills_custom.cfm%3F%26do%3Daction%26action%3Dchoose%2520PT%26clubid%3D1%2520OR%2520clubid%2520%3D%25202%2520OR%2520clubid%3D%25203%2520OR%2520clubid%2520%3D%25204%2520OR%2520clubid%2520%3D%25205%2520OR%2520clubid%2520%3D%25206%2520OR%2520clubid%2520%3D%25207%2520OR%2520clubid%2520%3D%25208%2520OR%2520clubid%2520%3D%25209%2520OR%2520clubid%2520%3D%252010&h=448&w=336&sz=14&tbnid=RHKePzUzxuEonM:&tbnh=127&tbnw=95&prev=/images%3Fq%3Dchris%2Beasley&hl=en&usg=__s-B6oN-fhpvZjOVG0WRiB7ancUk=&ei=tapXS5rzD4X80wSd4fz8BA&sa=X&oi=image_result&resnum=4&ct=image&ved=0CBAQ9QEwAw", "is"};
        for(int i2 = 0; i2 < words.length; i2++)
        {
            startTime = System.nanoTime();
            DocItem result = i.search(words[i2]);
            endTime = System.nanoTime();
            duration = (endTime - startTime);
            durSeconds = ((float) duration)/1000000000;
            times[i2+1] = durSeconds;
        }
        return times;
    }

    public static void writeTests(OverIndex index)
    {
        int n = 12;
        String tests;
        float bdurations;
        float[] sdurations = new float[10];
        int mem;
        String[] words = {"and", "Claus", "anarchy", "president", "machine", "basic", "probability", "q|z|x", "http://www.google.co.uk/imgres?imgurl=http://www.lesmills.co.nz/pt_photos/Chris%2520E1.jpg&imgrefurl=http://www.lesmills.co.nz/lesmills_custom.cfm%3F%26do%3Daction%26action%3Dchoose%2520PT%26clubid%3D1%2520OR%2520clubid%2520%3D%25202%2520OR%2520clubid%3D%25203%2520OR%2520clubid%2520%3D%25204%2520OR%2520clubid%2520%3D%25205%2520OR%2520clubid%2520%3D%25206%2520OR%2520clubid%2520%3D%25207%2520OR%2520clubid%2520%3D%25208%2520OR%2520clubid%2520%3D%25209%2520OR%2520clubid%2520%3D%252010&h=448&w=336&sz=14&tbnid=RHKePzUzxuEonM:&tbnh=127&tbnw=95&prev=/images%3Fq%3Dchris%2Beasley&hl=en&usg=__s-B6oN-fhpvZjOVG0WRiB7ancUk=&ei=tapXS5rzD4X80wSd4fz8BA&sa=X&oi=image_result&resnum=4&ct=image&ved=0CBAQ9QEwAw", "is"};
        
        for(int i = 0; i < n; i++)
        {
            String name = "application/documents/Wiki" + Integer.toString(i + 1) + ".txt";
            tests = name;
            float[] times = testImplementation(name, index);
            bdurations = times[0];
            for(int h = 1; h < 11; h++)
            {
                sdurations[h-1] = times[h];
            }
            
            mem = index.memoryuse();
            index.resetMemoryuse();
            try 
            {
                File myObj = new File("Testing/TimeTesting" + index.toString() + "f" + Integer.toString(i + 1) + ".txt");
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
                FileWriter myWriter = new FileWriter("Testing/TimeTesting" + index.toString() + "f" + Integer.toString(i + 1) + ".txt");
                myWriter.write("Buildtime:\n");
                myWriter.write(bdurations + "\n");
                myWriter.write("Memory use: \n");
                myWriter.write(mem + "\n");
                myWriter.write("Searchtime " + index.toString() + ":\n");
                for(int j=0; j < 10; j++)
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
        OverIndex i = new Index4();
        writeTests(i);
    }
}
