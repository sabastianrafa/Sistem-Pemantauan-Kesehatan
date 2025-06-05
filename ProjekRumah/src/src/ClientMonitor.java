package src;

public class ClientMonitor extends Client {
    private double kaloriKonsumsi;
    private boolean diet;

    // Constructor overloading
    public ClientMonitor(String nama, double kaloriKonsumsi) {
        super(nama);
        this.kaloriKonsumsi = kaloriKonsumsi;
        this.diet = false;
    }

    public ClientMonitor(String nama, String jenisKelamin, int usia, double kaloriKonsumsi, boolean diet) {
        super(nama, jenisKelamin, usia);
        this.kaloriKonsumsi = kaloriKonsumsi;
        this.diet = diet;
    }

    public ClientMonitor(String nama, String jenisKelamin, int usia) {
        super(nama, jenisKelamin, usia);
        this.kaloriKonsumsi = 0;
        this.diet = false;
    }

    // Getter dan Setter
    public double getKaloriKonsumsi() {
        return kaloriKonsumsi;
    }

    public void setKaloriKonsumsi(double kaloriKonsumsi) {
        this.kaloriKonsumsi = kaloriKonsumsi;
    }

    public boolean isDiet() {
        return diet;
    }

    public void setDiet(boolean diet) {
        this.diet = diet;
    }

    // Method overloading
    public String evaluasiKalori(double konsumsiKalori) {
        if (konsumsiKalori >= 2900) return "Kalori berlebih";
        else if (konsumsiKalori >= 2500) return "Kalori tercukupi";
        else if (konsumsiKalori >= 2250) return "Kalori hampir cukup";
        else return "Kalori kurang";
    }

    public String evaluasiKalori(double konsumsiKalori, boolean diet) {
        if (diet) {
            if (konsumsiKalori >= 2000) return "Kalori berlebih untuk diet";
            else if (konsumsiKalori >= 1600) return "Kalori tercukupi untuk diet";
            else return "Kalori kurang untuk diet";
        } else {
            return evaluasiKalori(konsumsiKalori);
        }
    }

    @Override
    public String getInfo() {
        return "Client Monitoring - " + getNama() + "\n"
                + "Jenis Kelamin: " + getJenisKelamin() + "\n"
                + "Usia: " + getUsia() + "\n"
                + "Kalori Konsumsi: " + kaloriKonsumsi + " kkal\n"
                + "Status Kalori: " + evaluasiKalori(kaloriKonsumsi, diet);
    }
}