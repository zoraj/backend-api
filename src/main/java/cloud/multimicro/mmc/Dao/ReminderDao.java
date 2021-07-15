/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cloud.multimicro.mmc.Dao;

import cloud.multimicro.mmc.Entity.TMmcClient;
import cloud.multimicro.mmc.Entity.TMmcParametrage;
import cloud.multimicro.mmc.Entity.TMmcRelance;
import cloud.multimicro.mmc.Entity.TMmcRelanceClient;
//import cloud.multimicro.mmc.Entity.TPmsNoteEntete;
import cloud.multimicro.mmc.Entity.VPmsClientDebiteur;
import cloud.multimicro.mmc.Entity.VPmsRelanceClient;
import cloud.multimicro.mmc.Exception.CustomConstraintViolationException;
import java.math.BigDecimal;
import java.text.ParseException;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import javax.annotation.Resource;
import javax.ejb.Stateless;
import javax.json.JsonObject;
import javax.mail.Address;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.validation.ConstraintViolationException;
import javax.ws.rs.core.MultivaluedMap;
import org.jboss.logging.Logger;

/**
 *
 * @author Tsiory
 */
@Stateless
@SuppressWarnings("unchecked")
public class ReminderDao {

    @PersistenceContext
    EntityManager entityManager;

    @Resource(mappedName = "java:jboss/mail/Default")
    private Session mailSession;
/*
    public TPmsNoteEntete sendReminder(MultivaluedMap<String, String> list) throws ParseException {
        String billNumber = list.getFirst("billNumber");
        Integer level = Integer.parseInt(list.getFirst("level"));
        String email = list.getFirst("email");
        List<TPmsNoteEntete> pmsNoteEntetes = entityManager.createQuery("FROM TPmsNoteEntete WHERE numfacture =:numfacture ")
                .setParameter("numfacture", billNumber)
                .getResultList();
        TPmsNoteEntete pmsNoteEntete = pmsNoteEntetes.get(0);
        List<VPmsClientDebiteur> vpmsClientDebiteurs = entityManager.createQuery("FROM VPmsClientDebiteur WHERE numReservation =:numReservation ")
                .setParameter("numReservation", pmsNoteEntete.getNumeroReservation())
                .getResultList();
        TMmcClient mmcClient = entityManager.find(TMmcClient.class, pmsNoteEntete.getMmcClientId());
        TMmcRelance mmcRelance = entityManager.find(TMmcRelance.class, level);

        BigDecimal soldeDebiteur = new BigDecimal(0.00);
        String numFacture = "";
        String dateFacture = "";
        String montant = "";
        for (VPmsClientDebiteur vpmsClientDebiteur : vpmsClientDebiteurs) {
            numFacture += "<div>" + vpmsClientDebiteur.getNumFacture() + "</div>";
            dateFacture += "<div>" + vpmsClientDebiteur.getDateFacture() + "</div>";
            montant += "<div>" + vpmsClientDebiteur.getMontant() + "</div>";
            soldeDebiteur = soldeDebiteur.add(vpmsClientDebiteur.getMontant());
        }
        String mailContent = mmcRelance.getContenu();
        String object = getObject(mailContent);
        object = object.replace("<p>", "");
        mailContent = mailContent.replace("<p>" + object + "</p>", "");
        mailContent = mailContent.replace("{{_SOLDE_DEBITEUR_}}", soldeDebiteur.toString());
        mailContent = mailContent.replace("{{_NUM_FACTURE_}}", numFacture);
        mailContent = mailContent.replace("{{_DATE_FACTURE_}}", dateFacture);
        mailContent = mailContent.replace("{{_MONTANT_}}", montant);
        object = object.replace("<br>", "");

        if (!Objects.isNull(email)) {
            sendMail(email, mmcClient.getEmail(), mailContent, vpmsClientDebiteurs, object);
            incrementLevel(pmsNoteEntete);
        }
        pmsNoteEntete.setContentReminder(mailContent);
        return pmsNoteEntete;
    }

    public void incrementLevel(TPmsNoteEntete pmsNoteEntete) throws ParseException {
        final java.text.SimpleDateFormat format = new java.text.SimpleDateFormat("yyyy-MM-dd");
        List<TMmcRelanceClient> mmcRelanceClientList = entityManager.createQuery("FROM TMmcRelanceClient WHERE numFacture =:numFacture ")
                .setParameter("numFacture", pmsNoteEntete.getNumfacture())
                .getResultList();

        TMmcParametrage parametrageDateLogicielle = entityManager.find(TMmcParametrage.class, "DATE_LOGICIELLE");
        TMmcRelanceClient mmcRelanceClient = new TMmcRelanceClient();
        Date dateNote = format.parse(parametrageDateLogicielle.getValeur());
        mmcRelanceClient.setDateRerniereRelance(dateNote);
        mmcRelanceClient.setMmcClientId(pmsNoteEntete.getMmcClientId());
        mmcRelanceClient.setNumFacture(pmsNoteEntete.getNumfacture());
        if (mmcRelanceClientList.size() > 0) {
            mmcRelanceClient.setNiveauRelance(mmcRelanceClientList.get(0).getNiveauRelance() + 1);
            mmcRelanceClient.setId(mmcRelanceClientList.get(0).getId());
            entityManager.merge(mmcRelanceClient);
        } else {
            mmcRelanceClient.setNiveauRelance(1);
            entityManager.persist(mmcRelanceClient);
        }
    }

    //envoi mail
    public void sendMail(String sender, String receiver, String mailContent, List<VPmsClientDebiteur> vpmsClientDebiteurs, String object) {
        try {
            Address from = new InternetAddress(sender);
            Address[] to = new InternetAddress[]{new InternetAddress(receiver)};

            MimeMessage mimeMessage = new MimeMessage(mailSession);
            mimeMessage.setFrom(from);
            mimeMessage.setSubject("");
            mimeMessage.setRecipients(Message.RecipientType.TO, to);
            mimeMessage.setSubject(object);
            mimeMessage.setContent(mailContent, "text/html; charset=UTF-8");
            Transport.send(mimeMessage);
        } catch (MessagingException ex) {
            ex.printStackTrace();
        }
    }

    public String getObject(String mailContent) {
        String[] arrOfStr = mailContent.split("</p>");
        String object = "";
        for (int i = 0; i < arrOfStr.length; i++) {
            if (arrOfStr[i].contains("Objet")) {
                object = arrOfStr[i];
                break;
            }
        }
        return object;
    }
*/
    public List<TMmcRelance> getAll() {
        List<TMmcRelance> reminder = entityManager.createQuery("FROM TMmcRelance WHERE dateDeletion = null order by niveau").getResultList();
        return reminder;
    }

    public TMmcRelance getById(int id) {
        TMmcRelance reminder = entityManager.find(TMmcRelance.class, id);
        return reminder;
    }

    public void add(TMmcRelance reminder) throws CustomConstraintViolationException {
        try {
            entityManager.persist(reminder);
        } catch (ConstraintViolationException ex) {
            throw new CustomConstraintViolationException(ex);
        }
    }

    public TMmcRelance update(TMmcRelance reminder) throws CustomConstraintViolationException {
        try {
            return entityManager.merge(reminder);
        } catch (ConstraintViolationException ex) {
            throw new CustomConstraintViolationException(ex);
        }
    }

    public void delete(Integer id) {
        entityManager.createNativeQuery("UPDATE t_mmc_relance SET date_deletion = CURRENT_TIMESTAMP WHERE id=:id")
                .setParameter("id", id)
                .executeUpdate();
    }

    public void addReminderClient(TMmcRelanceClient reminderClient) throws CustomConstraintViolationException {
        try {
            entityManager.persist(reminderClient);
        } catch (ConstraintViolationException ex) {
            throw new CustomConstraintViolationException(ex);
        }
    }

    public List<VPmsRelanceClient> getAllVPmsRelanceClient() {
        List<VPmsRelanceClient> reminder = entityManager.createQuery("FROM VPmsRelanceClient").getResultList();
        return reminder;
    }

}
