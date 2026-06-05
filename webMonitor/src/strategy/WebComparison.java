package strategy;

public interface WebComparison {
    boolean hasChanged(String oldContent, String newContent);
}
