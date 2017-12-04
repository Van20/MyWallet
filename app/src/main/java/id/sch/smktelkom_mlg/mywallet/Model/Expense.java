package id.sch.smktelkom_mlg.mywallet.Model;

/**
 * Created by fianxeka on 20/06/17.
 */

public class Expense {
    int id;
    int idkat;
    String deskripsi;
    int jumlah;
    String tanggal;

    public Expense() {
    }

    public Expense(int id, int idkat, String deskripsi, int jumlah, String tanggal) {
        this.id = id;
        this.idkat = idkat;
        this.deskripsi = deskripsi;
        this.jumlah = jumlah;
        this.tanggal = tanggal;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getIdkat() {
        return idkat;
    }

    public void setIdkat(int idkat) {
        this.idkat = idkat;
    }

    public String getDeskripsi() {
        return deskripsi;
    }

    public void setDeskripsi(String deskripsi) {
        this.deskripsi = deskripsi;
    }

    public int getJumlah() {
        return jumlah;
    }

    public void setJumlah(int jumlah) {
        this.jumlah = jumlah;
    }

    public String getTanggal() {
        return tanggal;
    }

    public void setTanggal(String tanggal) {
        this.tanggal = tanggal;
    }
}
