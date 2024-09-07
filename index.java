import java.io.*;
import java.util.Scanner;
import java.util.ArrayList;
 
class Index1 {
 
    WikiItem start;
    WikiItem docName;
 
    private class WikiItem {
        String str;
        WikiItem next;
 
        WikiItem(String s, WikiItem n) {
            str = s;
            next = n;
        }
    }
 
    public Index1(String filename) {
        String word;
        WikiItem current, tmp;
        try {
            Scanner input = new Scanner(new File(filename), "UTF-8");
            word = input.next();
            start = new WikiItem(word, null);
            current = start;
            while (input.hasNext()) {   // Read all words in input
                word = input.next();
                //System.out.println(word);
                tmp = new WikiItem(word, null);
                current.next = tmp;
                current = tmp;
            }
            input.close();
        } catch (FileNotFoundException e) {
            System.out.println("Error reading file " + filename);
        }
    }
 
    public ArrayList<WikiItem> search(String searchstr) {
        WikiItem current = start;
        docName = current;
        ArrayList<WikiItem> docNames = new ArrayList<WikiItem>();
        while (current != null) {
            if (current.str.equals("---END.OF.DOCUMENT---") && current.next != null)
            {
                docName = current.next;
            }
            if ((docNames.size() == 0 || !(docNames.get(docNames.size()-1).str.equals(docName.str))) && current.str.equals(searchstr)) {
                docNames.add(docName);
            }
            current = current.next;
        }
        return docNames;
    }
 
    public static void main(String[] args) {
        System.out.println("Preprocessing " + args[0]);
        Index1 i = new Index1(args[0]);
        Scanner console = new Scanner(System.in);
        for (;;) {
            System.out.println("Input search string or type exit to stop");
            String searchstr = console.nextLine();
            if (searchstr.equals("exit")) {
                break;
            }
            ArrayList<WikiItem> output = i.search(searchstr);
            if (output.size() > 0) {
                System.out.println(searchstr + " exists in the following documents:");
                for (int i2 = 0; i2 < output.size(); i2++)
                {
                    System.out.println(output.get(i2).str);
                }
            } else {
                System.out.println(searchstr + " does not exist");
            }
        }
        console.close();
    }
}