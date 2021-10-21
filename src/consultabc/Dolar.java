package consultabc;

import br.com.sankhya.jape.EntityFacade;
import br.com.sankhya.jape.sql.NativeSql;
import br.com.sankhya.jape.vo.DynamicVO;
import br.com.sankhya.jape.vo.EntityVO;
import br.com.sankhya.modelcore.util.EntityFacadeFactory;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.List;
import org.cuckoo.core.ScheduledAction;
import org.cuckoo.core.ScheduledActionContext;

/**
 *
 * @author Vitor Ribeiro dos Santos
 */
public class Dolar implements ScheduledAction {
    
    private static final BigDecimal CODMOEDA = new BigDecimal(2);

    @Override
    public void onTime(ScheduledActionContext sac) {
        try {
            EntityFacade dwfEntityFacade = EntityFacadeFactory.getDWFFacade();
            String ultimaData = NativeSql.getString("TO_CHAR(MAX(DTMOV + 1),'MM-DD-YYYY')", "TSICOT", "CODMOEDA = ?", CODMOEDA);
            if (ultimaData.isEmpty())
                ultimaData = "01-04-2016";

            ConsultaBC bc = new ConsultaBC();
            List<Cotacao> cotacoes = bc.getCotacaoDolarPeriodo(ultimaData);

            for (Cotacao cot : cotacoes) {
                DynamicVO cotacaoVO = (DynamicVO) dwfEntityFacade.getDefaultValueObjectInstance("CotacaoMoeda");

                cotacaoVO.setProperty("CODMOEDA", CODMOEDA);
                cotacaoVO.setProperty("DTMOV", new Timestamp(cot.getDataAsDate().getTime()));
                cotacaoVO.setProperty("COTACAO", cot.getValorVenda());

                dwfEntityFacade.createEntity("CotacaoMoeda", (EntityVO) cotacaoVO);
            }

        } catch (Exception ex) {
            sac.info("Erro ao inserir dolar: " + ex.getMessage());
        }
    }
}
