package cloud.multimicro.mmc.Dao;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import cloud.multimicro.mmc.Entity.TMmcParametrage;
import cloud.multimicro.mmc.Entity.VPosEncaissement;

/**
 *
 * @author Tsiory
 */
@Stateless
@SuppressWarnings("unchecked")
public class PosCheckoutDao {
    @PersistenceContext
    private EntityManager entityManager;

    public List<VPosEncaissement> getVPosEncaissement() {
        List<VPosEncaissement> cashing = entityManager.createQuery("FROM VPosEncaissement ").getResultList();
        return cashing;
    }

    public List<VPosEncaissement> getVPosEncaissementByDevice(LocalDate dateLogicielle) {
        List<VPosEncaissement> ecLists = entityManager
                .createQuery("FROM VPosEncaissement Where dateEncaissement=:dateLogicielle ")
                .setParameter("dateLogicielle", dateLogicielle).getResultList();

        return ecLists.stream()
                .collect(Collectors.groupingBy(VPosEncaissement::getPosteUuid,
                        Collectors.reducing(BigDecimal.ZERO, VPosEncaissement::getMontantTtc, BigDecimal::add)))
                .entrySet().stream().map(e -> new VPosEncaissement(e.getValue(), e.getKey()))
                .collect(Collectors.toList());

    }

    public LocalDate getDateLogicielle() {
        TMmcParametrage settingData = entityManager.find(TMmcParametrage.class, "DATE_LOGICIELLE");
        return LocalDate.parse(settingData.getValeur());
    }
}
