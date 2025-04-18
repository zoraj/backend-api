package cloud.multimicro.mmc.Dao;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.annotation.Resource;
import javax.ejb.Stateful;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceContextType;
import javax.transaction.Transactional;
import javax.transaction.UserTransaction;

import cloud.multimicro.mmc.Entity.TMmcParametrage;
import cloud.multimicro.mmc.Exception.CustomConstraintViolationException;
import cloud.multimicro.mmc.Exception.DataException;
import cloud.multimicro.mmc.Util.Util;
import static cloud.multimicro.mmc.Util.Util.generateRandomUUID;
import java.io.StringReader;
import java.time.LocalDateTime;
import javax.json.Json;
import javax.json.JsonObject;
import javax.json.JsonReader;

import javax.validation.ConstraintViolationException;
import javax.ws.rs.ForbiddenException;
import javax.ws.rs.NotFoundException;

import org.jboss.logging.Logger;

/**
 * SettingDao
 */
@Stateless
public class SettingDao {
    @PersistenceContext
    EntityManager entityManager;
    
    @PersistenceContext(unitName = "cloud.multimicro_Establishement_PU")
    private EntityManager entityManagerEstablishement;
    
    private static final Logger LOGGER = Logger.getLogger(SettingDao.class);
    private void init() {
        try {
            Date date = Calendar.getInstance().getTime();
            DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

            Date nextClosureDate = new Date();
            Calendar c = Calendar.getInstance();
            c.setTime(date);
            c.add(Calendar.DATE, 1);
            nextClosureDate = c.getTime();
            String strDate = dateFormat.format(date);
            String strNextClosureDate = dateFormat.format(nextClosureDate);

            TMmcParametrage settingDateLog = new TMmcParametrage();
            settingDateLog.setCle("DATE_LOGICIELLE");
            settingDateLog.setValeur(strDate);
            entityManager.persist(settingDateLog);

            TMmcParametrage settingPersonaliseCurr = new TMmcParametrage();
            settingPersonaliseCurr.setCle("PERSONALISED_CURRENCY_RATE");
            settingPersonaliseCurr.setValeur("0");
            entityManager.persist(settingPersonaliseCurr);

            TMmcParametrage settingMultiCurrency = new TMmcParametrage();
            settingMultiCurrency.setCle("MULTI_CURRENCY");
            settingMultiCurrency.setValeur("0");
            entityManager.persist(settingMultiCurrency);

            TMmcParametrage settingDefaultCurrency = new TMmcParametrage();
            settingDefaultCurrency.setCle("DEFAULT_CURRENCY");
            settingDefaultCurrency.setValeur("EUR");
            entityManager.persist(settingDefaultCurrency);

            TMmcParametrage settingLanguage = new TMmcParametrage();
            settingLanguage.setCle("LANGUAGE");
            settingLanguage.setValeur("fr-fr");
            entityManager.persist(settingLanguage);

            TMmcParametrage settingSalonManagement = new TMmcParametrage();
            settingSalonManagement.setCle("SALON_MANAGEMENT");
            settingSalonManagement.setValeur("0");
            entityManager.persist(settingSalonManagement);

            TMmcParametrage settingValidationCode = new TMmcParametrage();
            settingValidationCode.setCle("VALIDATION_CODE");
            settingValidationCode.setValeur("0");
            entityManager.persist(settingValidationCode);

            TMmcParametrage settingLinkToPms = new TMmcParametrage();
            settingLinkToPms.setCle("LINK_TO_PMS");
            settingLinkToPms.setValeur("0");
            entityManager.persist(settingLinkToPms);

            TMmcParametrage settingLinkToRESAPREAFF = new TMmcParametrage();
            settingLinkToRESAPREAFF.setCle("COLOR_RESA_PREAFF");
            settingLinkToRESAPREAFF.setValeur("1965A7");
            entityManager.persist(settingLinkToRESAPREAFF);

            TMmcParametrage settingLinkToRESAARRIVEE = new TMmcParametrage();
            settingLinkToRESAARRIVEE.setCle("COLOR_RESA_ARRIVEE");
            settingLinkToRESAARRIVEE.setValeur("F79E1B");
            entityManager.persist(settingLinkToRESAARRIVEE);

            TMmcParametrage settingLinkToINDIVNOTE = new TMmcParametrage();
            settingLinkToINDIVNOTE.setCle("COLOR_RECEP_INDIV_NOTE");
            settingLinkToINDIVNOTE.setValeur("c75050");
            entityManager.persist(settingLinkToINDIVNOTE);

            TMmcParametrage settingLinkToGRPNOTE = new TMmcParametrage();
            settingLinkToGRPNOTE.setCle("COLOR_RECEP_GRP_NOTE");
            settingLinkToGRPNOTE.setValeur("c75050");
            entityManager.persist(settingLinkToGRPNOTE);

            TMmcParametrage settingLinkToRECEPSOLDEE = new TMmcParametrage();
            settingLinkToRECEPSOLDEE.setCle("COLOR_RECEP_SOLDEE");
            settingLinkToRECEPSOLDEE.setValeur("E92467");
            entityManager.persist(settingLinkToRECEPSOLDEE);

            TMmcParametrage settingLinkToGRPARRIVEE = new TMmcParametrage();
            settingLinkToGRPARRIVEE.setCle("COLOR_RECEP_GRP_ARRIVEE");
            settingLinkToGRPARRIVEE.setValeur("c75050");
            entityManager.persist(settingLinkToGRPARRIVEE);

            TMmcParametrage settingLinkToOPTION = new TMmcParametrage();
            settingLinkToOPTION.setCle("COLOR_RESA_OPTION");
            settingLinkToOPTION.setValeur("c75050");
            entityManager.persist(settingLinkToOPTION);

            TMmcParametrage settingLinkToAGENCE_SOLDEE = new TMmcParametrage();
            settingLinkToAGENCE_SOLDEE.setCle("COLOR_NOTE_AGENCE_SOLDEE");
            settingLinkToAGENCE_SOLDEE.setValeur("c75050");
            entityManager.persist(settingLinkToAGENCE_SOLDEE);

            TMmcParametrage settingLinkToANNULATION = new TMmcParametrage();
            settingLinkToANNULATION.setCle("COLOR_ANNULATION");
            settingLinkToANNULATION.setValeur("c75050");
            entityManager.persist(settingLinkToANNULATION);

            TMmcParametrage settingLinkToLIBRE = new TMmcParametrage();
            settingLinkToLIBRE.setCle("COLOR_LIBRE");
            settingLinkToLIBRE.setValeur("c75050");
            entityManager.persist(settingLinkToLIBRE);

            TMmcParametrage settingLinkToSERVICE = new TMmcParametrage();
            settingLinkToSERVICE.setCle("COLOR_HORS_SERVICE");
            settingLinkToSERVICE.setValeur("c75050");
            entityManager.persist(settingLinkToSERVICE);

            TMmcParametrage settingLinkToCRITERE = new TMmcParametrage();
            settingLinkToCRITERE.setCle("COLOR_CRITERE");
            settingLinkToCRITERE.setValeur("c75050");
            entityManager.persist(settingLinkToCRITERE);

            TMmcParametrage settingLinkToDEPART = new TMmcParametrage();
            settingLinkToDEPART.setCle("COLOR_NOTE_DEPART");
            settingLinkToDEPART.setValeur("c75050");
            entityManager.persist(settingLinkToDEPART);

            TMmcParametrage settingLinkToPREAFF = new TMmcParametrage();
            settingLinkToPREAFF.setCle("COLOR_GRP_PREAFF");
            settingLinkToPREAFF.setValeur("c75050");
            entityManager.persist(settingLinkToPREAFF);

            TMmcParametrage settingLinkToGRPOPTION = new TMmcParametrage();
            settingLinkToGRPOPTION.setCle("COLOR_GRP_OPTION");
            settingLinkToGRPOPTION.setValeur("c75050");
            entityManager.persist(settingLinkToGRPOPTION);

            // COLLECTIVITE
            TMmcParametrage settingGestionCotisation = new TMmcParametrage();
            settingGestionCotisation.setCle("GESTION_COTISATION");
            settingGestionCotisation.setValeur("0");
            entityManager.persist(settingGestionCotisation);

            TMmcParametrage settingFlagCotisationOne = new TMmcParametrage();
            settingFlagCotisationOne.setCle("LINK_FLAG_COTISATION_1");
            settingFlagCotisationOne.setValeur("0");
            entityManager.persist(settingFlagCotisationOne);

            TMmcParametrage settingValeurPlateau = new TMmcParametrage();
            settingValeurPlateau.setCle("VALEUR_POURCENTAGE_PLATEAU");
            settingValeurPlateau.setValeur("0");
            entityManager.persist(settingValeurPlateau);

            TMmcParametrage settingGestionCotisationComptant = new TMmcParametrage();
            settingGestionCotisationComptant.setCle("COTISATION_COMPTANT");
            settingGestionCotisationComptant.setValeur("0");
            entityManager.persist(settingGestionCotisationComptant);

            TMmcParametrage settingFlagCotisationTwo = new TMmcParametrage();
            settingFlagCotisationTwo.setCle("LINK_FLAG_COTISATION_2");
            settingFlagCotisationTwo.setValeur("0");
            entityManager.persist(settingFlagCotisationTwo);

            TMmcParametrage settingValeurPlateauNumber = new TMmcParametrage();
            settingValeurPlateauNumber.setCle("VALEUR_POURCENTAGE_PLATEAU_NOMBRE");
            settingValeurPlateauNumber.setValeur("");
            entityManager.persist(settingValeurPlateauNumber);

            TMmcParametrage settingValeurConsommationPoint = new TMmcParametrage();
            settingValeurConsommationPoint.setCle("CONSOMMATION_POINT_CODE_COTISATION_NOMBRE");
            settingValeurConsommationPoint.setValeur("");
            entityManager.persist(settingValeurConsommationPoint);

            TMmcParametrage settingValeurManyAdmissionSubvention = new TMmcParametrage();
            settingValeurManyAdmissionSubvention.setCle("GESTION_PLUSIEURS_ADMISSION_SUBVENTION_NOMBRE");
            settingValeurManyAdmissionSubvention.setValeur("");
            entityManager.persist(settingValeurManyAdmissionSubvention);

            TMmcParametrage settingCodeCotisation = new TMmcParametrage();
            settingCodeCotisation.setCle("CODE_COTISATION_FACTURE_EVERYONE");
            settingCodeCotisation.setValeur("0");
            entityManager.persist(settingCodeCotisation);

            TMmcParametrage settingCheckConsommation = new TMmcParametrage();
            settingCheckConsommation.setCle("CONSOMMATION_POINT_CODE_COTISATION");
            settingCheckConsommation.setValeur("0");
            entityManager.persist(settingCheckConsommation);

            TMmcParametrage settingCodeSociete = new TMmcParametrage();
            settingCodeSociete.setCle("CODE_SOCIETE_FROM_BADGE");
            settingCodeSociete.setValeur("0");
            entityManager.persist(settingCodeSociete);

            TMmcParametrage settingPositionCodeSociete = new TMmcParametrage();
            settingPositionCodeSociete.setCle("POSITION_CODE_SOCIETE");
            settingPositionCodeSociete.setValeur("01");
            entityManager.persist(settingPositionCodeSociete);

            TMmcParametrage settingAdmissionSubventionAuto = new TMmcParametrage();
            settingAdmissionSubventionAuto.setCle("ADMISSION_SUBVENTION_AUTO");
            settingAdmissionSubventionAuto.setValeur("0");
            entityManager.persist(settingAdmissionSubventionAuto);

            TMmcParametrage settingCaisseCafeterieSansAdmissionSubvention = new TMmcParametrage();
            settingCaisseCafeterieSansAdmissionSubvention.setCle("CAISSE_CAFETERIE_SANS_ADMISSION_SUBVENTION");
            settingCaisseCafeterieSansAdmissionSubvention.setValeur("0");
            entityManager.persist(settingCaisseCafeterieSansAdmissionSubvention);

            TMmcParametrage settingAffichageAdmissionSubventionAuto = new TMmcParametrage();
            settingAffichageAdmissionSubventionAuto.setCle("DISPLAY_ADMISSION_SUBVENTION_ON_KEYBOARD");
            settingAffichageAdmissionSubventionAuto.setValeur("0");
            entityManager.persist(settingAffichageAdmissionSubventionAuto);

            TMmcParametrage settingMinimumPlateau = new TMmcParametrage();
            settingMinimumPlateau.setCle("GESTION_ADMISSION_MINIMUM_PLATEAU");
            settingMinimumPlateau.setValeur("0");
            entityManager.persist(settingMinimumPlateau);

            TMmcParametrage settingSecondSubvention = new TMmcParametrage();
            settingSecondSubvention.setCle("DEUXIEME_SUBVENTION");
            settingSecondSubvention.setValeur("0");
            entityManager.persist(settingSecondSubvention);

            TMmcParametrage settingManyAdmissioSubvention = new TMmcParametrage();
            settingManyAdmissioSubvention.setCle("GESTION_PLUSIEURS_ADMISSION_SUBVENTION");
            settingManyAdmissioSubvention.setValeur("0");
            entityManager.persist(settingManyAdmissioSubvention);

            TMmcParametrage settingLimitSubvention = new TMmcParametrage();
            settingLimitSubvention.setCle("LIMIT_SUBVENTION");
            settingLimitSubvention.setValeur("0");
            entityManager.persist(settingLimitSubvention);

            TMmcParametrage settingMontantLimitSubvention = new TMmcParametrage();
            settingMontantLimitSubvention.setCle("MONTANT_LIMIT_SUBVENTION");
            settingMontantLimitSubvention.setValeur("");
            entityManager.persist(settingMontantLimitSubvention);

            TMmcParametrage settingSubventionModulaire = new TMmcParametrage();
            settingSubventionModulaire.setCle("SUBVENTION_MODULAIRE");
            settingSubventionModulaire.setValeur("0");
            entityManager.persist(settingSubventionModulaire);

            TMmcParametrage settingSubventionSolide = new TMmcParametrage();
            settingSubventionSolide.setCle("SUBVENTION_SOLIDE");
            settingSubventionSolide.setValeur("0");
            entityManager.persist(settingSubventionSolide);

            TMmcParametrage settingSubventionAdmission = new TMmcParametrage();
            settingSubventionAdmission.setCle("SUBVENTION_ADMISSION");
            settingSubventionAdmission.setValeur("0");
            entityManager.persist(settingSubventionAdmission);

            TMmcParametrage settingSubventionLiquide = new TMmcParametrage();
            settingSubventionLiquide.setCle("SUBVENTION_LIQUIDE");
            settingSubventionLiquide.setValeur("0");
            entityManager.persist(settingSubventionLiquide);

            TMmcParametrage settingSubventionCotisation = new TMmcParametrage();
            settingSubventionCotisation.setCle("SUBVENTION_COTISATION");
            settingSubventionCotisation.setValeur("0");
            entityManager.persist(settingSubventionCotisation);

            TMmcParametrage settingZoneAdditional = new TMmcParametrage();
            settingZoneAdditional.setCle("ZONE_SUPPLEMENTAIRE");
            settingZoneAdditional.setValeur("0");
            entityManager.persist(settingZoneAdditional);

            TMmcParametrage settingActualReservationIndex = new TMmcParametrage();
            settingActualReservationIndex.setCle("ACTUAL_RESERVATION_INDEX");
            settingActualReservationIndex.setValeur("0");
            entityManager.persist(settingActualReservationIndex);

            TMmcParametrage settingValeurNumberBrigade = new TMmcParametrage();
            settingValeurNumberBrigade.setCle("NB_BRIGADE");
            settingValeurNumberBrigade.setValeur("1");
            entityManager.persist(settingValeurNumberBrigade);

            TMmcParametrage settingValeurCurrentBrigade = new TMmcParametrage();
            settingValeurCurrentBrigade.setCle("NUM_CURRENT_BRIGADE");
            settingValeurCurrentBrigade.setValeur("1");
            entityManager.persist(settingValeurCurrentBrigade);

            TMmcParametrage settingInvoiceIndex = new TMmcParametrage();
            settingInvoiceIndex.setCle("INVOICE_INDEX");
            settingInvoiceIndex.setValeur("1");
            entityManager.persist(settingInvoiceIndex);

            TMmcParametrage settingInvoiceSentByEmail = new TMmcParametrage();
            settingInvoiceSentByEmail.setCle("INVOICE_SENT_BY_EMAIL");
            settingInvoiceSentByEmail.setValeur("0");
            entityManager.persist(settingInvoiceSentByEmail);

            // PMS
            TMmcParametrage settingLinkToBACKGROUND = new TMmcParametrage();
            settingLinkToBACKGROUND.setCle("COLOR_BACKGROUND_IMAGE");
            settingLinkToBACKGROUND.setValeur("f96332");
            entityManager.persist(settingLinkToBACKGROUND);

            TMmcParametrage settingNameImage = new TMmcParametrage();
            settingNameImage.setCle("NAME_BACKGROUND_IMAGE");
            settingNameImage.setValeur("");
            entityManager.persist(settingNameImage);

            TMmcParametrage settingCompteClient = new TMmcParametrage();
            settingCompteClient.setCle("BOOKING_CLIENT_ID");
            settingCompteClient.setValeur("1");
            entityManager.persist(settingCompteClient);

            TMmcParametrage settingRateGrid = new TMmcParametrage();
            settingRateGrid.setCle("BOOKING_GRILLE_TARIF");
            settingRateGrid.setValeur("1");
            entityManager.persist(settingRateGrid);
            
            TMmcParametrage settingCashingMode = new TMmcParametrage();
            settingCashingMode.setCle("BOOKING_CASHING_MODE");
            settingCashingMode.setValeur("1");
            entityManager.persist(settingCashingMode);

            TMmcParametrage settingIntegrationJournalVente = new TMmcParametrage();
            settingIntegrationJournalVente.setCle("INTEGRATION_JOURNAL_VENTE");
            settingIntegrationJournalVente.setValeur("");
            entityManager.persist(settingIntegrationJournalVente);

            TMmcParametrage settingIntegrationJournalCaisse = new TMmcParametrage();
            settingIntegrationJournalCaisse.setCle("INTEGRATION_JOURNAL_CAISSE");
            settingIntegrationJournalCaisse.setValeur("");
            entityManager.persist(settingIntegrationJournalCaisse);

            TMmcParametrage settingIntegrationCompteClient = new TMmcParametrage();
            settingIntegrationCompteClient.setCle("INTEGRATION_COMPTE_CLIENT");
            settingIntegrationCompteClient.setValeur("");
            entityManager.persist(settingIntegrationCompteClient);

            TMmcParametrage settingIntegrationType = new TMmcParametrage();
            settingIntegrationType.setCle("INTEGRATION_TYPE");
            settingIntegrationType.setValeur("");
            entityManager.persist(settingIntegrationType);

            TMmcParametrage settingNextClosureDate = new TMmcParametrage();
            settingNextClosureDate.setCle("NEXT_CLOSURE_DATE");
            settingNextClosureDate.setValeur(strNextClosureDate);
            entityManager.persist(settingNextClosureDate);

            TMmcParametrage settingAutomaticRecoching = new TMmcParametrage();
            settingAutomaticRecoching.setCle("AUTOMATIC_RECOUCHING");
            settingAutomaticRecoching.setValeur("0");
            entityManager.persist(settingAutomaticRecoching);
            
            TMmcParametrage settingActiveVae = new TMmcParametrage();
            settingActiveVae.setCle("TAKEAWAY_ACTIVATED");
            settingActiveVae.setValeur("0");
            entityManager.persist(settingActiveVae);
            
            TMmcParametrage settingPmsInvoiceHeader1 = new TMmcParametrage();
            settingPmsInvoiceHeader1.setCle("PMS_INVOICE_HEADER_1");
            settingPmsInvoiceHeader1.setValeur("Adresse: "+ "<br>" +"CP Ville:"+ "<br>" +"Téléphone"+ "<br>" +"Références Internet: ");
            entityManager.persist(settingPmsInvoiceHeader1);
            
            TMmcParametrage settingPmsInvoiceHeader2 = new TMmcParametrage();
            settingPmsInvoiceHeader2.setCle("PMS_INVOICE_HEADER_2");
            settingPmsInvoiceHeader2.setValeur("Références " + "<br> " + "Date " + " <br>" + " N°client");
            entityManager.persist(settingPmsInvoiceHeader2);
            
            TMmcParametrage settingPmsInvoiceHeader3 = new TMmcParametrage();
            settingPmsInvoiceHeader3.setCle("PMS_INVOICE_HEADER_3");
            settingPmsInvoiceHeader3.setValeur("Société et/ou Nom du client "+ "<br>" +" Adresse  "+ "<br>" +" CP Ville");
            entityManager.persist(settingPmsInvoiceHeader3);
            
            TMmcParametrage settingPmsInvoiceFooter1 = new TMmcParametrage();
            settingPmsInvoiceFooter1.setCle("PMS_INVOICE_FOOTER_1");
            settingPmsInvoiceFooter1.setValeur("En votre aimable règlement,"+ "<br>" +"Cordialement,");
            entityManager.persist(settingPmsInvoiceFooter1);
            
            TMmcParametrage settingPmsInvoiceFooter2 = new TMmcParametrage();
            settingPmsInvoiceFooter2.setCle("PMS_INVOICE_FOOTER_2");
            settingPmsInvoiceFooter2.setValeur("Conditions de paiement : paiement à réception de facture, à 30 jours..."+ "<br>" +" Aucun escomptè consenti pour le règlement anticipé"+ "<br>" +
            "Tout incident de paiement est passible d'intérêt de retard.");
            entityManager.persist(settingPmsInvoiceFooter2);
                                   
            TMmcParametrage settingInvoiceFooter3 = new TMmcParametrage();
            settingInvoiceFooter3.setCle("PMS_INVOICE_FOOTER_3");
            settingInvoiceFooter3.setValeur("N° Siret 210.890.764 00015 RCS Monpelier "+ "<br>" +"Code APE 947A-N°TVA intracom FR 77825696764000");
            entityManager.persist(settingInvoiceFooter3);
            
            //MODEL FACTURE POS
            TMmcParametrage settingPosInvoiceHeader1 = new TMmcParametrage();
            settingPosInvoiceHeader1.setCle("POS_INVOICE_HEADER_1");
            settingPosInvoiceHeader1.setValeur("Adresse: "+ "<br>" +"CP Ville:"+ "<br>" +"Téléphone"+ "<br>" +"Références Internet: ");
            entityManager.persist(settingPosInvoiceHeader1);
            
            TMmcParametrage settingPosInvoiceHeader2 = new TMmcParametrage();
            settingPosInvoiceHeader2.setCle("POS_INVOICE_HEADER_2");
            settingPosInvoiceHeader2.setValeur("Références " + "<br> " + "Date " + " <br>" + " N°client");
            entityManager.persist(settingPosInvoiceHeader2);
            
            TMmcParametrage settingPosInvoiceHeader3 = new TMmcParametrage();
            settingPosInvoiceHeader3.setCle("POS_INVOICE_HEADER_3");
            settingPosInvoiceHeader3.setValeur("Société et/ou Nom du client "+ "<br>" +" Adresse  "+ "<br>" +" CP Ville");
            entityManager.persist(settingPosInvoiceHeader3);
            
            TMmcParametrage settingPosInvoiceFooter1 = new TMmcParametrage();
            settingPosInvoiceFooter1.setCle("POS_INVOICE_FOOTER_1");
            settingPosInvoiceFooter1.setValeur("En votre aimable règlement,"+ "<br>" +"Cordialement,");
            entityManager.persist(settingPosInvoiceFooter1);
            
            TMmcParametrage settingPosInvoiceFooter2 = new TMmcParametrage();
            settingPosInvoiceFooter2.setCle("POS_INVOICE_FOOTER_2");
            settingPosInvoiceFooter2.setValeur("Conditions de paiement : paiement à réception de facture, à 30 jours..."+ "<br>" +" Aucun escomptè consenti pour le règlement anticipé"+ "<br>" +
            "Tout incident de paiement est passible d'intérêt de retard.");
            entityManager.persist(settingPosInvoiceFooter2);
                                   
            TMmcParametrage settingPosInvoiceFooter3 = new TMmcParametrage();
            settingPosInvoiceFooter3.setCle("POS_INVOICE_FOOTER_3");
            settingPosInvoiceFooter3.setValeur("N° Siret 210.890.764 00015 RCS Monpelier "+ "<br>" +"Code APE 947A-N°TVA intracom FR 77825696764000");
            entityManager.persist(settingPosInvoiceFooter3);
            
            TMmcParametrage settingBookingMailHeder = new TMmcParametrage();
            settingBookingMailHeder.setCle("BOOKING_MAIL_HEADER");
            settingBookingMailHeder.setValeur("Hotel FNP Demo (Promenade des Anglais 35 - Nice - France)");
            entityManager.persist(settingBookingMailHeder);
            
            TMmcParametrage settingBookingMailDetail = new TMmcParametrage();
            settingBookingMailDetail.setCle("BOOKING_MAIL_DETAIL");
            settingBookingMailDetail.setValeur("Details");
            entityManager.persist(settingBookingMailDetail);
            
            TMmcParametrage settingBookingMailFooter = new TMmcParametrage();
            settingBookingMailFooter.setCle("BOOKING_MAIL_FOOTER");
            settingBookingMailFooter.setValeur("This message may contain confidential information. If you are not the addressee or authorized to receive this message, you must not use, copy, disclose or take any action based on any information herein");
            entityManager.persist(settingBookingMailFooter);
            
            TMmcParametrage settingStripePrivateKey = new TMmcParametrage();
            settingStripePrivateKey.setCle("STRIPE_PRIVATE_KEY");
            settingStripePrivateKey.setValeur("");
            entityManager.persist(settingStripePrivateKey);
            
            TMmcParametrage settingStripePublicKey = new TMmcParametrage();
            settingStripePublicKey.setCle("STRIPE_PUBLIC_KEY");
            settingStripePublicKey.setValeur("");
            entityManager.persist(settingStripePublicKey);
            
            TMmcParametrage settingUser = new TMmcParametrage();
            settingUser.setCle("VAE_USER_ID");
            settingUser.setValeur("1000");
            entityManager.persist(settingUser);

            TMmcParametrage settingActivityId = new TMmcParametrage();
            settingActivityId.setCle("VAE_ACTIVITY_ID");
            settingActivityId.setValeur("1");
            entityManager.persist(settingActivityId);
            
            TMmcParametrage settingCashingModeVae = new TMmcParametrage();
            settingCashingModeVae.setCle("VAE_CASHING_MODE_ID");
            settingCashingModeVae.setValeur("1");
            entityManager.persist(settingCashingModeVae);
            
            TMmcParametrage vaeUuidDeviceKey = new TMmcParametrage();
            vaeUuidDeviceKey.setCle("VAE_UUID_DEVICE");
            vaeUuidDeviceKey.setValeur("");
            entityManager.persist(vaeUuidDeviceKey);
                 
        } catch (Exception e) {
            LOGGER.error("Setting initialization went wrong");
        }
   }

    public List<TMmcParametrage> getAll() {
        return entityManager.createQuery("FROM TMmcParametrage").getResultList();
    }

    public TMmcParametrage getSettingByKey(String key) {
        TMmcParametrage setting = entityManager.find(TMmcParametrage.class, key);
        if (setting == null) {
            if (key.equals("DATE_LOGICIELLE")) {
                init();
                setting = entityManager.find(TMmcParametrage.class, key);
            }
        }
        return setting;
    }
    
     private static JsonObject stringToJsonObject(String jsonString) {
        JsonObject object;
        try ( JsonReader jsonReader = Json.createReader(new StringReader(jsonString))) {
            object = jsonReader.readObject();
        }
        return object;
    }

    public void updateSettingByKey(String key, String valeur, String siteCode)
            throws CustomConstraintViolationException, NotFoundException, ParseException {
        try {
            if (key.equals("NEXT_CLOSURE_DATE")) {
                saveNextClosureDate(valeur, key);
            } else {
                TMmcParametrage setting = getSettingByKey(key);
                setting.setValeur(valeur);
                entityManager.merge(setting);
            }
            
            if (key.equals("TAKEAWAY_ACTIVATED")){
                if(valeur.equals("1")){
                    TMmcParametrage defaultCurrency = entityManager.find(TMmcParametrage.class, "DEFAULT_CURRENCY");
                    TMmcParametrage defaultLanguage = entityManager.find(TMmcParametrage.class, "LANGUAGE");
                    TMmcParametrage vaeUuidDevice   = entityManager.find(TMmcParametrage.class, "VAE_UUID_DEVICE");
                    JsonObject siteCodeObject = stringToJsonObject(siteCode);
                    siteCode = siteCodeObject.getString("siteCode");
                    String name                                = "VAE";
                    String site_code                           = siteCode;
                    String uuid                                = generateRandomUUID();
                    vaeUuidDevice.setValeur(uuid);
                    String currency                            = defaultCurrency.getValeur();
                    String language                            = defaultLanguage.getValeur();
                    int invoice_current_num                    = 1;
                    String invoice_prefix                      = "VAE";  
                    entityManagerEstablishement.createNativeQuery("INSERT INTO t_device(uuid, site_code, name, currency, language, invoice_current_num, invoice_prefix) VALUES('"+uuid+"','"+site_code+"','"+name+"','"+currency+"','"+language+"','"+invoice_current_num+"','"+invoice_prefix+"')").executeUpdate();                                     
                    
                }

            }
        } catch (ConstraintViolationException ex) {
            throw new CustomConstraintViolationException(ex);
        }
    }

    public void saveNextClosureDate(String valeur, String key) throws ParseException, NotFoundException {
        TMmcParametrage dateLogicielleparameter = (TMmcParametrage) entityManager.find(TMmcParametrage.class,
                "DATE_LOGICIELLE");
        final SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        Date dateLogicielle = format.parse(dateLogicielleparameter.getValeur());
        Date nextClosureDate = format.parse(valeur);
        if (nextClosureDate.after(dateLogicielle)) {
            TMmcParametrage setting = getSettingByKey(key);
            setting.setValeur(valeur);
            entityManager.merge(setting);
        } else {
            throw new NotFoundException();
        }
    }
}
