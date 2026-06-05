package strategy;

public class htmlComparision implements webComparision {
    @Override
    public boolean hasChanged(String oldContent, String newContent) {
        if (oldContent == null || newContent == null) {
            return oldContent != newContent;
        }
        return !oldContent.equals(newContent);
    }
}