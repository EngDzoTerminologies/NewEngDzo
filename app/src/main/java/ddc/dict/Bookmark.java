package ddc.dict;


public class Bookmark {

    private String en_word;
    private String dzo_word;
    private String category;

    public Bookmark(String en_word, String dzo_word, String category)
    {
        this.en_word = en_word;
        this.dzo_word = dzo_word;
        this.category = category;
    }

    public String get_en_word()
    {
        return en_word;
    }

    public String get_dzo_word()
    {
        return dzo_word;
    }

    public String get_category()
    {
        return category;
    }

}
