package goulart.bancoimobiliario;

public class dadosJogadores {
    private int id;
    private String nome, totem, dataEntrada;
    private double dinheiro;

    public dadosJogadores(int id, String nome, String totem, String dataEntrada, double dinheiro) {
        this.id = id;
        this.nome = nome;
        this.totem = totem;
        this.dataEntrada = dataEntrada;
        this.dinheiro = dinheiro;
    }

    public String getTotem() {
        return totem;
    }

    public void setTotem(String totem) {
        this.totem = totem;
    }

    public String getDataEntrada() {
        return dataEntrada;
    }

    public void setDataEntrada(String dataEntrada) {
        this.dataEntrada = dataEntrada;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public double getDinheiro() {
        return dinheiro;
    }

    public void setDinheiro(double dinheiro) {
        this.dinheiro = dinheiro;
    }
}