package mapaprog3;

import javax.swing.JOptionPane;

public class Aluguel {
    int id;
    Carro carro;
    Cliente cliente;
    int qtdDias;
    int kmInicial;
    int kmFinal;
    double valorTotalAluguel;

    public void iniciarLocacao() {
        cliente = new Cliente();
        cliente.cadastrarUsuario();

        carro = new Carro();
        carro.cadastrarCarro();

        kmInicial = Integer.parseInt(JOptionPane.showInputDialog("Informe o Km inicial do veículo:"));
    }

    public void fecharLocacao() {
        kmFinal = Integer.parseInt(JOptionPane.showInputDialog("Informe o Km final do veículo:"));
        int diasLocados = qtdDias;
        double valorPorKm = carro.valorPorKm;
        
        if (diasLocados > 20) {
            valorTotalAluguel = diasLocados * valorPorKm * 0.8;
        } else if (diasLocados > 10) {
            valorTotalAluguel = diasLocados * valorPorKm * 0.9;
        } else {
            valorTotalAluguel = diasLocados * valorPorKm;
        }
    }

    public void mostrarResumoLocacao() {
        JOptionPane.showMessageDialog(null, "Resumo Aluguel\nCliente\n" + cliente.mostrarDadosUsuario() + 
                                        "\nCarro\n" + carro.mostrarDadosCarro() + 
                                        "\nAluguel\nQuantidade de dias: " + qtdDias + 
                                        "\nKm Inicial: " + kmInicial + "\nKm Final: " + kmFinal + 
                                        "\nValor Total: R$" + valorTotalAluguel);
    }
}

