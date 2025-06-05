package src;

public abstract class Client {
    private String nama;
    private String jenisKelamin;
    private int usia;
    // Constructor overloading
    public Client(String nama, String jenisKelamin, int usia) {
        this.nama = nama;
        this.jenisKelamin = jenisKelamin;
        this.usia = usia;
    }
    public Client(String nama) {
        this(nama, "Tidak Diketahui", 0);
    }
    // Getter
    public String getNama() {
        return nama;
    }
    public String getJenisKelamin() {
        return jenisKelamin;
    }
    public int getUsia() {
        return usia;
    }
    // Setter
    public void setNama(String nama) {
        this.nama = nama;
    }
    public void setJenisKelamin(String jenisKelamin) {
        this.jenisKelamin = jenisKelamin;
    }
    public void setUsia(int usia) {
        this.usia = usia;
    }
    // Abstract method yang harus dioverride
    public abstract String getInfo();
}
