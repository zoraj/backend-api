/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cloud.multimicro.mmc.Dao;

import cloud.multimicro.mmc.Entity.ApercuIntegrationVente;
import cloud.multimicro.mmc.Entity.TMmcParametrageIntegrationComptable;
import cloud.multimicro.mmc.Entity.TMmcSousFamilleCa;
import cloud.multimicro.mmc.Entity.VPmsIntegrationVente;
import cloud.multimicro.mmc.Exception.CustomConstraintViolationException;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.net.URL;
import java.net.URLConnection;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;

import javax.ejb.Stateless;
import javax.json.Json;
import javax.json.JsonObject;
import javax.json.JsonValue;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.validation.ConstraintViolationException;

/**
 *
 * @author HERIZO RA
 */
@Stateless
@SuppressWarnings("unchecked")
public class IntegrationDao {
    @PersistenceContext
    private EntityManager entityManager;

    public List<ApercuIntegrationVente> setApercuSalesIntegrationsData(String startDate, String endDate) {

        if (Objects.isNull(startDate) || startDate.equals("")) {
            Date start = (Date) entityManager.createQuery("select MIN(dateNote) from TPmsNoteDetail").getSingleResult();
            startDate = start.toString();
        }

        if (Objects.isNull(endDate) || endDate.equals("")) {
            Date end = (Date) entityManager.createQuery("select MAX(dateNote) from TPmsNoteDetail").getSingleResult();
            endDate = end.toString();
        }

        List<Object[]> integrationDataList = entityManager
                .createNativeQuery("CALL p_pms_integration_vente(:startDate,:endDate)")
                .setParameter("startDate", startDate).setParameter("endDate", endDate).getResultList();

        List<ApercuIntegrationVente> result = new ArrayList<ApercuIntegrationVente>();
        for (Object[] n : integrationDataList) {
            if (n.length > 1) {
                if (n[0] != null) {
                    ApercuIntegrationVente pmsIntegrationVente = new ApercuIntegrationVente();
                    java.sql.Date localdate = (java.sql.Date) n[0];
                    pmsIntegrationVente.setDate_note(localdate.toLocalDate());
                    pmsIntegrationVente.setSousfamille((String) n[1]);
                    pmsIntegrationVente.setCompte("");
                    BigDecimal value = (BigDecimal) n[3];
                    if (value.compareTo(BigDecimal.ZERO) > 0) {
                        pmsIntegrationVente.setCredit(value);
                        pmsIntegrationVente.setDebit(BigDecimal.ZERO);
                    } else {
                        pmsIntegrationVente.setCredit(BigDecimal.ZERO);
                        pmsIntegrationVente.setDebit(value.multiply(new BigDecimal("-1.0")));
                    }
                    pmsIntegrationVente.setSolde((BigDecimal) n[3]);
                    result.add(pmsIntegrationVente);
                }
            }
        }
        return result;
    }

    public List<ApercuIntegrationVente> setApercuSalesIntegrationsDataCummul(String startDate, String endDate) {
        if (Objects.isNull(startDate) || startDate.equals("")) {
            Date start = (Date) entityManager.createQuery("select MIN(dateNote) from TPmsNoteDetail").getSingleResult();
            startDate = start.toString();
        }

        if (Objects.isNull(endDate) || endDate.equals("")) {
            Date end = (Date) entityManager.createQuery("select MAX(dateNote) from TPmsNoteDetail").getSingleResult();
            endDate = end.toString();
        }

        List<Object[]> integrationDataList = entityManager
                .createNativeQuery("CALL p_pms_integration_vente_cumul(:startDate,:endDate)")
                .setParameter("startDate", startDate).setParameter("endDate", endDate).getResultList();

        List<ApercuIntegrationVente> result = new ArrayList<ApercuIntegrationVente>();
        for (Object[] n : integrationDataList) {
            if (n.length > 1) {
                if (n[0] != null) {
                    ApercuIntegrationVente pmsIntegrationVente = new ApercuIntegrationVente();
                    pmsIntegrationVente.setSousfamille((String) n[0]);
                    pmsIntegrationVente.setCompte("");
                    BigDecimal value = (BigDecimal) n[2];
                    if (value.compareTo(BigDecimal.ZERO) > 0) {
                        pmsIntegrationVente.setCredit(value);
                        pmsIntegrationVente.setDebit(BigDecimal.ZERO);
                    } else {
                        pmsIntegrationVente.setCredit(BigDecimal.ZERO);
                        pmsIntegrationVente.setDebit(value.multiply(new BigDecimal("-1.0")));
                    }
                    pmsIntegrationVente.setSolde(value);
                    result.add(pmsIntegrationVente);
                }
            }
        }
        return result;
    }

    public List<VPmsIntegrationVente> setSalesIntegrationsData(String startDate, String endDate) {

        if (Objects.isNull(startDate) || startDate.equals("")) {
            Date start = (Date) entityManager.createQuery("select MIN(dateNote) from TPmsNoteDetail").getSingleResult();
            startDate = start.toString();
        }

        if (Objects.isNull(endDate) || endDate.equals("")) {
            Date end = (Date) entityManager.createQuery("select MAX(dateNote) from TPmsNoteDetail").getSingleResult();
            endDate = end.toString();
        }

        List<Object[]> integrationDataList = entityManager
                .createNativeQuery("CALL p_pms_integration_vente(:startDate,:endDate)")
                .setParameter("startDate", startDate).setParameter("endDate", endDate).getResultList();

        List<VPmsIntegrationVente> result = new ArrayList<VPmsIntegrationVente>();
        for (Object[] n : integrationDataList) {
            if (n.length > 1) {
                if (n[0] != null) {
                    VPmsIntegrationVente pmsIntegrationVente = new VPmsIntegrationVente();
                    java.sql.Date localdate = (java.sql.Date) n[0];
                    pmsIntegrationVente.setDate_note(localdate.toLocalDate());
                    pmsIntegrationVente.setSousfamille((String) n[1]);
                    pmsIntegrationVente.setMmc_famille_ca_id((Integer) n[2]);
                    pmsIntegrationVente.setBrut((BigDecimal) n[3]);
                    pmsIntegrationVente.setNet((BigDecimal) n[4]);
                    pmsIntegrationVente.setRemise((BigDecimal) n[5]);
                    pmsIntegrationVente.setCommission((BigDecimal) n[6]);
                    pmsIntegrationVente.setCompte_remise((String) n[7]);
                    pmsIntegrationVente.setCompte_brut((String) n[8]);
                    pmsIntegrationVente.setCompte_commission((String) n[9]);
                    pmsIntegrationVente.setCompte_analytique((String) n[10]);

                    result.add(pmsIntegrationVente);
                }
            }
        }
        return result;
    }

    public List<VPmsIntegrationVente> setSalesIntegrationsDataCummul(String startDate, String endDate) {
        if (Objects.isNull(startDate) || startDate.equals("")) {
            Date start = (Date) entityManager.createQuery("select MIN(dateNote) from TPmsNoteDetail").getSingleResult();
            startDate = start.toString();
        }

        if (Objects.isNull(endDate) || endDate.equals("")) {
            Date end = (Date) entityManager.createQuery("select MAX(dateNote) from TPmsNoteDetail").getSingleResult();
            endDate = end.toString();
        }

        List<Object[]> integrationDataList = entityManager
                .createNativeQuery("CALL p_pms_integration_vente_cumul(:startDate,:endDate)")
                .setParameter("startDate", startDate).setParameter("endDate", endDate).getResultList();

        List<VPmsIntegrationVente> result = new ArrayList<VPmsIntegrationVente>();
        for (Object[] n : integrationDataList) {
            if (n.length > 1) {
                if (n[0] != null) {
                    VPmsIntegrationVente pmsIntegrationVente = new VPmsIntegrationVente();
                    pmsIntegrationVente.setSousfamille((String) n[0]);
                    pmsIntegrationVente.setMmc_famille_ca_id((Integer) n[1]);
                    pmsIntegrationVente.setBrut((BigDecimal) n[2]);
                    pmsIntegrationVente.setNet((BigDecimal) n[3]);
                    pmsIntegrationVente.setRemise((BigDecimal) n[4]);
                    pmsIntegrationVente.setCommission((BigDecimal) n[5]);
                    pmsIntegrationVente.setCompte_remise((String) n[6]);
                    pmsIntegrationVente.setCompte_brut((String) n[7]);
                    pmsIntegrationVente.setCompte_commission((String) n[8]);
                    pmsIntegrationVente.setCompte_analytique((String) n[9]);

                    result.add(pmsIntegrationVente);
                }
            }
        }
        return result;
    }

    // Get all ParametrageIntegrationComptable
    public JsonObject getApercuSalesIntegrationsData(String start, String end) {
        List<ApercuIntegrationVente> integrationVente = setApercuSalesIntegrationsData(start, end);

        if (integrationVente.size() > 0) {
            var dateNote = integrationVente.get(0).getDate_note();
            var debit = new BigDecimal(0);
            var credit = new BigDecimal(0);
            var totalDebit = new BigDecimal(0);
            var totalCredit = new BigDecimal(0);
            var salesArrayBySubFam = Json.createArrayBuilder();
            var resultArray = Json.createArrayBuilder();
            var solde = new BigDecimal(0);
            var totalSolde = new BigDecimal(0);

            for (var i = 0; i < integrationVente.size(); i++) {
                totalDebit = totalDebit.add(integrationVente.get(i).getDebit());
                totalCredit = totalCredit.add(integrationVente.get(i).getCredit());
                var sale = Json.createObjectBuilder().add("date", integrationVente.get(i).getDate_note().toString())
                        .add("libelle", integrationVente.get(i).getSousfamille())
                        .add("compte", integrationVente.get(i).getCompte())
                        .add("debit", integrationVente.get(i).getDebit().toString())
                        .add("credit", integrationVente.get(i).getCredit().toString())
                        .add("solde", integrationVente.get(i).getSolde()).build();

                if (dateNote.compareTo(integrationVente.get(i).getDate_note()) == 0) {
                    debit = debit.add(integrationVente.get(i).getDebit());
                    credit = credit.add(integrationVente.get(i).getCredit());
                    salesArrayBySubFam.add(sale);
                } else {
                    solde = credit.subtract(debit);
                    JsonObject totalObject = Json.createObjectBuilder().add("debit", debit).add("credit", credit)
                            .add("solde", solde).build();
                    var salesArray = Json.createObjectBuilder().add("vente", salesArrayBySubFam.build())
                            .add("total", totalObject).build();
                    resultArray.add((JsonValue) salesArray);
                    dateNote = integrationVente.get(i).getDate_note();
                    salesArrayBySubFam = Json.createArrayBuilder();
                    debit = new BigDecimal(0);
                    credit = new BigDecimal(0);
                    solde = new BigDecimal(0);
                    totalSolde = new BigDecimal(0);
                    debit = debit.add(integrationVente.get(i).getDebit());
                    credit = credit.add(integrationVente.get(i).getCredit());
                    salesArrayBySubFam.add(sale);

                }
            }
            solde = credit.subtract(debit);
            JsonObject totalObject = Json.createObjectBuilder().add("debit", debit).add("credit", credit)
                    .add("solde", solde).build();
            totalSolde = totalCredit.subtract(totalDebit);
            var salesArray = Json.createObjectBuilder().add("vente", salesArrayBySubFam.build())
                    .add("total", totalObject).build();
            resultArray.add((JsonValue) salesArray);

            var salesList = Json.createObjectBuilder().add("liste-vente", resultArray.build())
                    .add("totalDebit", totalDebit).add("totalCredit", totalCredit).add("totalSolde", totalSolde)
                    .build();
            return salesList;
        } else {
            return null;
        }
    }

    public void writeApercuSalesIntegrationsData(String start, String end, FileWriter fileWriter) throws IOException {
        List<ApercuIntegrationVente> integrationVente = setApercuSalesIntegrationsData(start, end);

        if (integrationVente.size() > 0) {
            var dateNote = integrationVente.get(0).getDate_note();
            var debit = new BigDecimal(0);
            var credit = new BigDecimal(0);
            var totalDebit = new BigDecimal(0);
            var totalCredit = new BigDecimal(0);

            for (var i = 0; i < integrationVente.size(); i++) {
                totalDebit = totalDebit.add(integrationVente.get(i).getDebit());
                totalCredit = totalCredit.add(integrationVente.get(i).getCredit());

                var line = integrationVente.get(i).getDate_note().toString() + " ; "
                        + integrationVente.get(i).getSousfamille() + " ; " + integrationVente.get(i).getCompte() + " ; "
                        + integrationVente.get(i).getDebit().toString() + " ; "
                        + integrationVente.get(i).getCredit().toString() + " ; " + integrationVente.get(i).getSolde()
                        + " \n";
                if (dateNote.compareTo(integrationVente.get(i).getDate_note()) == 0) {
                    debit = debit.add(integrationVente.get(i).getDebit());
                    credit = credit.add(integrationVente.get(i).getCredit());
                    fileWriter.write(line);
                } else {
                    fileWriter.write(" ;  ;  ; " + debit.toString() + " ; " + credit.toString() + " ;  \n");
                    dateNote = integrationVente.get(i).getDate_note();
                    debit = new BigDecimal(0);
                    credit = new BigDecimal(0);
                    debit = debit.add(integrationVente.get(i).getDebit());
                    credit = credit.add(integrationVente.get(i).getCredit());
                    fileWriter.write(line);

                }
            }

            fileWriter.write(" ;  ;  ; " + debit.toString() + " ; " + credit.toString() + " ;  \n");
            fileWriter.write(" ;  ;  ; " + totalDebit.toString() + " ; " + totalCredit.toString() + " ;  \n");
            fileWriter.close();
        }
    }

    public File createApercuSalesIntegrationsCumulationData(String start, String end) throws IOException {
        File file = new File("welcome-content/apercuIntegrationVenteCumulation.txt");

        if (file.createNewFile()) {
            FileWriter filewrite = new FileWriter(file);
            writeApercuSalesIntegrationsDataCumulation(start, end, filewrite);
        } else {
            System.out.println("Fichier existe déjà.");
            FileWriter filewrite = new FileWriter(file);
            writeApercuSalesIntegrationsDataCumulation(start, end, filewrite);
        }
        return file;
    }

    public void writeApercuSalesIntegrationsDataCumulation(String start, String end, FileWriter filewrite)
            throws IOException {
        List<ApercuIntegrationVente> integrationVente = setApercuSalesIntegrationsDataCummul(start, end);
        if (integrationVente.size() > 0) {
            var totalDebit = new BigDecimal(0);
            var totalCredit = new BigDecimal(0);

            for (var i = 0; i < integrationVente.size(); i++) {
                totalDebit = totalDebit.add(integrationVente.get(i).getDebit());
                totalCredit = totalCredit.add(integrationVente.get(i).getCredit());
                var line = integrationVente.get(i).getSousfamille() + " ; " + integrationVente.get(i).getCompte()
                        + " ; " + integrationVente.get(i).getDebit().toString() + " ; "
                        + integrationVente.get(i).getCredit().toString() + " ; " + integrationVente.get(i).getSolde()
                        + " \n";

                filewrite.write(line);
            }

            filewrite.write(" ;  ; " + totalDebit.toString() + " ; " + totalCredit.toString() + " ;  \n");
            filewrite.close();
        }
    }

    public File createApercuSalesIntegrationsData(String start, String end) throws IOException {
        File file = new File("welcome-content/apercuIntegrationVente.txt");

        if (file.createNewFile()) {
            FileWriter filewrite = new FileWriter(file);
            writeApercuSalesIntegrationsData(start, end, filewrite);

        } else {
            System.out.println("Fichier existe déjà.");
            FileWriter filewrite = new FileWriter(file);
            writeApercuSalesIntegrationsData(start, end, filewrite);
        }
        return file;

    }

    // Get all ParametrageIntegrationComptable
    public JsonObject getApercuSalesIntegrationsDataCumulation(String start, String end) {
        List<ApercuIntegrationVente> integrationVente = setApercuSalesIntegrationsDataCummul(start, end);
        if (integrationVente.size() > 0) {
            var totalDebit = new BigDecimal(0);
            var totalCredit = new BigDecimal(0);
            var salesArrayBySubFam = Json.createArrayBuilder();
            var resultArray = Json.createArrayBuilder();

            for (var i = 0; i < integrationVente.size(); i++) {
                totalDebit = totalDebit.add(integrationVente.get(i).getDebit());
                totalCredit = totalCredit.add(integrationVente.get(i).getCredit());
                var sale = Json.createObjectBuilder().add("libelle", integrationVente.get(i).getSousfamille())
                        .add("compte", integrationVente.get(i).getCompte())
                        .add("debit", integrationVente.get(i).getDebit().toString())
                        .add("credit", integrationVente.get(i).getCredit().toString())
                        .add("solde", integrationVente.get(i).getSolde()).build();

                salesArrayBySubFam.add(sale);
            }

            var salesArray = Json.createObjectBuilder().add("vente", salesArrayBySubFam.build()).build();
            resultArray.add((JsonValue) salesArray);
            var totalSolde = totalCredit.subtract(totalDebit);
            var salesList = Json.createObjectBuilder().add("liste-vente", resultArray.build())
                    .add("totalDebit", totalDebit).add("totalCredit", totalCredit).add("totalSolde", totalSolde)
                    .build();
            return salesList;
        } else {
            return null;
        }

    }

    // Get all ParametrageIntegrationComptable
    public JsonObject getSalesIntegrationsData(String start, String end) {
        List<VPmsIntegrationVente> integrationVente = setSalesIntegrationsData(start, end);

        if (integrationVente.size() > 0) {
            var family = integrationVente.get(0).getMmc_famille_ca_id();
            var totalNet = new BigDecimal(0);
            var totalBrut = new BigDecimal(0);
            var totalRemise = new BigDecimal(0);
            var totalCommission = new BigDecimal(0);
            var totalNetGeneral = new BigDecimal(0);
            var salesArrayBySubFam = Json.createArrayBuilder();
            var resultArray = Json.createArrayBuilder();

            for (var i = 0; i < integrationVente.size(); i++) {
                totalNetGeneral = totalNetGeneral.add(integrationVente.get(i).getNet());
                var sale = Json.createObjectBuilder().add("date", integrationVente.get(i).getDate_note().toString())
                        .add("libelle", integrationVente.get(i).getSousfamille())
                        .add("brut", integrationVente.get(i).getBrut())
                        .add("remise", integrationVente.get(i).getRemise()).add("net", integrationVente.get(i).getNet())
                        .add("commission", integrationVente.get(i).getCommission())
                        .add("CteBrut",
                                (integrationVente.get(i).getCompte_brut() == null) ? ""
                                        : integrationVente.get(i).getCompte_brut())
                        .add("Cteremise",
                                (integrationVente.get(i).getCompte_remise() == null) ? ""
                                        : integrationVente.get(i).getCompte_remise())
                        .add("CteCommis",
                                (integrationVente.get(i).getCompte_commission() == null) ? ""
                                        : integrationVente.get(i).getCompte_commission())
                        .add("CteArg1/4", "null").add("CteArg13/4", "null")
                        .add("CteAnalytique", (integrationVente.get(i).getCompte_analytique() == null) ? ""
                                : integrationVente.get(i).getCompte_analytique())
                        .build();

                salesArrayBySubFam.add(sale);
                if (family == integrationVente.get(i).getMmc_famille_ca_id()) {
                    totalNet = totalNet.add(integrationVente.get(i).getNet());
                    totalBrut = totalBrut.add(integrationVente.get(i).getBrut());
                    totalRemise = totalRemise.add(integrationVente.get(i).getRemise());
                    totalCommission = totalCommission.add(integrationVente.get(i).getCommission());
                } else {
                    totalNet = totalNet.add(integrationVente.get(i).getNet());
                    totalBrut = totalBrut.add(integrationVente.get(i).getBrut());
                    totalRemise = totalRemise.add(integrationVente.get(i).getRemise());
                    totalCommission = totalCommission.add(integrationVente.get(i).getCommission());
                    JsonObject totalObject = Json.createObjectBuilder().add("totalNet", totalNet)
                            .add("totalBrut", totalBrut).add("totalRemise", totalRemise)
                            .add("totalCommission", totalCommission).build();
                    var salesArray = Json.createObjectBuilder().add("vente", salesArrayBySubFam.build())
                            .add("total", totalObject).build();
                    resultArray.add((JsonValue) salesArray);
                    family = integrationVente.get(i).getMmc_famille_ca_id();
                    salesArrayBySubFam = Json.createArrayBuilder();
                    totalNet = new BigDecimal(0);
                    totalBrut = new BigDecimal(0);
                    totalRemise = new BigDecimal(0);
                    totalCommission = new BigDecimal(0);
                    totalNet = totalNet.add(integrationVente.get(i).getNet());
                    totalBrut = totalBrut.add(integrationVente.get(i).getBrut());
                    totalRemise = totalRemise.add(integrationVente.get(i).getRemise());
                    totalCommission = totalCommission.add(integrationVente.get(i).getCommission());
                    salesArrayBySubFam.add(sale);
                }
            }

            JsonObject totalObject = Json.createObjectBuilder().add("totalNet", totalNet).add("totalBrut", totalBrut)
                    .add("totalRemise", totalRemise).add("totalCommission", totalCommission).build();
            var salesArray = Json.createObjectBuilder().add("vente", salesArrayBySubFam.build())
                    .add("total", totalObject).build();
            resultArray.add((JsonValue) salesArray);

            var salesList = Json.createObjectBuilder().add("liste-vente", resultArray.build())
                    .add("totalNetGeneral", totalNetGeneral).build();
            return salesList;
        } else {
            return null;
        }

    }

    // Get all ParametrageIntegrationComptable
    public JsonObject getSalesIntegrationsDataCumulation(String start, String end) {
        List<VPmsIntegrationVente> integrationVente = setSalesIntegrationsDataCummul(start, end);
        if (integrationVente.size() > 0) {
            var family = integrationVente.get(0).getMmc_famille_ca_id();
            var totalNet = new BigDecimal(0);
            var totalBrut = new BigDecimal(0);
            var totalRemise = new BigDecimal(0);
            var totalCommission = new BigDecimal(0);
            var totalNetGeneral = new BigDecimal(0);
            var salesArrayBySubFam = Json.createArrayBuilder();
            var resultArray = Json.createArrayBuilder();

            for (var i = 0; i < integrationVente.size(); i++) {
                totalNetGeneral = totalNetGeneral.add(integrationVente.get(i).getNet());
                var sale = Json.createObjectBuilder().add("libelle", integrationVente.get(i).getSousfamille())
                        .add("brut", integrationVente.get(i).getBrut())
                        .add("remise", integrationVente.get(i).getRemise()).add("net", integrationVente.get(i).getNet())
                        .add("commission", integrationVente.get(i).getCommission())
                        .add("CteBrut",
                                (integrationVente.get(i).getCompte_brut() == null) ? ""
                                        : integrationVente.get(i).getCompte_brut())
                        .add("Cteremise",
                                (integrationVente.get(i).getCompte_remise() == null) ? ""
                                        : integrationVente.get(i).getCompte_remise())
                        .add("CteCommis",
                                (integrationVente.get(i).getCompte_commission() == null) ? ""
                                        : integrationVente.get(i).getCompte_commission())
                        .add("CteArg1/4", "null").add("CteArg13/4", "null")
                        .add("CteAnalytique", (integrationVente.get(i).getCompte_analytique() == null) ? ""
                                : integrationVente.get(i).getCompte_analytique())
                        .build();

                salesArrayBySubFam.add(sale);
                if (family == integrationVente.get(i).getMmc_famille_ca_id()) {
                    totalNet = totalNet.add(integrationVente.get(i).getNet());
                    totalBrut = totalBrut.add(integrationVente.get(i).getBrut());
                    totalRemise = totalRemise.add(integrationVente.get(i).getRemise());
                    totalCommission = totalCommission.add(integrationVente.get(i).getCommission());
                } else {
                    totalNet = totalNet.add(integrationVente.get(i).getNet());
                    totalBrut = totalBrut.add(integrationVente.get(i).getBrut());
                    totalRemise = totalRemise.add(integrationVente.get(i).getRemise());
                    totalCommission = totalCommission.add(integrationVente.get(i).getCommission());
                    JsonObject totalObject = Json.createObjectBuilder().add("totalNet", totalNet)
                            .add("totalBrut", totalBrut).add("totalRemise", totalRemise)
                            .add("totalCommission", totalCommission).build();
                    var salesArray = Json.createObjectBuilder().add("vente", salesArrayBySubFam.build())
                            .add("total", totalObject).build();
                    resultArray.add((JsonValue) salesArray);
                    family = integrationVente.get(i).getMmc_famille_ca_id();
                    salesArrayBySubFam = Json.createArrayBuilder();
                    totalNet = new BigDecimal(0);
                    totalBrut = new BigDecimal(0);
                    totalRemise = new BigDecimal(0);
                    totalCommission = new BigDecimal(0);
                    totalNet = totalNet.add(integrationVente.get(i).getNet());
                    totalBrut = totalBrut.add(integrationVente.get(i).getBrut());
                    totalRemise = totalRemise.add(integrationVente.get(i).getRemise());
                    totalCommission = totalCommission.add(integrationVente.get(i).getCommission());
                    salesArrayBySubFam.add(sale);
                }
            }

            JsonObject totalObject = Json.createObjectBuilder().add("totalNet", totalNet).add("totalBrut", totalBrut)
                    .add("totalRemise", totalRemise).add("totalCommission", totalCommission).build();
            var salesArray = Json.createObjectBuilder().add("vente", salesArrayBySubFam.build())
                    .add("total", totalObject).build();
            resultArray.add((JsonValue) salesArray);

            var salesList = Json.createObjectBuilder().add("liste-vente", resultArray.build())
                    .add("totalNetGeneral", totalNetGeneral).build();
            return salesList;
        } else {
            return null;
        }

    }

    // Get all ParametrageIntegrationComptable
    public List<TMmcParametrageIntegrationComptable> getAll() {
        List<TMmcParametrageIntegrationComptable> ParametrageIntegrationComptable = entityManager
                .createQuery("FROM TMmcParametrageIntegrationComptable p where p.dateDeletion = null ").getResultList();
        return ParametrageIntegrationComptable;

    }

    /*
     * public List<TMmcParametrageIntegrationComptable> getSubFamily() {
     * List<Object[]> mmcSFamilies =
     * entityManager.createQuery("FROM TMmcParametrageIntegrationComptable head " +
     * "LEFT JOIN TMmcSousFamilleCa c ON  head.mmcSousFamilleCaId = c.id  ")
     * .getResultList(); List<TMmcParametrageIntegrationComptable> result = new
     * ArrayList<TMmcParametrageIntegrationComptable>(); // Retrieve the
     * corresponding sub-family for (Object[] n : mmcSFamilies) { if (n.length > 1)
     * { TMmcParametrageIntegrationComptable h =
     * (TMmcParametrageIntegrationComptable) n[0]; TMmcSousFamilleCa c =
     * (TMmcSousFamilleCa) n[1]; h.setMmcSousFamilleCa(c.getLibelle());
     * result.add(h); } }
     * 
     * return result; }
     */

    // GET ParametrageIntegrationComptable BY ID
    public TMmcParametrageIntegrationComptable getParametrageIntegrationComptableById(int id) {
        TMmcParametrageIntegrationComptable paramIntegration = entityManager
                .find(TMmcParametrageIntegrationComptable.class, id);
        return paramIntegration;
    }

    // SET ParametrageIntegrationComptable
    public void setParametrageIntegrationComptable(TMmcParametrageIntegrationComptable paramIntegration)
            throws CustomConstraintViolationException {
        try {
            entityManager.persist(paramIntegration);
        } catch (ConstraintViolationException ex) {
            throw new CustomConstraintViolationException(ex);
        }
    }

    // UPDATE ParametrageIntegrationComptable
    public TMmcParametrageIntegrationComptable updateParametrageIntegrationComptable(
            TMmcParametrageIntegrationComptable paramIntegration) throws CustomConstraintViolationException {
        try {
            return entityManager.merge(paramIntegration);
        } catch (ConstraintViolationException ex) {
            throw new CustomConstraintViolationException(ex);
        }
    }

    // DELETE ParametrageIntegrationComptable
    public void deleteParametrageIntegrationComptable(int id) throws CustomConstraintViolationException {
        TMmcParametrageIntegrationComptable paramIntegration = getParametrageIntegrationComptableById(id);
        Date dateDel = new Date();
        paramIntegration.setDateDeletion(dateDel);
        try {
            entityManager.merge(paramIntegration);
        } catch (ConstraintViolationException ex) {
            throw new CustomConstraintViolationException(ex);
        }
    }

}
