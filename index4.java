import java.io.*;
import java.util.Scanner;
import java.util.concurrent.ThreadLocalRandom;

class Index4 {
 
    WikiItem start;
    int [] hashvals;
    WikiItem [] hashTable;
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

        public boolean equals(DocItem d2)
        {
            if(str.equals(d2.str)){return true;}
            return false;
        }
    }
 
    public Index4(String filename, int[] hashvals) 
    {
        String word;
        WikiItem current, tmp;
        this.hashvals = hashvals;
        hashTable = new WikiItem[hashvals[0]];
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
            insertDocItem(rdtemp.str, docs);
            rdtemp.docs = docs;
            while (current != null)
            {
                rdtemp.next = new WikiItem(current.str, null, null);
                rdtemp = rdtemp.next;
                docs = findDocuments(rdtemp.str);
                rdtemp.docs = docs;
                insertDocItem(rdtemp.str, docs);
                current = current.next;
            }
            start = removedDuplicatesStart;
        } catch (FileNotFoundException e) 
        {
            System.out.println("Error reading file " + filename);
        }
    }

    public void insertDocItem(String str, DocItem item)
    {
        int hash_int = hash(str);
        WikiItem word = hashTable[hash_int];
        boolean not_added_yet_word = true;
        while (word != null) 
        {
            if(word.str == str)
            {
                DocItem cur = word.docs;
                boolean not_added_yet = true;
                while(cur != null)
                {
                    if(cur.equals(item))
                    {
                        not_added_yet = true;
                        break;
                    }
                    cur = cur.next;
                }
                if(not_added_yet)
                {
                    item.next = word.docs;
                    word.docs = item;
                }
                not_added_yet_word = false;
                break;
            }
            word = word.next;
        }
        if(not_added_yet_word)
            {
                WikiItem new_word = new WikiItem(str, item, null);
                hashTable[hash_int] = new_word;
            }
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
        int hashint = hash(searchstr);
        WikiItem curr = hashTable[hashint];  
        while (curr != null) 
        {
            if(curr.str.equals(searchstr)){return curr.docs;}
            curr = curr.next;    
        }
        return null;
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
            }
            result = (result % p + p) % p;
            return ((a2*result + b) % p + p) % p;
        }
 
    public static void main(String[] args) {
        
        int [] hashvals = new int[4];
        hashvals[0] = 98317;
        hashvals[1] = ThreadLocalRandom.current().nextInt(1, hashvals[0]);
        hashvals[2] = ThreadLocalRandom.current().nextInt(1, hashvals[0]);
        hashvals[3] = ThreadLocalRandom.current().nextInt(1, hashvals[0]);
        System.out.println("Preprocessing " + args[0]);
        Index4 i = new Index4(args[0], hashvals);
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