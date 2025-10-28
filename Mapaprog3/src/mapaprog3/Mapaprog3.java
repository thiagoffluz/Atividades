package mapaprog3;
public class Mapaprog3 {
    public static void main(String[] args) {
        Aluguel aluguel = new Aluguel();
        aluguel.qtdDias = 21; // Defina a quantidade de dias locados aqui
        aluguel.iniciarLocacao();
        aluguel.fecharLocacao();
        aluguel.mostrarResumoLocacao();
    }   
}
