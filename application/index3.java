package application;

import java.io.*;
import java.util.Scanner;
 
class Index3 
{
    WikiItem start;
    private class WikiItem 
    {
        String str;
        DocItem docs;
        WikiItem next;
 
        WikiItem(String s, DocItem d, WikiItem n) 
        {
            str = s;
            docs = d;
            next = n;
        }
    }

    private class DocItem {
        String str;
        DocItem next;
 
        DocItem(String s, DocItem n) 
        {
            str = s;
            next = n;
        }
    }
 
    public Index3(String filename) 
    {
        String word;
        WikiItem current, tmp;
        try {
            Scanner input = new Scanner(new File(filename), "UTF-8");
            word = input.next();
            String curDoc = word;
            start = new WikiItem(word, new DocItem(curDoc, null), null);
            current = start;
            while (input.hasNext()) 
            {   // Read all words in input
                word = input.next();
                
                if (word.equals("---END.OF.DOCUMENT---") && input.hasNext())
                {
                    System.out.println(curDoc);
                    String docNameString = input.next();
                    while (docNameString.charAt(docNameString.length() - 1) !=   '.') 
                    {
                        // System.out.println(docNameString);
                        docNameString = docNameString + " " + input.next();
                    }
                    curDoc = docNameString;
                    String[] docWords = docNameString.split(" ");
                    for(int i = 0; i < docWords.length; i++)
                    {
                        insertDocItem(start, docWords[i], curDoc);
                    }
                }
                insertDocItem(start, word, curDoc);
            }
            input.close();
        } catch (FileNotFoundException e) 
        {
            System.out.println("Error reading file " + filename);
        }
    }

    public void insertDocItem(WikiItem head, String word, String docname)
    {
        WikiItem tmp = head;
        while (tmp != null) 
        {
            if(tmp.str.equals(word))
            {
                DocItem dtmp = tmp.docs;
                while (dtmp != null) 
                {
                    //System.out.println(dtmp.str);
                    if(dtmp.str.equals(docname))
                    {
                        return;
                    }
                    dtmp = dtmp.next;
                }
                dtmp = new DocItem(docname, tmp.docs);
                tmp.docs = dtmp;
                return;
            }
            tmp = tmp.next;
        }
        WikiItem newtmp = new WikiItem(word, new DocItem(docname, null), head);
        start = newtmp;
    }

 
    public DocItem search(String searchstr)
    {
        WikiItem current = start;
        while (current != null) 
        {
            if (current.str.equals(searchstr))
            {
                return current.docs;
            }
            current = current.next;
        }  
        return null;  
    }
 
    public static void main(String[] args) {
        System.out.println("Preprocessing " + args[0]);
        Index3 i = new Index3(args[0]);
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