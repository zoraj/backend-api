package cloud.multimicro.mmc.Dao;

import java.util.List;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import cloud.multimicro.mmc.Entity.VCollectiviteDashboard;
import cloud.multimicro.mmc.Entity.VCollectiviteDashboardGrapheCaMensuel;
import cloud.multimicro.mmc.Entity.VPmsDashboard;
import cloud.multimicro.mmc.Entity.VPmsDashboardCaDetail;
import cloud.multimicro.mmc.Entity.VPmsDashboardGrapheCaMensuel;
import cloud.multimicro.mmc.Entity.VPmsDashboardGrapheNombreArrivee;
import cloud.multimicro.mmc.Entity.VPosDashboard;
import cloud.multimicro.mmc.Entity.VPosDashboardCaDetail;
import cloud.multimicro.mmc.Entity.VPosDashboardGrapheCaMensuel;
import cloud.multimicro.mmc.Entity.VStatistiqueDashboard;
import java.math.BigInteger;
import java.time.LocalDate;
import javax.json.Json;
import javax.json.JsonObject;

@Stateless
@SuppressWarnings("unchecked")
public class DashboardDao {
    @PersistenceContext
    EntityManager entityManager;

    // GET DASHBOARD
    public VPmsDashboard getDashBoard() {
        VPmsDashboard dashboard = (VPmsDashboard) entityManager.createQuery("FROM VPmsDashboard").getSingleResult();
        return dashboard;
    }

    // GET GRAPHE NOMBRE ARRIVEE
    public List<VPmsDashboardGrapheNombreArrivee> getDashBoardGraphNombreArrivee() {
        List<VPmsDashboardGrapheNombreArrivee> dashboardList = entityManager
                .createQuery("FROM VPmsDashboardGrapheNombreArrivee").getResultList();
        return dashboardList;
    }

    // GET GRAPHE NOMBRE ARRIVEE
    public List<VPmsDashboardGrapheCaMensuel> getDashBoardGraphCaMensuel() {
        List<VPmsDashboardGrapheCaMensuel> dashboardList = entityManager
                .createQuery("FROM VPmsDashboardGrapheCaMensuel").getResultList();
        return dashboardList;
    }

    // GET DASHBOARD CA DETAIL
    public VPmsDashboardCaDetail getDashBoardCaDetail() {
        VPmsDashboardCaDetail dashboard = (VPmsDashboardCaDetail) entityManager
                .createQuery("FROM VPmsDashboardCaDetail").getSingleResult();
        return dashboard;
    }

    // GET DASHBOARD
    public VPosDashboard getPosDashBoard() {
        VPosDashboard dashboard = (VPosDashboard) entityManager.createQuery("FROM VPosDashboard").getSingleResult();
        return dashboard;
    }

    // GET GRAPHE NOMBRE ARRIVEE
    public List<VPosDashboardGrapheCaMensuel> getPosDashBoardGraphCaMensuel() {
        List<VPosDashboardGrapheCaMensuel> dashboardList = entityManager
                .createQuery("FROM VPosDashboardGrapheCaMensuel").getResultList();
        return dashboardList;
    }

    // GET DASHBOARD CA DETAIL
    public VPosDashboardCaDetail getPosDashBoardCaDetail() {
        VPosDashboardCaDetail dashboard = (VPosDashboardCaDetail) entityManager
                .createQuery("FROM VPosDashboardCaDetail").getSingleResult();
        return dashboard;
    }

    // GET DASHBOARD
    public VStatistiqueDashboard getStatDashBoard() {
        VStatistiqueDashboard dashboard = (VStatistiqueDashboard) entityManager
                .createQuery("FROM VStatistiqueDashboard").getSingleResult();
        return dashboard;
    }

    // GET DASHBOARD
    public VCollectiviteDashboard getCollectiviteDashBoard() {
        VCollectiviteDashboard dashboard = (VCollectiviteDashboard) entityManager
                .createQuery("FROM VCollectiviteDashboard").getSingleResult();
        return dashboard;
    }

    // GET GRAPHE NOMBRE ARRIVEE
    public List<VCollectiviteDashboardGrapheCaMensuel> getCollectiviteDashBoardGraphCaMensuel() {
        List<VCollectiviteDashboardGrapheCaMensuel> dashboardList = entityManager
                .createQuery("FROM VCollectiviteDashboardGrapheCaMensuel").getResultList();
        return dashboardList;
    }
    
    public BigInteger countNbArriveeReservation(String dateLogiciel) {
        return (BigInteger) entityManager
                .createNativeQuery("SELECT COUNT(*) FROM t_pms_reservation WHERE date_arrivee >= :dateLogiciel AND day(date_arrivee) = day(:dateLogiciel) AND MONTH(date_arrivee) = MONTH(:dateLogiciel) AND YEAR(date_arrivee) = YEAR(:dateLogiciel)  ")
                .setParameter("dateLogiciel", dateLogiciel).getSingleResult();
    }
    
    public JsonObject getNbArriveeReservation(String dateLogiciel) {       
        JsonObject resultJson = Json.createObjectBuilder()
                .add("JOUR", countNbArriveeReservation(dateLogiciel))
                .build();
        return resultJson;
    }
}
