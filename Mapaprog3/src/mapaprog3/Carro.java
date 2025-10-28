package mapaprog3;

import javax.swing.JOptionPane;

public class Carro {
    int id;
    String modelo;
    String marca;
    double valorPorKm;

    public void cadastrarCarro() {
        modelo = JOptionPane.showInputDialog("Informe o modelo do carro:");
        marca = JOptionPane.showInputDialog("Informe a marca do carro:");
        valorPorKm = Double.parseDouble(JOptionPane.showInputDialog("Informe o valor por Km do carro:"));
    }

    public String mostrarDadosCarro() {
        return "Modelo: " + modelo + "\nMarca: " + marca + "\nValor por Km: " + valorPorKm;
    }
}
