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
        float[] times = new float[5];
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

        startTime = System.nanoTime();
        DocItem result = i.search("and");
        endTime = System.nanoTime();
        duration = (endTime - startTime);
        durSeconds = ((float) duration)/1000000000;
        times[1] = durSeconds;

        startTime = System.nanoTime();
        result = i.search("q|z|x");
        endTime = System.nanoTime();
        duration = (endTime - startTime);
        durSeconds = ((float) duration)/1000000000;
        times[2] = durSeconds;

        startTime = System.nanoTime();
        result = i.search("http://www.google.co.uk/imgres?imgurl=http://www.lesmills.co.nz/pt_photos/Chris%2520E1.jpg&imgrefurl=http://www.lesmills.co.nz/lesmills_custom.cfm%3F%26do%3Daction%26action%3Dchoose%2520PT%26clubid%3D1%2520OR%2520clubid%2520%3D%25202%2520OR%2520clubid%3D%25203%2520OR%2520clubid%2520%3D%25204%2520OR%2520clubid%2520%3D%25205%2520OR%2520clubid%2520%3D%25206%2520OR%2520clubid%2520%3D%25207%2520OR%2520clubid%2520%3D%25208%2520OR%2520clubid%2520%3D%25209%2520OR%2520clubid%2520%3D%252010&h=448&w=336&sz=14&tbnid=RHKePzUzxuEonM:&tbnh=127&tbnw=95&prev=/images%3Fq%3Dchris%2Beasley&hl=en&usg=__s-B6oN-fhpvZjOVG0WRiB7ancUk=&ei=tapXS5rzD4X80wSd4fz8BA&sa=X&oi=image_result&resnum=4&ct=image&ved=0CBAQ9QEwAw");
        endTime = System.nanoTime();
        duration = (endTime - startTime);
        durSeconds = ((float) duration)/1000000000;
        times[3] = durSeconds;

        startTime = System.nanoTime();
        result = i.search("is");
        endTime = System.nanoTime();
        duration = (endTime - startTime);
        durSeconds = ((float) duration)/1000000000;
        times[4] = durSeconds;

        return times;
    }

    public static void writeTests(OverIndex index)
    {
        int n = 2;
        String[] tests = new String[n];
        float[] bdurations = new float[n];
        float[] sdurations = new float[4*n];
        int[] mem = new int[n];
        for(int i = 0; i < n; i++)
        {
            String name = "application/documents/Wiki" + Integer.toString(i + 1) + ".txt";
            tests[i] = name;
            float[] times = testImplementation(name, index);
            bdurations[i] = times[0];
            sdurations[(4*i)] = times[1];
            sdurations[(4*i)+1] = times[2];
            sdurations[(4*i)+2] = times[3];
            sdurations[(4*i)+3] = times[4];
            mem[i] = Index4.memoryuse;
            Index4.memoryuse = 0;
        }
        
        try 
        {
            File myObj = new File("TimeTesting.txt");
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
            FileWriter myWriter = new FileWriter("TimeTesting.txt");
            myWriter.write("Buildtime " + index.toString() + ":\n");
            myWriter.write(String.join("|", tests) + "\n");
            String temp = Arrays.toString(bdurations).replaceAll(",", "|");
            temp = temp.replaceAll(" ", "");
            myWriter.write(temp + "\n");
            myWriter.write("Memory use: \n");
            temp = Arrays.toString(mem).replaceAll(",", "|");
            temp = temp.replaceAll(" ", "");
            myWriter.write(temp + "\n");
            myWriter.write("Searchtime " + index.toString() + ":\n");
            String[] words = {"and", "q|z|x", "http://www.google.co.uk/imgres?imgurl=http://www.lesmills.co.nz/pt_photos/Chris%2520E1.jpg&imgrefurl=http://www.lesmills.co.nz/lesmills_custom.cfm%3F%26do%3Daction%26action%3Dchoose%2520PT%26clubid%3D1%2520OR%2520clubid%2520%3D%25202%2520OR%2520clubid%3D%25203%2520OR%2520clubid%2520%3D%25204%2520OR%2520clubid%2520%3D%25205%2520OR%2520clubid%2520%3D%25206%2520OR%2520clubid%2520%3D%25207%2520OR%2520clubid%2520%3D%25208%2520OR%2520clubid%2520%3D%25209%2520OR%2520clubid%2520%3D%252010&h=448&w=336&sz=14&tbnid=RHKePzUzxuEonM:&tbnh=127&tbnw=95&prev=/images%3Fq%3Dchris%2Beasley&hl=en&usg=__s-B6oN-fhpvZjOVG0WRiB7ancUk=&ei=tapXS5rzD4X80wSd4fz8BA&sa=X&oi=image_result&resnum=4&ct=image&ved=0CBAQ9QEwAw", "is"};
            for(int i=0; i < 4; i++)
            {
                myWriter.write(words[i] + "\n");
                float[] temptime = new float[n];
                for(int i2 = 0; i2 < n; i2++)
                {
                    temptime[i2] = sdurations[i + 4*i2];
                }
                temp = Arrays.toString(temptime).replaceAll(",", "|");
                temp = temp.replaceAll(" ", "");
                myWriter.write(temp + "\n");
            }
            
            myWriter.close();
            System.out.println("Successfully wrote to the file.");
        } catch (IOException e) 
        {
            System.out.println("An error occurred.");
            e.printStackTrace();
        }
    }

    public static void main(String[] args)
    {
        Index4 i = new Index4();
        writeTests(i);
    }
}
