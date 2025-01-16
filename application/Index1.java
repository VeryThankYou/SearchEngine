package application;

import java.io.*;
import java.util.Scanner;
 
class Index1 implements OverIndex
{ 
    WikiItem start;
    WikiItem docName;
    int memoryuse;

    private class WikiItem {
        String str;
        WikiItem next;
 
        WikiItem(String s, WikiItem n) {
            str = s;
            next = n;
        }
    }

    public void resetMemoryuse()
    {
        memoryuse = 0;
    }
    
    public int memoryuse()
    {
        return memoryuse;
    }

    public String toString()
    {
        return "Index1";
    }
    
    public Index1()
    {

    }

    public void Build(String filename, int[] hashvals) {
        String word;
        WikiItem current, tmp;
        memoryuse = 0;
        try {
            Scanner input = new Scanner(new File(filename), "UTF-8");
            word = input.next();
            start = new WikiItem(word, null);
            memoryuse += 1;
            current = start;
            while (input.hasNext()) {   // Read all words in input
                word = input.next();
                //System.out.println(word);
                tmp = new WikiItem(word, null);
                memoryuse += 1;
                current.next = tmp;
                memoryuse += 1;
                current = tmp;
            }
            input.close();
        } catch (FileNotFoundException e) {
            System.out.println("Error reading file " + filename);
        }
    }
 
    public DocItem search(String searchstr) {
        WikiItem current = start;
        docName = current;
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
                docName = new WikiItem(docNameString, null);
            }
            if ((docNames == null || !(docNames.str.equals(docName.str))) && current.str.equals(searchstr)) {
                DocItem temp = new DocItem(docName.str, docNames);
                docNames = temp;
            }
            current = current.next;
        }
        return docNames;
    }
 
    public static void main(String[] args) {
        System.out.println("Preprocessing " + args[0]);
        Index1 i = new Index1();
        i.Build(args[0], null);
        Scanner console = new Scanner(System.in);
        for (;;) {
            System.out.println("Input search string or type exit to stop");
            String searchstr = console.nextLine();
            if (searchstr.equals("exit")) {
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