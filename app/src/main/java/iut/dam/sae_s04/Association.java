package iut.dam.sae_s04;

public class Association {
    private String title;
    private String description;
    private int logoResId; // ID de l'image drawable
    private String cat;
    private String acro;
    public Association(String title, int logoResId, String description, String cat,String acro) {
        this.title = title;
        this.description = description;
        this.logoResId = logoResId;
        this.cat = cat;
        this.acro=acro;
    }

    public String getTitle() { return title; }
    public String getDescription() { return description; }
    public int getLogoResId() { return logoResId; }
    public String getCat() { return cat; }

    public String getAcro() {
        return acro;
    }

    @Override
    public String toString() {
        return title;
    }
}