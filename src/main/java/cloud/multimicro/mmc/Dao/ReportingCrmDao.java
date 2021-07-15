package cloud.multimicro.mmc.Dao;

import cloud.multimicro.mmc.Entity.VComEditionDetailCompte;
import cloud.multimicro.mmc.Entity.VComEditionSoldeCompte;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import javax.ejb.Stateless;
import javax.json.Json;
import javax.json.JsonArray;
import javax.json.JsonObject;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import org.jboss.logging.Logger;

/**
 *
 * @author Tsiory
 */
@Stateless
public class ReportingCrmDao {
        @PersistenceContext
        EntityManager entityManager;
        private static final Logger LOGGER = Logger.getLogger(ReportingCrmDao.class);

        // COMMERCIALE
        public List<VComEditionSoldeCompte> getAllSoldeCompte(String dateStart, String dateEnd, String sold,
                        String consomme) {

                var exist = false;
                StringBuilder stringBuilder = new StringBuilder();
                stringBuilder.append("FROM VComEditionSoldeCompte  ");
                if (!Objects.isNull(dateStart)) {
                        stringBuilder.append(" WHERE DATE(dateArrivee) >= '" + dateStart + "'");
                        exist = true;
                }
                if (!Objects.isNull(dateEnd)) {
                        if (exist == true) {
                                stringBuilder.append(" AND DATE(dateDepart) <= '" + dateEnd + "'");
                        } else {
                                stringBuilder.append(" WHERE DATE(dateDepart) <= '" + dateEnd + "'");
                        }
                }

                if (!Objects.isNull(sold)) {
                        sold = (sold.equals("SOLDEE")) ? "etat = 'SOLDE'" : "etat <> 'SOLDE' ";
                        if (exist == true) {
                                stringBuilder.append(" AND " + sold + "");
                        } else {
                                stringBuilder.append(" WHERE " + sold + "");
                        }
                }

                if (!Objects.isNull(consomme)) {
                        consomme = (consomme.equals("CONSOMMATION")) ? " consomme = 1 "
                                        : " (consomme is null OR consomme = 0)";
                        if (exist == true) {
                                stringBuilder.append(" AND " + consomme + "");
                        } else {
                                stringBuilder.append(" WHERE " + consomme + "");
                        }
                }

                LOGGER.info(" stringBuilder.toString() " + stringBuilder.toString());
                List<VComEditionSoldeCompte> postings = entityManager.createQuery(stringBuilder.toString())
                                .getResultList();
                List<VComEditionSoldeCompte> results = new ArrayList<>();

                if (postings.size() > 0) {
                        results = getEditionSoldeCompteGroupinByClientAccoumpt(postings);
                }
                return results;

        }

        public List<VComEditionSoldeCompte> getEditionSoldeCompteGroupinByClientAccoumpt(
                        List<VComEditionSoldeCompte> postings) {
                List<VComEditionSoldeCompte> result = new ArrayList<>();
                Integer idInitial = postings.get(0).getId();
                BigDecimal soldeFact = new BigDecimal(0);
                BigDecimal soldeArrh = new BigDecimal(0);
                BigDecimal solde = new BigDecimal(0);
                String clientAccoumpteInitial = "";
                String etat = "";
                Integer consomme = 0;

                for (var i = 0; i < postings.size(); i++) {
                        if (postings.get(i).getId() == idInitial) {
                                clientAccoumpteInitial = postings.get(i).getCompteClient();
                                soldeFact = soldeFact.add(postings.get(i).getSoldeFact());
                                soldeArrh = soldeArrh.add(postings.get(i).getSoldeArrh());
                                solde = solde.add(postings.get(i).getSolde());
                                etat = postings.get(i).getEtat();
                                consomme = postings.get(i).getConsomme();
                        } else {
                                VComEditionSoldeCompte comEditionSoldeCompte = new VComEditionSoldeCompte(idInitial,
                                                clientAccoumpteInitial, soldeFact, soldeArrh, solde, etat, consomme);
                                result.add(comEditionSoldeCompte);
                                idInitial = postings.get(i).getId();
                                clientAccoumpteInitial = postings.get(i).getCompteClient();
                                soldeFact = postings.get(i).getSoldeFact();
                                soldeArrh = postings.get(i).getSoldeArrh();
                                solde = postings.get(i).getSolde();
                                etat = postings.get(i).getEtat();
                                consomme = postings.get(i).getConsomme();
                        }
                }

                VComEditionSoldeCompte comEditionSoldeCompte = new VComEditionSoldeCompte(idInitial,
                                clientAccoumpteInitial, soldeFact, soldeArrh, solde, etat, consomme);
                result.add(comEditionSoldeCompte);

                return result;
        }

        // COMMERCIALE
        public JsonObject getAllDetailDesComptes(String dateStart, String dateEnd) {
                var exist = false;
                StringBuilder stringBuilder = new StringBuilder();
                stringBuilder.append("FROM VComEditionDetailCompte  ");
                if (!Objects.isNull(dateStart)) {
                        stringBuilder.append(" WHERE DATE(saisie) >= '" + dateStart + "'");
                        exist = true;
                }
                if (!Objects.isNull(dateEnd)) {
                        if (exist == true) {
                                stringBuilder.append(" AND DATE(saisie) <= '" + dateEnd + "'");
                        } else {
                                stringBuilder.append(" WHERE DATE(saisie) <= '" + dateEnd + "'");
                        }
                }

                stringBuilder.append(" ORDER BY id ");

                List<VComEditionDetailCompte> postings = entityManager.createQuery(stringBuilder.toString())
                                .getResultList();

                var res = Json.createObjectBuilder().build();
                if (postings.size() > 0) {
                        res = getEditionDetailDesComptesGroupinByClientAccoumpt(postings);
                }
                return res;
        }

        public JsonObject createJsonObjectDetailDesComptes(String typeLigneValue, LocalDate saisieValue,
                        String nomValue, BigDecimal montantValue, String reglementValue, String infoValue,
                        String etatValue) {

                return Json.createObjectBuilder().add("typeLigne", typeLigneValue).add("saisie", saisieValue.toString())
                                .add("nom", nomValue).add("montant", montantValue).add("reglement", reglementValue)
                                .add("infos", infoValue).add("etat", etatValue).build();

        }

        public JsonObject getEditionDetailDesComptesGroupinByClientAccoumpt(List<VComEditionDetailCompte> postings) {
                Integer mmcClientId = postings.get(0).getMmcClientId();
                BigDecimal montant = new BigDecimal(0);
                LocalDate dateSaisie = null;
                String nomClient = "";
                String reglement = "";
                String infos = "";
                String etat = "";
                String type = "";
                String compte = "";
                var jsonArray = Json.createArrayBuilder();
                var results = Json.createArrayBuilder();
                var jsonObject = Json.createObjectBuilder().build();
                BigDecimal totalCompte = new BigDecimal(0);
                BigDecimal totalGeneral = new BigDecimal(0);

                for (var i = 0; i < postings.size(); i++) {
                        if (postings.get(i).getMmcClientId() == mmcClientId) {
                                nomClient = postings.get(i).getNom();
                                montant = postings.get(i).getMontant();
                                totalCompte = totalCompte.add(montant);
                                dateSaisie = postings.get(i).getSaisie();
                                nomClient = postings.get(i).getNom();
                                reglement = postings.get(i).getReglement();
                                infos = postings.get(i).getInfos();
                                etat = postings.get(i).getEtat();
                                type = postings.get(i).getTypeLigne();
                                compte = postings.get(i).getCompte();
                                jsonObject = createJsonObjectDetailDesComptes(type, dateSaisie, nomClient, montant,
                                                reglement, infos, etat);
                                jsonArray.add(jsonObject);

                        } else {
                                jsonObject = Json.createObjectBuilder().add("compte", compte)
                                                .add("totalCompte", totalCompte).add("reglements", jsonArray.build())
                                                .build();
                                results.add(jsonObject);

                                nomClient = postings.get(i).getNom();
                                montant = postings.get(i).getMontant();
                                totalCompte = montant;
                                dateSaisie = postings.get(i).getSaisie();
                                nomClient = postings.get(i).getNom();
                                reglement = postings.get(i).getReglement();
                                infos = postings.get(i).getInfos();
                                etat = postings.get(i).getEtat();
                                type = postings.get(i).getTypeLigne();
                                compte = postings.get(i).getCompte();
                                jsonObject = createJsonObjectDetailDesComptes(type, dateSaisie, nomClient, montant,
                                                reglement, infos, etat);
                                jsonArray = Json.createArrayBuilder();
                                jsonArray.add(jsonObject);
                                mmcClientId = postings.get(i).getMmcClientId();

                        }
                        totalGeneral = totalGeneral.add(montant);
                }

                jsonObject = Json.createObjectBuilder().add("compte", compte).add("totalCompte", totalCompte)
                                .add("reglements", jsonArray.build()).build();

                results.add(jsonObject);

                jsonObject = Json.createObjectBuilder().add("totalGeneral", totalGeneral)
                                .add("totalCompte", results.build()).add("reglements", jsonArray.build()).build();

                return jsonObject;
        }

}