/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cloud.multimicro.mmc.Dao;

import cloud.multimicro.mmc.Entity.TMmcParametrage;
import cloud.multimicro.mmc.Entity.TMmcUser;
import cloud.multimicro.mmc.Entity.TPosCalendrierServeur;
import cloud.multimicro.mmc.Entity.WaiterCalendarDashboard;
import cloud.multimicro.mmc.Exception.CustomConstraintViolationException;
import static java.lang.Integer.parseInt;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.Month;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.validation.ConstraintViolationException;
import org.jboss.logging.Logger;

/**
 *
 * @author Naly
 */
@Stateless
@SuppressWarnings("unchecked")
public class ServerCalendarDao {

    @PersistenceContext
    private EntityManager entityManager;

    private static final Logger LOGGER = Logger.getLogger(ServerCalendarDao.class);

    // GET ALL
    public List<TPosCalendrierServeur> getAll() {
        List<Object[]> calendrierServeur = entityManager.createQuery(
                "FROM TPosCalendrierServeur cS LEFT JOIN TMmcUser u ON  cS.mmcUserId = u.id where u.dateDeletion = null ")
                .getResultList();
        List<TPosCalendrierServeur> result = new ArrayList<TPosCalendrierServeur>();
        // Retrieve the corresponding sub-family
        for (Object[] cServeur : calendrierServeur) {
            if (cServeur.length > 1) {
                TPosCalendrierServeur cS = (TPosCalendrierServeur) cServeur[0];
                TMmcUser u = (TMmcUser) cServeur[1];
                cS.setMmcUserFirstname(u.getFirstname());
                result.add(cS);
            }
        }
        return result;
    }

    // GET ALL
    public List<TMmcUser> getServeur() {
        List<TMmcUser> users = entityManager
                .createQuery("FROM TMmcUser u where u.dateDeletion = null ").getResultList();
        return users;
    }

    // GET BY ID
    public TPosCalendrierServeur getCalendrierServeurById(int id) {
        TPosCalendrierServeur cServeur = entityManager.find(TPosCalendrierServeur.class, id);
        return cServeur;
    }

    // ADD CALENDRIER SERVEUR
    public void setCalendrierServeur(TPosCalendrierServeur cServeur) throws CustomConstraintViolationException {
        try {
            entityManager.persist(cServeur);
        } catch (ConstraintViolationException ex) {
            throw new CustomConstraintViolationException(ex);
        }
    }

    // EDIT CALENDRIER SERVEUR
    public TPosCalendrierServeur updateCalendrierServeur(TPosCalendrierServeur cServeur)
            throws CustomConstraintViolationException {
        try {
            return entityManager.merge(cServeur);
        } catch (ConstraintViolationException ex) {
            throw new CustomConstraintViolationException(ex);
        }
    }

    // DELETE CALENDRIER SERVEUR
    public void deleteCalendrierServeur(int id) {
        TPosCalendrierServeur cServeur = getCalendrierServeurById(id);
        entityManager.remove(cServeur);
    }

    // recuperation de tableau de bord ,calendrier de serveur
    public List<WaiterCalendarDashboard> getCalendar(int nbOfMonth) {
        List<WaiterCalendarDashboard> waiterCalendarDashboardList = new ArrayList<WaiterCalendarDashboard>();
        WaiterCalendarDashboard waiterCalendarDashboard = new WaiterCalendarDashboard();
        List<TPosCalendrierServeur> calendrierServeurList = this.getAll();

        List<LocalDate> monthList = this.getLastMonth(nbOfMonth);

        for (TPosCalendrierServeur cServeur : calendrierServeurList) {
            for (LocalDate dateDash : monthList) {
                waiterCalendarDashboard = getWaiterCalendarDashboard(cServeur, dateDash);
                if (!Objects.isNull(waiterCalendarDashboard.getStart())
                        && !Objects.isNull(waiterCalendarDashboard.getTitle())) {
                    waiterCalendarDashboardList.add(waiterCalendarDashboard);
                }
            }
        }

        return waiterCalendarDashboardList;
    }

    // recuperation de l'objet WaiterCalendarDashboard stock la liste de calendrier
    // de serveur par jour
    public WaiterCalendarDashboard getWaiterCalendarDashboard(TPosCalendrierServeur cServeur, LocalDate dateDash) {
        WaiterCalendarDashboard waiterCalendarDashboard = new WaiterCalendarDashboard();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        Integer dayofWeek = dateDash.getDayOfWeek().getValue();
        String strDateofWeek = dateDash.format(formatter);

        if (cServeur.getLundi() == null) {
            cServeur.setLundi(Boolean.FALSE);
        }

        if (cServeur.getMardi() == null) {
            cServeur.setMardi(Boolean.FALSE);
        }

        if (cServeur.getMercredi() == null) {
            cServeur.setMercredi(Boolean.FALSE);
        }

        if (cServeur.getJeudi() == null) {
            cServeur.setJeudi(Boolean.FALSE);
        }

        if (cServeur.getVendredi() == null) {
            cServeur.setVendredi(Boolean.FALSE);
        }

        if (cServeur.getSamedi() == null) {
            cServeur.setSamedi(Boolean.FALSE);
        }

        if (cServeur.getDimanche() == null) {
            cServeur.setDimanche(Boolean.FALSE);
        }

        if (cServeur.getJourAbsenceDebut() == null) {
            switch (dayofWeek) {
            case 1:
                if (cServeur.getLundi()) {
                    waiterCalendarDashboard.setStart(strDateofWeek);
                    waiterCalendarDashboard.setTitle(cServeur.getMmcUserFirstname());
                }
                break;
            case 2:
                if (cServeur.getMardi()) {
                    waiterCalendarDashboard.setStart(strDateofWeek);
                    waiterCalendarDashboard.setTitle(cServeur.getMmcUserFirstname());
                }
                break;
            case 3:
                if (cServeur.getMercredi()) {
                    waiterCalendarDashboard.setStart(strDateofWeek);
                    waiterCalendarDashboard.setTitle(cServeur.getMmcUserFirstname());
                }
                break;
            case 4:
                if (cServeur.getJeudi() == true) {
                    waiterCalendarDashboard.setStart(strDateofWeek);
                    waiterCalendarDashboard.setTitle(cServeur.getMmcUserFirstname());
                }
                break;
            case 5:
                if (cServeur.getVendredi()) {
                    waiterCalendarDashboard.setStart(strDateofWeek);
                    waiterCalendarDashboard.setTitle(cServeur.getMmcUserFirstname());
                }
                break;
            case 6:
                if (cServeur.getSamedi()) {
                    waiterCalendarDashboard.setStart(strDateofWeek);
                    waiterCalendarDashboard.setTitle(cServeur.getMmcUserFirstname());
                }
                break;
            case 7:
                if (cServeur.getDimanche()) {
                    waiterCalendarDashboard.setStart(strDateofWeek);
                    waiterCalendarDashboard.setTitle(cServeur.getMmcUserFirstname());
                }
                break;
            }

        } else {
            if (dateDash.isBefore(cServeur.getJourAbsenceDebut()) == true
                    || dateDash.isAfter(cServeur.getJourAbsenceFin()) == true) {
                switch (dayofWeek) {
                case 1:
                    if (cServeur.getLundi() == true) {
                        waiterCalendarDashboard.setStart(strDateofWeek);
                        waiterCalendarDashboard.setTitle(cServeur.getMmcUserFirstname());
                    }
                    break;
                case 2:
                    if (cServeur.getMardi() == true) {
                        waiterCalendarDashboard.setStart(strDateofWeek);
                        waiterCalendarDashboard.setTitle(cServeur.getMmcUserFirstname());
                    }
                    break;
                case 3:
                    if (cServeur.getMercredi() == true) {
                        waiterCalendarDashboard.setStart(strDateofWeek);
                        waiterCalendarDashboard.setTitle(cServeur.getMmcUserFirstname());
                    }
                    break;
                case 4:
                    if (cServeur.getJeudi() == true) {
                        waiterCalendarDashboard.setStart(strDateofWeek);
                        waiterCalendarDashboard.setTitle(cServeur.getMmcUserFirstname());
                    }
                    break;
                case 5:
                    if (cServeur.getVendredi() == true) {
                        waiterCalendarDashboard.setStart(strDateofWeek);
                        waiterCalendarDashboard.setTitle(cServeur.getMmcUserFirstname());
                    }
                    break;
                case 6:
                    if (cServeur.getSamedi() == true) {
                        waiterCalendarDashboard.setStart(strDateofWeek);
                        waiterCalendarDashboard.setTitle(cServeur.getMmcUserFirstname());
                    }
                    break;
                case 7:
                    if (cServeur.getDimanche() == true) {
                        waiterCalendarDashboard.setStart(strDateofWeek);
                        waiterCalendarDashboard.setTitle(cServeur.getMmcUserFirstname());
                    }
                    break;
                }

            }
        }
        return waiterCalendarDashboard;
    }

    // recuperation de la liste des dates
    public List<LocalDate> getLastMonth(int nbOfMonth) {
        List<LocalDate> dateList = new ArrayList<LocalDate>();
        TMmcParametrage settingData = entityManager.find(TMmcParametrage.class, "DATE_LOGICIELLE");
        LocalDate dateLogicielle = LocalDate.parse(settingData.getValeur());
        LocalDate lastDate = dateLogicielle.plusMonths(nbOfMonth);
        YearMonth lastYearMonth = YearMonth.of(lastDate.getYear(), lastDate.getMonthValue());
        lastDate = lastYearMonth.atEndOfMonth();
        LocalDate dateCompte = dateLogicielle;
        lastDate = lastDate.plusDays(1);

        while (dateCompte.isBefore(lastDate)) {
            dateList.add(dateCompte);
            dateCompte = dateCompte.plusDays(1);
        }

        return dateList;
    }

}
