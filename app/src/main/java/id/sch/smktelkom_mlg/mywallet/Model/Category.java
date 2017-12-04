package id.sch.smktelkom_mlg.mywallet.Model;

/**
 * Created by fianxeka on 18/06/17.
 */

public class Category {

    int id;
    String kategori;

    //contructor
    public Category(){

    }
    public Category(int id, String kategori) {
        this.id = id;
        this.kategori = kategori;
    }

    //setter
    public void setId(int id) {
        this.id = id;
    }

    public void setKategori(String kategori) {
        this.kategori = kategori;
    }

    //getter
    public int getId() {
        return id;
    }
    public String getKategori() {
        return kategori;
    }

    @Override
    public String toString() {
        return kategori ;
    }


}
