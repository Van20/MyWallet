package id.sch.smktelkom_mlg.mywallet.Model;

/**
 * Created by fianxeka on 24/06/17.
 */

public class Report {

    int idkategori;
    int jumlah;
    int total;
    String namakategori;

    public Report( ) {
    }

    public Report(int idkategori, int jumlah, int total, String namakategori) {
        this.idkategori = idkategori;
        this.jumlah = jumlah;
        this.total = total;
        this.namakategori = namakategori;
    }

    public int getIdkategori() {
        return idkategori;
    }

    public void setIdkategori(int idkategori) {
        this.idkategori = idkategori;
    }

    public int getJumlah() {
        return jumlah;
    }

    public void setJumlah(int jumlah) {
        this.jumlah = jumlah;
    }

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    public String getNamakategori() {
        return namakategori;
    }

    public void setNamakategori(String namakategori) {
        this.namakategori = namakategori;
    }

    @Override
    public String toString() {
        return "Laporan{" +
                "idkategori=" + idkategori +
                ", jumlah=" + jumlah +
                ", total=" + total +
                ", namakategori='" + namakategori + '\'' +
                '}';
    }
}
