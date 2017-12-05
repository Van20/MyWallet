package id.sch.smktelkom_mlg.mywallet.Model;


public class Saldo {

    int id;
    int saldo;

    public Saldo(int id, int saldo) {
        this.id = id;
        this.saldo = saldo;
    }
    public void setSaldo(int saldo) {
        this.saldo = saldo;
    }
    public void setId(int id) {
        this.id = id;
    }
    public int getSaldo() {
        return saldo;
    }
    public int getId() {
        return id;
    }
}
