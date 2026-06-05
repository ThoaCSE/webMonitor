package strategy;

public class HtmlComparison implements WebComparison {
    @Override
    public boolean hasChanged(String oldContent, String newContent) {
        if (oldContent == null || newContent == null) {
            return oldContent != newContent;
        }
        return !oldContent.equals(newContent);
    }
}