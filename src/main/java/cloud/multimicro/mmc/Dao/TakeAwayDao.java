/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cloud.multimicro.mmc.Dao;
import cloud.multimicro.mmc.Entity.TMmcParametrage;
import cloud.multimicro.mmc.Entity.TPosAccompagnement;
import cloud.multimicro.mmc.Entity.TPosCuisson;
import cloud.multimicro.mmc.Entity.TPosEncaissement;
import cloud.multimicro.mmc.Entity.TPosNoteDetail;
import cloud.multimicro.mmc.Entity.TPosNoteDetailCommande;
import cloud.multimicro.mmc.Entity.TPosNoteEntete;
import cloud.multimicro.mmc.Entity.TPosPrestation;
import cloud.multimicro.mmc.Entity.TPosPrestationGroupe;
import cloud.multimicro.mmc.Entity.TPosReservation;
import cloud.multimicro.mmc.Exception.CustomConstraintViolationException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import javax.ejb.Stateless;
import javax.json.JsonArray;
import javax.json.JsonObject;
import javax.json.bind.Jsonb;
import javax.json.bind.JsonbBuilder;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.validation.ConstraintViolationException;

/**
 *
 * @author herizo
 */
@Stateless
@SuppressWarnings("unchecked")
public class TakeAwayDao {
    @PersistenceContext
    private EntityManager entityManager;
    @PersistenceContext(unitName = "cloud.multimicro_Establishement_PU")
    private EntityManager entityManagerEstablishement;
    
    
    public List<TPosPrestation> getPosProduct() {
        List<TPosPrestation> products = entityManager
                .createQuery("FROM TPosPrestation  WHERE autoriseVAE = 1 and dateDeletion is null ORDER BY libelle")
                .getResultList();
        return products;

    }

    
       public List<TPosPrestationGroupe> getProductGroup() {
        List<Object[]> productGroups = entityManager.createQuery(" SELECT "
                            +" groupe.id AS id, " 
                            +" groupe.code AS code, "
                            +" groupe.libelle AS libelle, "
                            +" groupe.iconeImg AS iconeImg "
                            +" FROM TPosPrestation pres LEFT JOIN TPosPrestationGroupe groupe ON (groupe.id = pres.posPrestationGroupeId)"
                            +" WHERE pres.autoriseVAE = 1 GROUP BY groupe.id ").getResultList();
        
        List<TPosPrestationGroupe> result = new ArrayList<TPosPrestationGroupe>();
        for (Object[] productGroup : productGroups) {
            if (productGroup.length > 1) {
                TPosPrestationGroupe groupe = new TPosPrestationGroupe();
                groupe.setId((Integer) productGroup[0]);
                groupe.setCode((String) productGroup[1]);
                groupe.setLibelle((String) productGroup[2]);
                 groupe.setIconeImg((String) productGroup[3]);
                result.add(groupe);
            }
        }
        
        return result;
    }
       
    // Cuisson
    public List<TPosCuisson> getCuisson() {
        List<TPosCuisson> cuisson = entityManager
                .createQuery("FROM TPosCuisson cs WHERE cs.dateDeletion = null ORDER BY libelle").getResultList();
        return cuisson;
    }
    
    // Accompagnement
    public List<TPosAccompagnement> getAccompagnement() {
        List<TPosAccompagnement> accompagnement = entityManager
                .createQuery("FROM TPosAccompagnement a WHERE a.dateDeletion = null ORDER BY libelle").getResultList();
        return accompagnement;
    }
    
    public void addCommandeTakeAway(JsonObject object) throws CustomConstraintViolationException {
        JsonObject jsonObjectNoteEntete = object.getJsonObject("noteEntete");
        JsonObject jsonObjectEncaissement = object.getJsonObject("encaissement");
        JsonArray jsonArrayNotedetail = object.getJsonArray("notedetail");
        JsonArray jsonArray = object.getJsonArray("detailcommandes");
       
        TPosNoteEntete noteEntete = null;
        TPosEncaissement encaissement = null;
        TPosNoteDetail noteDetail = null;
   
        try (Jsonb jsonb = JsonbBuilder.create()) { // Object mapping

            noteEntete = jsonb.fromJson(jsonObjectNoteEntete.toString(), TPosNoteEntete.class);
            //noteDetail = jsonb.fromJson(jsonArrayNotedetail.getJsonObject(i).toString(), TPosNoteDetail.class);
            encaissement = jsonb.fromJson(jsonObjectEncaissement.toString(), TPosEncaissement.class);
       
            try {
                entityManager.persist(noteEntete);  
                encaissement.setPosNoteEnteteId(noteEntete.getId());
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
                LocalTime currentTime = LocalTime.now();
                // Get the current date
                TMmcParametrage settingData = entityManager.find(TMmcParametrage.class, "DATE_LOGICIELLE");
                LocalDate dateLogicielle = LocalDate.parse(settingData.getValeur(), formatter);
                LocalDateTime dateTimeLogicielle = currentTime.atDate(dateLogicielle);
                encaissement.setDateEncaissement(dateTimeLogicielle);
                //GET NUM INVOICE VAE
                String numFact = getInvoiceNumberVAE(noteEntete.getPosteUuid());
                noteEntete.setNumFacture(numFact);
                entityManager.persist(encaissement);
                entityManager.merge(noteEntete);
            } catch (ConstraintViolationException ex) {
                throw new CustomConstraintViolationException(ex);
            }
        }catch(Exception ex){
            throw new CustomConstraintViolationException(ex.getMessage());
        }  
        
        for (int i = 0; i < jsonArrayNotedetail.size(); i++) {
            try (Jsonb jsonb = JsonbBuilder.create()) { 
                TPosNoteDetailCommande detailcommandes = jsonb.fromJson(jsonArray.getJsonObject(i).toString(), TPosNoteDetailCommande.class);
                noteDetail = jsonb.fromJson(jsonArrayNotedetail.getJsonObject(i).toString(), TPosNoteDetail.class);
                noteDetail.setPosNoteEnteteId(noteEntete.getId());
                entityManager.persist(noteDetail);  
                detailcommandes.setPosNoteDetailId(noteDetail.getId());
                entityManager.persist(detailcommandes);
            }catch(Exception ex){
                throw new CustomConstraintViolationException(ex.getMessage());
            }  
        }
        
        /*for (int i = 0; i < jsonArray.size(); i++) {
            try (Jsonb jsonb = JsonbBuilder.create()) { 
                TPosNoteDetailCommande detailcommandes = jsonb.fromJson(jsonArray.getJsonObject(i).toString(), TPosNoteDetailCommande.class);
                detailcommandes.setPosNoteDetailId(noteDetail.getId());
                entityManager.persist(detailcommandes);
            }catch(Exception ex){
                throw new CustomConstraintViolationException(ex.getMessage());
            }  
    
        }*/   
    }
    
    public String getInvoiceNumberVAE(String posteUuid){
        String invoice = entityManagerEstablishement.createNativeQuery("SELECT invoice_current_num FROM t_device WHERE uuid = '"+ posteUuid +"' ")
                .getSingleResult().toString();
        
        String invoicePrefix = entityManagerEstablishement.createNativeQuery("SELECT invoice_prefix FROM t_device WHERE uuid = '"+ posteUuid +"' ")
                .getSingleResult().toString();
        
        String numInvoice = "";
        int invoiceIncrement = Integer.parseInt(invoice) + 1;
        numInvoice = Integer.toString(invoiceIncrement);
        entityManagerEstablishement.createNativeQuery("UPDATE t_device SET invoice_current_num = '"+numInvoice+"' WHERE uuid = '"+ posteUuid +"' ").executeUpdate();
        String numberInvoice = invoicePrefix + String.format("%09d", invoiceIncrement);
        return numberInvoice;
    }
     
     public void setPosReservation(TPosReservation reservation) throws CustomConstraintViolationException {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        LocalTime currentTime = LocalTime.now();

        TMmcParametrage settingData = entityManager.find(TMmcParametrage.class, "DATE_LOGICIELLE");
        LocalDate dateLogicielle = LocalDate.parse(settingData.getValeur(), formatter);
        LocalDateTime dateTimeLogicielle = currentTime.atDate(dateLogicielle);

        try {
            
            if ((reservation.getDateReservation()).isBefore(dateTimeLogicielle)) {
                 throw new CustomConstraintViolationException("Invalid date reservation");
            }
            else {
               entityManager.persist(reservation);
            }
            
        } catch (ConstraintViolationException ex) {
            throw new CustomConstraintViolationException(ex);
        }
    }
    
}
