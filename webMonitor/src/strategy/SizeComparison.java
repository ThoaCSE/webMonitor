package strategy;

public class SizeComparison implements WebComparison {
    @Override
    public boolean hasChanged(String oldContent, String newContent) {
        if (oldContent == null || newContent == null) {
            return oldContent != newContent;
        }
        return oldContent.length() != newContent.length();
    }
}