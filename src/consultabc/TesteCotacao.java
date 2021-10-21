package consultabc;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;

/**
 *
 * @author Vitor Ribeiro dos Santos
 */
public class TesteCotacao {

    public static SimpleDateFormat FORMATADOR = new SimpleDateFormat("MM-dd-yyyy");

    public static void main(String[] args) throws IOException, ParseException {

        ConsultaBC bc = new ConsultaBC();
        Date dtIni = FORMATADOR.parse("01-01-2021");
        
        LocalDate s = dtIni.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
        List<Cotacao> cotacoes = bc.getCotacaoDolarPeriodo("08-01-2021");

        for (Cotacao cot : cotacoes) {
            System.out.println("Dt. Cotação: " + FORMATADOR.format(cot.getDataAsDate()));
            System.out.println("Valor Venda = " + cot.getValorVenda());
        }
        
        /* Date dataInicial = FORMATADOR.parse("07-01-2021");
        //Date dataFinal = FORMATADOR.parse("08-01-2021");

        LocalDate start = dataInicial.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
        Date dataFinal = FORMATADOR.parse("08-03-2021");
        LocalDate end = dataFinal.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
        
        for (LocalDate date = start; date.isBefore(end); date = date.plusDays(1)) {
            
            Cotacao1 cot = bc.getCotacao(Moeda.EUR, date);

            if(cot != null)
                //System.out.println("Dt. Cotação: " + cot.getData());
                System.out.println("Valor Venda: " + date + " - " + cot.getValorVenda());
        }*/
    }

}
