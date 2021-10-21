package consultabc;

import java.math.BigDecimal;
import java.text.ParseException;
import java.time.LocalDate;
import java.util.Date;

public class Cotacao {

    private LocalDate data;
    private Moeda moeda;
    private BigDecimal valorCompra;
    private BigDecimal valorVenda;
    private BigDecimal paridadeCompra;
    private BigDecimal paridadeVenda;

    public Cotacao(LocalDate data, Moeda moeda, BigDecimal valorCompra, BigDecimal valorVenda, BigDecimal paridadeCompra, BigDecimal paridadeVenda) {
        this.data = data;
        this.moeda = moeda;
        this.valorCompra = valorCompra;
        this.valorVenda = valorVenda;
        this.paridadeCompra = paridadeCompra;
        this.paridadeVenda = paridadeVenda;
    }

    public LocalDate getData() {
        return data;
    }

    public Date getDataAsDate() throws ParseException {
        return ConsultaBC.FORMATADOR_YYYYMMDD.parse(data.toString());
    }

    public Moeda getMoeda() {
        return moeda;
    }

    public BigDecimal getValorCompra() {
        return valorCompra;
    }

    public BigDecimal getValorVenda() {
        return valorVenda;
    }

    public BigDecimal getParidadeCompra() {
        return paridadeCompra;
    }

    public BigDecimal getParidadeVenda() {
        return paridadeVenda;
    }

    public void setMoeda(Moeda moeda) {
        this.moeda = moeda;
    }

    public void setData(LocalDate data) {
        this.data = data;
    }

    public void setValorCompra(BigDecimal valorCompra) {
        this.valorCompra = valorCompra;
    }

    public void setValorVenda(BigDecimal valorVenda) {
        this.valorVenda = valorVenda;
    }

    public void setParidadeCompra(BigDecimal paridadeCompra) {
        this.paridadeCompra = paridadeCompra;
    }

    public void setParidadeVenda(BigDecimal paridadeVenda) {
        this.paridadeVenda = paridadeVenda;
    }

}
