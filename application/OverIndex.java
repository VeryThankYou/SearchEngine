package application;

public interface OverIndex 
{
    public void Build(String Filename, int[] hashvals);

    public DocItem search(String searchstring);
}
