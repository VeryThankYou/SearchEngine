package application;

class DocItem {
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