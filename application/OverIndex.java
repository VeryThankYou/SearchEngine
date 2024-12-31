package application;

public interface OverIndex 
{
    public int memoryuse();

    public void resetMemoryuse();

    public void Build(String Filename, int[] hashvals);

    public DocItem search(String searchstring);
}
