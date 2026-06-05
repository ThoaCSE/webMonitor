package strategy;

public class textComparision implements webComparision {
    @Override
    public boolean hasChanged(String oldContent, String newContent) {
        if (oldContent == null || newContent == null) {
            return oldContent != newContent;
        }

        String oldText = extractPlainText(oldContent);
        String newText = extractPlainText(newContent);
        
        return !oldText.equals(newText);
    }

    private String extractPlainText(String html) {
        if (html == null) {
            return "";
        }
        String textOnly = html.replaceAll("<[^>]*>", " ");
        return textOnly.replaceAll("\\s+", " ").trim();
    }
}