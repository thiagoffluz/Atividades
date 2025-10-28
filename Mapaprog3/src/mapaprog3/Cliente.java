package mapaprog3;

import javax.swing.JOptionPane;

public class Cliente {
    int id;
    String nome;
    String cpf;
    String telefone;

    public void cadastrarUsuario() {
        nome = JOptionPane.showInputDialog("Informe o nome do cliente:");
        cpf = JOptionPane.showInputDialog("Informe o CPF do cliente:");
        telefone = JOptionPane.showInputDialog("Informe o telefone do cliente:");
    }

    public String mostrarDadosUsuario() {
        return "Nome: " + nome + "\nCPF: " + cpf + "\nTelefone: " + telefone;
    }
}
