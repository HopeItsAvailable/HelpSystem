package helpSystem;

public class Article {
    private int id;
    private String title;
    private String author;
    private String paperAbstract;
    private String keywords;
    private String body;
    private String references;
    private String level;

    public Article(int id, String title, String author, String paperAbstract, String keywords, String body, String references, String level) {
        this.id = id;
        this.title = title;
        this.author = author;
        this.paperAbstract = paperAbstract;
        this.keywords = keywords;
        this.body = body;
        this.references = references;
        this.level = level;
    }

    public int getId() { return id; }
    public String getTitle() { return title; }
    public String getAuthor() { return author; }
    public String getPaperAbstract() { return paperAbstract; }
    public String getKeywords() { return keywords; }
    public String getBody() { return body; }
    public String getReferences() { return references; }
    public String getLevel() { return level; }
}

