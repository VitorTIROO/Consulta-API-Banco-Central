package consultabc;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.sankhya.util.TimeUtils;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.math.BigDecimal;
import java.net.URL;
import java.net.URLConnection;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ConsultaBC {

    public static final DateTimeFormatter FORMATADOR_MMDDYYYY = DateTimeFormatter.ofPattern("MM-dd-yyyy");
    public static SimpleDateFormat FORMATADOR_SDF_MMDDYYYY = new SimpleDateFormat("MM-dd-yyyy");
    public static DateFormat FORMATADOR_YYYYMMDD = new SimpleDateFormat("yyyy-MM-dd");

    public Cotacao getCotacao(Moeda moeda, LocalDate dataMMDDYYYY) throws IOException {

        URL url = new URL("https://olinda.bcb.gov.br/olinda/servico/PTAX/versao/v1/odata/CotacaoMoedaDia(moeda='" + moeda + "',dataCotacao='"
                + dataMMDDYYYY.format(FORMATADOR_MMDDYYYY) + "')?$top=1&$format=json&$select=cotacaoCompra,cotacaoVenda,paridadeCompra,paridadeVenda");
        URLConnection request = url.openConnection();
        request.connect();

        try (InputStreamReader isr = new InputStreamReader((InputStream) request.getContent())) {
            JsonObject jsonObject = new JsonParser().parse(isr).getAsJsonObject();
            for (JsonElement cotacaoElement : jsonObject.getAsJsonArray("value")) {
                JsonObject cotacaoObject = cotacaoElement.getAsJsonObject();
                return new Cotacao(dataMMDDYYYY, moeda, cotacaoObject.get("cotacaoCompra").getAsBigDecimal(), cotacaoObject.get("cotacaoVenda").getAsBigDecimal(),
                        cotacaoObject.get("paridadeCompra").getAsBigDecimal(), cotacaoObject.get("paridadeVenda").getAsBigDecimal());
            }
        }
        return null;
    }

    public List<Cotacao> getCotacaoDolarPeriodo(String dataMMDDYYYY) throws IOException {

        URL url = new URL("https://olinda.bcb.gov.br/olinda/servico/PTAX/versao/v1/odata/CotacaoDolarPeriodo(dataInicial=@dataInicial,dataFinalCotacao=@dataFinalCotacao)?"
                + "@dataInicial='" + dataMMDDYYYY + "'&@dataFinalCotacao='" + TimeUtils.getNow("MM-dd-yyyy") + "'&$top=5000&$format=json&$select=cotacaoCompra,cotacaoVenda,dataHoraCotacao");

        URLConnection request = url.openConnection();
        request.connect();

        try (InputStreamReader isr = new InputStreamReader((InputStream) request.getContent())) {
            List<Cotacao> cotacoes = new ArrayList<>();
            JsonObject jsonObject = new JsonParser().parse(isr).getAsJsonObject();
            for (JsonElement cotacaoElement : jsonObject.getAsJsonArray("value")) {

                JsonObject cotacaoObject = cotacaoElement.getAsJsonObject();

                Date d = FORMATADOR_YYYYMMDD.parse(cotacaoObject.get("dataHoraCotacao").toString().substring(1, 11));
                LocalDate dataCotacao = d.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();

                Cotacao cot = new Cotacao(dataCotacao, Moeda.USD, cotacaoObject.get("cotacaoCompra").getAsBigDecimal(), cotacaoObject.get("cotacaoVenda").getAsBigDecimal(),
                        BigDecimal.ONE, BigDecimal.ONE);
                cotacoes.add(cot);
            }
            return cotacoes;
        } catch (ParseException ex) {
            Logger.getLogger(ConsultaBC.class.getName()).log(Level.SEVERE, null, ex);
        }

        return null;
    }

    public List<Cotacao> getCotacaoMoedaPeriodo(Moeda moeda, String dataMMDDYYYY) throws IOException {
        
        final URL url = new URL("https://olinda.bcb.gov.br/olinda/servico/PTAX/versao/v1/odata/CotacaoMoedaPeriodo(moeda='" + moeda + "',dataInicial='" + dataMMDDYYYY + "',"
                + "dataFinalCotacao='" + TimeUtils.getNow("MM-dd-yyyy") + "')?"
                + "$top=5000&"
                + "$format=json&"
                + "$select=cotacaoCompra,cotacaoVenda,dataHoraCotacao,paridadeCompra,paridadeVenda");

        URLConnection request = url.openConnection();
        request.connect();

        try (InputStreamReader isr = new InputStreamReader((InputStream) request.getContent())) {
            List<Cotacao> cotacoes = new ArrayList<>();
            JsonObject jsonObject = new JsonParser().parse(isr).getAsJsonObject();
            for (JsonElement cotacaoElement : jsonObject.getAsJsonArray("value")) {

                JsonObject cotacaoObject = cotacaoElement.getAsJsonObject();

                Date d = FORMATADOR_YYYYMMDD.parse(cotacaoObject.get("dataHoraCotacao").toString().substring(1, 11));
                LocalDate dataCotacao = d.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();

                Cotacao cot = new Cotacao(dataCotacao, moeda, cotacaoObject.get("cotacaoCompra").getAsBigDecimal(), cotacaoObject.get("cotacaoVenda").getAsBigDecimal(),
                        cotacaoObject.get("paridadeCompra").getAsBigDecimal(), cotacaoObject.get("paridadeVenda").getAsBigDecimal());
                cotacoes.add(cot);
            }
            return cotacoes;
        } catch (ParseException ex) {
            Logger.getLogger(ConsultaBC.class.getName()).log(Level.SEVERE, null, ex);
        }

        return null;
    }
}
