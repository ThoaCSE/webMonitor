package strategy;

public class sizeComparison implements webComparision {
    @Override
    public boolean hasChanged(String oldContent, String newContent) {
        if (oldContent == null || newContent == null) {
            return oldContent != newContent;
        }
        return oldContent.length() != newContent.length();
    }
}