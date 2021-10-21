package consultabc;

import br.com.sankhya.jape.EntityFacade;
import br.com.sankhya.jape.sql.NativeSql;
import br.com.sankhya.jape.vo.DynamicVO;
import br.com.sankhya.jape.vo.EntityVO;
import br.com.sankhya.modelcore.util.EntityFacadeFactory;
import com.sankhya.util.TimeUtils;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;
import org.cuckoo.core.ScheduledAction;
import org.cuckoo.core.ScheduledActionContext;

/**
 *
 * @author Vitor Ribeiro dos Santos
 */
public class Euro implements ScheduledAction {

    private static final BigDecimal CODMOEDA = new BigDecimal(5);

    @Override
    public void onTime(ScheduledActionContext sac) {
        try {
            EntityFacade dwfEntityFacade = EntityFacadeFactory.getDWFFacade();
            String ultimaData = NativeSql.getString("TO_CHAR(MAX(DTMOV + 1),'MM-DD-YYYY')", "TSICOT", "CODMOEDA = ?", CODMOEDA);
            if (ultimaData.isEmpty())
                ultimaData = "01-04-2016";
            ConsultaBC bc = new ConsultaBC();

            Date dataInicial = ConsultaBC.FORMATADOR_SDF_MMDDYYYY.parse(ultimaData);

            LocalDate start = dataInicial.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
            Date dataFinal = ConsultaBC.FORMATADOR_SDF_MMDDYYYY.parse(TimeUtils.getNow("MM-DD-YYYY"));
            LocalDate end = dataFinal.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();

            for (LocalDate date = start; date.isBefore(end); date = date.plusDays(1)) {
                Cotacao cot = bc.getCotacao(Moeda.EUR, date);
                if (cot != null) {
                    DynamicVO cotacaoVO = (DynamicVO) dwfEntityFacade.getDefaultValueObjectInstance("CotacaoMoeda");

                    cotacaoVO.setProperty("CODMOEDA", CODMOEDA);
                    cotacaoVO.setProperty("DTMOV", new Timestamp(cot.getDataAsDate().getTime()));
                    cotacaoVO.setProperty("COTACAO", cot.getValorVenda());

                    dwfEntityFacade.createEntity("CotacaoMoeda", (EntityVO) cotacaoVO);
                }
            }

        } catch (Exception ex) {
            sac.info("Erro ao inserir EURO: " + ex.getMessage());
        }
    }
}
