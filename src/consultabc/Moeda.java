package consultabc;

public enum Moeda {

    DKK("Coroa Dinamarquesa"),
    NOK("Coroa Norueguesa"),
    SEK("Coroa Sueca"),
    USD("Dólar Norte-Americano"),
    AUD("Dólar Australiano"),
    CAD("Dólar Canadense"),
    EUR("Euro"),
    CHF("Franco Suiço"),
    JPY("Yen"),
    GBP("Libra Esterlina");

    private String descricao;

    private Moeda(String descricao) {
        this.descricao = descricao;
    }

    public String getDescricao() {
        return descricao;
    }
}
