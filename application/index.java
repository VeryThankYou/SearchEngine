package application;

import java.io.*;
import java.util.Scanner;
 
class Index1 {
 
    WikiItem start;
 
    private class WikiItem {
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
 
    public Index1(String filename) 
    {
        String word;
        WikiItem current, tmp;
        try {
            Scanner input = new Scanner(new File(filename), "UTF-8");
            word = input.next();
            start = new WikiItem(word, null, null);
            current = start;
            while (input.hasNext()) 
            {   // Read all words in input
                word = input.next();
                //System.out.println(word);
                tmp = new WikiItem(word, null, null);
                current.next = tmp;
                current = tmp;
            }
            input.close();
            current = start;
            WikiItem removedDuplicatesStart = new WikiItem(current.str, null, null);
            WikiItem rdtemp = removedDuplicatesStart;
            DocItem docs = findDocuments(rdtemp.str);
            rdtemp.docs = docs;
            while (current != null)
            {
                if (stringExists(current.str, removedDuplicatesStart)){
                    //System.out.println(current.str);
                } else
                {
                    rdtemp.next = new WikiItem(current.str, null, null);
                    rdtemp = rdtemp.next;
                    docs = findDocuments(rdtemp.str);
                    rdtemp.docs = docs;
                }
                current = current.next;
            }
            start = removedDuplicatesStart;
        } catch (FileNotFoundException e) 
        {
            System.out.println("Error reading file " + filename);
        }
    }

    public boolean stringExists(String str, WikiItem head)
    {
        WikiItem temp = head;
        while (temp != null) 
        {
            if (temp.str.equals(str))
            {
                return true;
            }    
            temp = temp.next;
        }
        return false;
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
        Index1 i = new Index1(args[0]);
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