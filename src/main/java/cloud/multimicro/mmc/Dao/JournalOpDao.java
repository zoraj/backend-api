/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cloud.multimicro.mmc.Dao;

import cloud.multimicro.mmc.Entity.TMmcJournalOperation;
import cloud.multimicro.mmc.Entity.TPosJournalOperation;
import cloud.multimicro.mmc.Exception.CustomConstraintViolationException;
import java.io.StringReader;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.logging.Level;
import javax.ejb.Stateless;
import javax.json.Json;
import javax.json.JsonObject;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import org.jboss.logging.Logger;

/**
 *
 * @author herizo
 */
@Stateless
public class JournalOpDao {

    @PersistenceContext
    private EntityManager entityManager;

    public List<Integer> stringToList(String value) {
        List<Integer> array = new ArrayList<Integer>();
        List<String> arrayString = new ArrayList<String>();
        String str[] = value.split(",");
        arrayString = Arrays.asList(str);
        for (String s : arrayString) {
            array.add(Integer.parseInt(s));
        }
        return array;
    }

    public List<TMmcJournalOperation> getByCriteria(JsonObject criteria) {
        String query;
        StringBuilder criteriaStr = new StringBuilder();
        String dateDebut = criteria.getString("dateDebut");
        String dateFin = criteria.getString("dateFin");

        StringBuilder detailCriteria = new StringBuilder();

        criteriaStr.append("FROM TMmcJournalOperation WHERE dateCreation  >= '" + dateDebut + "' AND dateCreation  <= '" + dateFin + "'");

        StringBuilder brigadeDetail1 = new StringBuilder();
        brigadeDetail1.append("(JSON_EXTRACT(detail,'$.BRIGADE') =").append(criteria.get("BRIGADE").toString()).append(")");
        System.out.println("brigadeDetail1" + brigadeDetail1);

        detailCriteria.append(brigadeDetail1);

        StringBuilder moduleDetail1 = new StringBuilder();
        moduleDetail1.append("AND (JSON_EXTRACT(detail,'$.MODULE') ='").append(criteria.get("MODULE").toString()).append("')");
        System.out.println("moduleDetail1" + moduleDetail1);

        detailCriteria.append(moduleDetail1);

        StringBuilder roomDetail1 = new StringBuilder();
        final String rooms = criteria.get("CHAMBRE").toString();
        // final String rooms = criteria.get("CHAMBRE").toString();
        if (!"[]".equals(rooms)) {
            List<Integer> modelsIntegers = stringToList(rooms.substring(1, rooms.length() - 1));
            if (modelsIntegers.size() > 0) {

                modelsIntegers.forEach((Integer room) -> {
                    if (roomDetail1.length() > 0) {
                        roomDetail1.append(" OR (JSON_EXTRACT(detail,'$.CHAMBRE') =").append(room).append(")");
                    } else {
                        roomDetail1.append(" AND ((JSON_EXTRACT(detail,'$.CHAMBRE') =").append(room).append(")");
                    }

                });
                roomDetail1.append(")");
            }
        }

        detailCriteria.append(roomDetail1);

        StringBuilder productDetail1 = new StringBuilder();
        String prestas = criteria.get("PRESTATION").toString();
        prestas = prestas.substring(1, prestas.length() - 1);
        if (!"[]".equals(prestas)) {
            List<String> myList = new ArrayList<String>(Arrays.asList(prestas.split(",")));
            if (myList.size() > 0) {
                myList.forEach((String presta) -> {
                    System.out.println(" presta : " + presta);
                    if (productDetail1.length() > 0) {
                        productDetail1.append(" AND (JSON_EXTRACT(detail,'$.PRESTATION') ='").append(presta).append("')");
                    } else {

                        productDetail1.append(" OR (JSON_EXTRACT(detail,'$.PRESTATION')='").append(presta).append("')");
                    }

                });

                detailCriteria.append(productDetail1);
            }
        }

        StringBuilder rglDetail1 = new StringBuilder();
        String modeReglements = criteria.get("MODE").toString();
        modeReglements = modeReglements.substring(1, modeReglements.length() - 1);
        if (!"[]".equals(modeReglements)) {
            List<String> myList = new ArrayList<String>(Arrays.asList(modeReglements.split(",")));
            if (myList.size() > 0) {
                myList.forEach((String modeReglement) -> {
                    System.out.println(" modeReglement : " + modeReglement);
                    if (rglDetail1.length() > 0) {
                        rglDetail1.append(" AND (JSON_EXTRACT(detail,'$.MODE') ='").append(modeReglement).append("')");
                    } else {

                        rglDetail1.append(" OR (JSON_EXTRACT(detail,'$.MODE')='").append(modeReglement).append("')");
                    }

                });

                detailCriteria.append(rglDetail1);
            }
        }

        StringBuilder productDetail = new StringBuilder();
        List<Map> productList = (List) criteria.get("PRESTATIONS");
        if (productList.size() > 0) {
            if (detailCriteria.length() != 0) {
                detailCriteria.append(" OR ");
            }
            detailCriteria.append(" (action='PRESTATIONS'");
        }
        productList.forEach(prod -> {
            if (productDetail.length() != 0) {
                productDetail.append(" AND ");
            }
            productDetail.append("JSON_EXTRACT(detail,'$.PRESTATION_ENCOURS') ='").append(prod.get("PRESTATION_ENCOURS").toString()).append("'");
        });
        detailCriteria.append(" AND ");
        detailCriteria.append(productDetail);
        detailCriteria.append(" )");

        StringBuilder rglDetail = new StringBuilder();
        List<Map> rglList = (List) criteria.get("REGLEMENT");
        if (rglList.size() > 0) {
            if (detailCriteria.length() != 0) {
                detailCriteria.append(" OR ");
            }
            detailCriteria.append(" (action='REGLEMENT'");

        }
        rglList.forEach(rgl -> {
            if (rglDetail.length() != 0) {
                rglDetail.append(" OR ");
            }
            rglDetail.append("JSON_EXTRACT(detail,'$.MODE_REGLEMENT') ='").append(rgl.get("MODE_REGLEMENT").toString()).append("'");

        });

        detailCriteria.append(" AND ");
        detailCriteria.append(rglDetail);
        detailCriteria.append(" )");

        List<Map> ca = (List) criteria.get("CA");
        if (Integer.parseInt(ca.get(0).get("FACTURATION_DIRECTE").toString()) == 1) {
            if (detailCriteria.length() != 0) {
                detailCriteria.append(" AND ");
            }
            detailCriteria.append(" action='FACTURATION_DIRECTE'");

        }
        if (Integer.parseInt(ca.get(1).get("RECOUCHE_AUTO").toString()) == 1) {
            if (detailCriteria.length() != 0) {
                detailCriteria.append(" AND ");
            }
            detailCriteria.append(" action='RECOUCHE_AUTO'");
        }

        if (Integer.parseInt(ca.get(2).get("ENREGISTREMENT").toString()) == 1) {
            if (detailCriteria.length() != 0) {
                detailCriteria.append(" AND ");
            }
            detailCriteria.append(" action='ENREGISTREMENT'");
        }
        if (Integer.parseInt(ca.get(3).get("CHANGEMENT_TARIF").toString()) == 1) {
            if (detailCriteria.length() != 0) {
                detailCriteria.append(" AND ");
            }
            detailCriteria.append(" action='CHANGEMENT_TARIF'");
        }
        if (Integer.parseInt(ca.get(4).get("REPORT_NOTE").toString()) == 1) {
            if (detailCriteria.length() != 0) {
                detailCriteria.append(" AND ");
            }
            detailCriteria.append(" action='REPORT_NOTE'");
        }
        if (Integer.parseInt(ca.get(5).get("REPORT_TPV").toString()) == 1) {
            if (detailCriteria.length() != 0) {
                detailCriteria.append(" AND ");
            }
            detailCriteria.append(" action='REPORT_TPV'");
        }
        if (Integer.parseInt(ca.get(6).get("DEPART_ANTICIPE").toString()) == 1) {
            if (detailCriteria.length() != 0) {
                detailCriteria.append(" AND ");
            }
            detailCriteria.append(" action='DEPART_ANTICIPE'");
        }

        List<Map> divers = (List) criteria.get("DIVERS");
        if (Integer.parseInt(divers.get(0).get("MODIF_PREVISIONNEL").toString()) == 1) {
            if (detailCriteria.length() != 0) {
                detailCriteria.append(" AND ");
            }
            detailCriteria.append(" action='MODIF_PREVISIONNEL'");
        }
        if (Integer.parseInt(divers.get(1).get("DELOGEMENT").toString()) == 1) {
            if (detailCriteria.length() != 0) {
                detailCriteria.append(" AND ");
            }
            detailCriteria.append(" action='DELOGEMENT'");
        }
        if (Integer.parseInt(divers.get(2).get("PURGE").toString()) == 1) {
            if (detailCriteria.length() != 0) {
                detailCriteria.append(" AND ");
            }
            detailCriteria.append(" action='PURGE'");
        }
        if (Integer.parseInt(divers.get(3).get("NETTOYAGE_PLANNING").toString()) == 1) {
            if (detailCriteria.length() != 0) {
                detailCriteria.append(" AND ");
            }
            detailCriteria.append(" action='NETTOYAGE_PLANNING'");
        }
        if (Integer.parseInt(divers.get(4).get("FIN_DE_BRIGADE").toString()) == 1) {
            if (detailCriteria.length() != 0) {
                detailCriteria.append(" AND ");
            }
            detailCriteria.append(" action='FIN_DE_BRIGADE'");
        }
        if (Integer.parseInt(divers.get(5).get("CLOTURE").toString()) == 1) {
            if (detailCriteria.length() != 0) {
                detailCriteria.append(" AND ");
            }
            detailCriteria.append(" action='CLOTURE'");
        }
        if (Integer.parseInt(divers.get(6).get("TRANSFERT_COMPTE_A_COMPTE").toString()) == 1) {
            if (detailCriteria.length() != 0) {
                detailCriteria.append(" AND ");
            }
            detailCriteria.append(" action='TRANSFERT_COMPTE_A_COMPTE'");
        }
        List<Map> ENCAISSEMENT = (List) criteria.get("ENCAISSEMENT");
        if (Integer.parseInt(ENCAISSEMENT.get(0).get("DEPART").toString()) == 1) {
            if (detailCriteria.length() != 0) {
                detailCriteria.append(" AND ");
            }
            detailCriteria.append(" action='DEPART'");
        }
        if (Integer.parseInt(ENCAISSEMENT.get(1).get("ACOMPTES").toString()) == 1) {
            if (detailCriteria.length() != 0) {
                detailCriteria.append(" AND ");
            }
            detailCriteria.append(" action='ACOMPTES'");
        }
        if (Integer.parseInt(ENCAISSEMENT.get(2).get("SAISIE_ARRHES").toString()) == 1) {
            if (detailCriteria.length() != 0) {
                detailCriteria.append(" AND ");
            }
            detailCriteria.append(" action='SAISIE_ARRHES'");
        }
        if (Integer.parseInt(ENCAISSEMENT.get(3).get("CONSO_ARRHES").toString()) == 1) {
            if (detailCriteria.length() != 0) {
                detailCriteria.append(" AND ");
            }
            detailCriteria.append(" action='CONSO_ARRHES'");
        }
        if (Integer.parseInt(ENCAISSEMENT.get(4).get("PURGE_RGLT_EN_ATTENTE").toString()) == 1) {
            if (detailCriteria.length() != 0) {
                detailCriteria.append(" AND ");
            }
            detailCriteria.append(" action='PURGE_RGLT_EN_ATTENTE'");
        }
        if (Integer.parseInt(ENCAISSEMENT.get(5).get("REPRISE_ARRHES").toString()) == 1) {
            if (detailCriteria.length() != 0) {
                detailCriteria.append(" AND ");
            }
            detailCriteria.append(" action='REPRISE_ARRHES'");
        }
        if (Integer.parseInt(ENCAISSEMENT.get(6).get("REPRISE_DE_RGLT").toString()) == 1) {
            if (detailCriteria.length() != 0) {
                detailCriteria.append(" AND ");
            }
            detailCriteria.append(" action='REPRISE_DE_RGLT'");
        }
        if (Integer.parseInt(ENCAISSEMENT.get(7).get("REMBOURSEMENT_ARRHES").toString()) == 1) {
            if (detailCriteria.length() != 0) {
                detailCriteria.append(" AND ");
            }
            detailCriteria.append(" action='REMBOURSEMENT_ARRHES'");
        }

        StringBuilder roomDetail = new StringBuilder();
        List<Map> roomList = (List) criteria.get("CHAMBRES");

        if (roomList.size() > 0) {
            if (detailCriteria.length() != 0) {
                detailCriteria.append(" OR ");
            }
            detailCriteria.append(" (action='CHAMBRES'");
        }
        roomList.forEach(room -> {
            if (roomDetail.length() != 0) {
                roomDetail.append(" OR ");
            }
            roomDetail.append("JSON_EXTRACT(detail,'$.CHB') ='").append(room.get("CHB").toString()).append("'");
        });
        detailCriteria.append(" AND ");
        detailCriteria.append(roomDetail);
        detailCriteria.append(" )");

        if (detailCriteria.length() != 0) {
            detailCriteria.insert(0, " AND (");
            detailCriteria.append(")");
        }

        if (!detailCriteria.toString().isEmpty()) {
            query = criteriaStr.toString().concat(detailCriteria.toString());
        } else {
            query = criteriaStr.toString();
        }
        query = query.replaceAll("\"", "");
        System.out.println("query" + query);

        List<TMmcJournalOperation> journal = entityManager.createQuery(query).getResultList();

        return journal;

    }

    public List<TMmcJournalOperation> getByBrigade(JsonObject criteria1) {
        String query1;
        StringBuilder criteriaStr1 = new StringBuilder();
        String dateDebut = criteria1.getString("dateDebut");
        String dateFin = criteria1.getString("dateFin");

        StringBuilder detailCriteria1 = new StringBuilder();

        criteriaStr1.append("FROM TMmcJournalOperation WHERE dateCreation  >= '" + dateDebut + "' AND dateCreation  <= '" + dateFin + "'");
        StringBuilder brigadeDetail = new StringBuilder();

        brigadeDetail.append("JSON_EXTRACT(detail,'$.BRIGADE') =").append(criteria1.get("BRIGADE").toString()).append("");

        detailCriteria1.append(brigadeDetail);

        if (detailCriteria1.length() != 0) {
            detailCriteria1.insert(0, " AND (");
            detailCriteria1.append(")");
        }

        if (!detailCriteria1.toString().isEmpty()) {
            query1 = criteriaStr1.toString().concat(detailCriteria1.toString());
        } else {
            query1 = criteriaStr1.toString();
        }
        query1 = query1.replaceAll("\"", "");

        List<TMmcJournalOperation> journal1 = entityManager.createQuery(query1).getResultList();

        return journal1;

    }

    public List<TMmcJournalOperation> getByActivity(JsonObject criteria) {

        String query;
        StringBuilder criteriaStr = new StringBuilder();
        String dateDebut = criteria.getString("dateDebut");
        String dateFin = criteria.getString("dateFin");

        StringBuilder detailCriteria = new StringBuilder();

        criteriaStr.append("FROM TMmcJournalOperation WHERE dateCreation  >= '" + dateDebut + "' AND dateCreation  <= '" + dateFin + "'");

        System.out.println("criteriaStr" + criteriaStr);
        StringBuilder rglDetail = new StringBuilder();
        List<Map> rglList = (List) criteria.get("AJOUT_PRESTATION");
        if (rglList.size() > 0) {
            if (detailCriteria.length() != 0) {
                detailCriteria.append(" OR ");
            }
            detailCriteria.append(" AND (action='AJOUT_PRESTATION'");
            System.out.println("rglDetail" + rglDetail);
        }
        rglList.forEach(rgl -> {
            if (rglDetail.length() != 0) {
                rglDetail.append(" OR ");
            }
            rglDetail.append("JSON_EXTRACT(detail,'$.ACTIVITE') ='").append(rgl.get("ACTIVITE").toString()).append("'");

        });

        detailCriteria.append(" AND ");
        detailCriteria.append(rglDetail);
        detailCriteria.append(" )");

        StringBuilder serviceDetail = new StringBuilder();
        serviceDetail.append("AND (JSON_EXTRACT(detail,'$.SERVICE') ='").append(criteria.get("SERVICE").toString()).append("')");
        detailCriteria.append(serviceDetail);
        
        String criteriaPoste = criteria.get("POSTE").toString();    
        
        StringBuilder posteDetail = new StringBuilder();
        if (!"Tous".equals(criteriaPoste.replaceAll("\"", ""))) {
            System.out.println(criteriaPoste);
            posteDetail.append("AND (JSON_EXTRACT(detail,'$.POSTE') ='").append(criteria.get("POSTE").toString()).append("')");
            detailCriteria.append(posteDetail);
        }else {
            posteDetail.append(" OR (JSON_EXTRACT(detail,'$.POSTE') ='Tous')");
            detailCriteria.append(posteDetail);
        }

        if (!detailCriteria.toString().isEmpty()) {
            query = criteriaStr.toString().concat(detailCriteria.toString());
        } else {
            query = criteriaStr.toString();
        }
        query = query.replaceAll("\"", "");
//       query = "FROM TMmcJournalOperation WHERE dateCreation  >= '2020-08-08' AND dateCreation  <= '2020-12-12' AND JSON_EXTRACT(detail,'$.POSTE')";
        System.out.println("query" + query);
        List<TMmcJournalOperation> journal = entityManager.createQuery(query).getResultList();

        return journal;
    }

    public List<TMmcJournalOperation> getAll() {
        List<TMmcJournalOperation> journal = entityManager.createQuery("FROM TMmcJournalOperation  ").getResultList();
        return journal;

    }
    
    public List<TPosJournalOperation> getJournalOperation() {
        List<TMmcJournalOperation> posJournal = entityManager.createQuery("FROM TMmcJournalOperation").getResultList();
        
        List<TPosJournalOperation> resultJournalOp = new ArrayList<TPosJournalOperation>();
        
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        
        for (TMmcJournalOperation journal : posJournal) {     
            TPosJournalOperation journalOp = new TPosJournalOperation();
            String action = journal.getAction();
            String jsonDetail = journal.getDetail();
            
            JsonObject jsonObject = Json.createReader(new StringReader(jsonDetail)).readObject();
            
            String operationDate = jsonObject.getString("DATE");
            LocalDateTime dateOperation = LocalDateTime.parse(operationDate, formatter);

            journalOp.setDateOperation(dateOperation);
            journalOp.setActivity(jsonObject.getString("ACTIVITY"));
            journalOp.setPoste(jsonObject.getString("POSTE"));
            journalOp.setServeur(jsonObject.getString("WAITER"));
            journalOp.setAction(action);
            journalOp.setDetail(jsonDetail);
  
            resultJournalOp.add(journalOp);
        } 
        return resultJournalOp;

    } 
    
}
