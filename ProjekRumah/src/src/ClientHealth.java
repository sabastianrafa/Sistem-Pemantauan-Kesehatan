package src;

public class ClientHealth extends Client implements Evaluasi {
    private double beratBadan;
    private double tinggiBadan;

    // Constructor overloading
    public ClientHealth(String nama, double beratBadan, double tinggiBadan) {
        super(nama);
        this.beratBadan = beratBadan;
        this.tinggiBadan = tinggiBadan;
    }

    public ClientHealth(String nama, String jenisKelamin, int usia, double beratBadan, double tinggiBadan) {
        super(nama, jenisKelamin, usia);
        this.beratBadan = beratBadan;
        this.tinggiBadan = tinggiBadan;
    }

    // Getter dan Setter
    public double getBeratBadan() {
        return beratBadan;
    }

    public void setBeratBadan(double beratBadan) {
        this.beratBadan = beratBadan;
    }

    public double getTinggiBadan() {
        return tinggiBadan;
    }

    public void setTinggiBadan(double tinggiBadan) {
        this.tinggiBadan = tinggiBadan;
    }

    public double hitungBMI() {
        double tinggiMeter = tinggiBadan / 100;
        return beratBadan / (tinggiMeter * tinggiMeter);
    }

    @Override
    public String getInfo() {
        double bmi = hitungBMI();
        String status;
        if (bmi < 18.5) {
            status = "Kurus";
        } else if (bmi < 25) {
            status = "Normal";
        } else if (bmi < 30) {
            status = "Gemuk";
        } else {
            status = "Obesitas";
        }

        return status;
    }
}