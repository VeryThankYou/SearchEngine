package application;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;
import java.util.concurrent.ThreadLocalRandom;

public class Testing 
{
    public static float testImplementation(String filename, OverIndex i)
    {
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
        
        return durSeconds;
    }

    public static void writeTests(OverIndex i)
    {
        float time = testImplementation("application/documents/Wiki2.txt", i);
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
            myWriter.write("Buildtime " + i.toString() + ": " + Float.toString(time));
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
