package cloud.multimicro.mmc.Dao;

import cloud.multimicro.mmc.Entity.TMmcParametrage;
import cloud.multimicro.mmc.Entity.VPosCa;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.YearMonth;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 *
 * @author Tsiory
 */
@Stateless
public class PosRevenueDao {
        @PersistenceContext
        private EntityManager entityManager;

        // v_pos_ca
        public List<VPosCa> getVPosCa() {
                List<VPosCa> caLists = entityManager.createQuery("FROM VPosCa ").getResultList();
                return caLists;
        }

        public List<VPosCa> getVPosCaByDate(LocalDate dateStart, LocalDate dateEnd) {
                List<VPosCa> caLists = entityManager.createQuery("FROM VPosCa ").getResultList();

                return caLists.stream()
                                .filter(ca -> (ca.getDateCa().isAfter(dateStart) && ca.getDateCa().isBefore(dateEnd))
                                                || ca.getDateCa().equals(dateStart) || ca.getDateCa().equals(dateEnd))
                                .collect(Collectors.groupingBy(VPosCa::getDateCa,
                                                Collectors.reducing(BigDecimal.ZERO, VPosCa::getMontantCa,
                                                                BigDecimal::add)))
                                .entrySet().stream().map(e -> new VPosCa(e.getKey(), e.getValue()))
                                .sorted(Comparator.comparing(VPosCa::getDateCa)).collect(Collectors.toList());

        }

        public List<VPosCa> getVPosCaByMonth(LocalDate dateStart, LocalDate dateEnd) {
                List<VPosCa> caLists = entityManager.createQuery("FROM VPosCa ").getResultList();

                return caLists.stream()
                                .filter(ca -> (ca.getDateCa().isAfter(dateStart) && ca.getDateCa().isBefore(dateEnd))
                                                || ca.getDateCa().equals(dateStart) || ca.getDateCa().equals(dateEnd))
                                .collect(Collectors.groupingBy(VPosCa::getYearMonthNote,
                                                Collectors.reducing(BigDecimal.ZERO, VPosCa::getMontantCa,
                                                                BigDecimal::add)))
                                .entrySet().stream().map(e -> new VPosCa(e.getKey(), e.getValue()))
                                .sorted(Comparator.comparing(VPosCa::getYearMonthNote)).collect(Collectors.toList());

        }

        public List<VPosCa> getVPosCaByYear(LocalDate dateStart, LocalDate dateEnd) {
                List<VPosCa> caLists = entityManager.createQuery("FROM VPosCa ").getResultList();

                return caLists.stream()
                                .filter(ca -> (ca.getDateCa().isAfter(dateStart) && ca.getDateCa().isBefore(dateEnd))
                                                || ca.getDateCa().equals(dateStart) || ca.getDateCa().equals(dateEnd))
                                .collect(Collectors.groupingBy(VPosCa::getYearNote,
                                                Collectors.reducing(BigDecimal.ZERO, VPosCa::getMontantCa,
                                                                BigDecimal::add)))
                                .entrySet().stream().map(e -> new VPosCa(e.getKey(), e.getValue()))
                                .sorted(Comparator.comparing(VPosCa::getYearNote)).collect(Collectors.toList());

        }

        public List<VPosCa> getVPosCaByPoste(LocalDate dateLogicielle) {
                List<VPosCa> caLists = entityManager.createQuery("FROM VPosCa Where dateCa=:dateLogicielle ")
                                .setParameter("dateLogicielle", dateLogicielle).getResultList();

                return caLists.stream()
                                .collect(Collectors.groupingBy(VPosCa::getPosteUuid,
                                                Collectors.reducing(BigDecimal.ZERO, VPosCa::getMontantCa,
                                                                BigDecimal::add)))
                                .entrySet().stream().map(e -> new VPosCa(e.getValue(), e.getKey()))
                                .collect(Collectors.toList());

        }

        public LocalDate getDateLogicielle() {
                TMmcParametrage settingData = entityManager.find(TMmcParametrage.class, "DATE_LOGICIELLE");
                return LocalDate.parse(settingData.getValeur());
        }


}
