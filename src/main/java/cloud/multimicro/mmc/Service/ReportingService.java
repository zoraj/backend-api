/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cloud.multimicro.mmc.Service;

import cloud.multimicro.mmc.Dao.ReportingDao;
import cloud.multimicro.mmc.Entity.EditionEffectifPrevDebours;
import cloud.multimicro.mmc.Entity.EditionEffectifPrevFamTarif;
import cloud.multimicro.mmc.Entity.EditionListePrestationArrivee;
//import cloud.multimicro.mmc.Entity.EditionListeVerifArrivee;
import cloud.multimicro.mmc.Entity.EditionRemplissageAnnuel;
import cloud.multimicro.mmc.Entity.TPmsNoteEntete;
import cloud.multimicro.mmc.Entity.VCollectiviteLectureFamille;
import cloud.multimicro.mmc.Entity.VCollectiviteValorisationBac;
import cloud.multimicro.mmc.Entity.VComEditionAgeeArrhe;
import cloud.multimicro.mmc.Entity.VComEditionAgeeFacture;
import cloud.multimicro.mmc.Entity.VComEditionDetailCompte;
import cloud.multimicro.mmc.Entity.VComEditionExtraitCompte;
import cloud.multimicro.mmc.Entity.VComEditionFacture;
import cloud.multimicro.mmc.Entity.VComEditionReleveDetaileFacture;
import cloud.multimicro.mmc.Entity.VComEditionReleveSimpleFacture;
import cloud.multimicro.mmc.Entity.VComEditionSituationDebiteur;
import cloud.multimicro.mmc.Entity.VComEditionSoldeCompte;
import cloud.multimicro.mmc.Entity.VPmsEditionArriveePrevue;
import cloud.multimicro.mmc.Entity.VPmsEditionBalanceAppartement;
import cloud.multimicro.mmc.Entity.VPmsEditionClientPresent;
import cloud.multimicro.mmc.Entity.VPmsEditionClotureDefinitive;
import cloud.multimicro.mmc.Entity.VPmsEditionClotureProvisoire;
import cloud.multimicro.mmc.Entity.VPmsEditionDepartPrevu;
import cloud.multimicro.mmc.Entity.VPmsEditionEtatControl;
import cloud.multimicro.mmc.Entity.VPmsEditionEtatDebit;
import cloud.multimicro.mmc.Entity.VPmsEditionEtatDebitChambre;
import cloud.multimicro.mmc.Entity.VPmsEditionEtatPrevRealMois;
import cloud.multimicro.mmc.Entity.VPmsEditionEtatRemplissage;
import cloud.multimicro.mmc.Entity.VPmsEditionListeArrivee;
import cloud.multimicro.mmc.Entity.VPmsEditionListeGlobalArrivee;
import cloud.multimicro.mmc.Entity.VPmsEditionPrestationVendue;
import cloud.multimicro.mmc.Entity.VPmsEditionRapportArriveeDepart;
import cloud.multimicro.mmc.Entity.VPmsEditionRapportEtage;
import cloud.multimicro.mmc.Entity.VPmsEditionOccupation;
import cloud.multimicro.mmc.Entity.VPmsEditionListeOccupation;
import cloud.multimicro.mmc.Entity.VPmsEditionListeOccupationReel;
import cloud.multimicro.mmc.Entity.VPmsEditionListeOccupationReelRecep;
import cloud.multimicro.mmc.Entity.VPmsEditionListeOccupationResa;
import cloud.multimicro.mmc.Entity.VPmsEditionListeOption;
import cloud.multimicro.mmc.Entity.VPmsEditionListePrestationArrivee;
import cloud.multimicro.mmc.Entity.VPmsEditionListeVerifArrivee;
import cloud.multimicro.mmc.Entity.VPmsEditionPlanningMensuelChambre;
import cloud.multimicro.mmc.Entity.VPmsEditionRemplissageAnnuel;
import cloud.multimicro.mmc.Entity.VPmsEditionSoldeNoteOuverte;
import cloud.multimicro.mmc.Entity.VPosEditionCaActivite;
import cloud.multimicro.mmc.Entity.VPosEditionConsolidation;
import cloud.multimicro.mmc.Entity.VPosEditionConsolidationResto;
import cloud.multimicro.mmc.Entity.VPosEditionJournalOffert;
import cloud.multimicro.mmc.Entity.VPosEditionNoteSoldeJour;
import cloud.multimicro.mmc.Entity.VPosEditionPrestationVendue;
import cloud.multimicro.mmc.Entity.VPosEditionVisualisationModeEncaissement;
import java.util.List;
import java.util.Objects;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.json.JsonArray;
import javax.json.JsonObject;
import javax.ws.rs.GET;
import javax.ws.rs.NotFoundException;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;
import org.jboss.logging.Logger;

/**
 *
 * @author Tsiory
 */
@Stateless
@Path("editing")
@Produces(MediaType.APPLICATION_JSON)
public class ReportingService {
    private static final Logger LOGGER = Logger.getLogger(ProductService.class);
    @Inject
    ReportingDao reportingDao;

    // PMS MODEL TARIF
    /*
     * @GET
     * 
     * @Path("/pms/report-product")
     * 
     * @Produces(MediaType.APPLICATION_JSON) public Response getAll(@Context UriInfo
     * info) { String dateStart = info.getQueryParameters().getFirst("dateStart");
     * String dateEnd = info.getQueryParameters().getFirst("dateEnd");
     * List<VPmsEditionPrestationVendue> listModel = reportingDao.getAll(dateStart,
     * dateEnd); if (listModel.isEmpty()) { throw new NotFoundException(); } return
     * Response.ok(listModel, MediaType.APPLICATION_JSON).build(); }
     */

    @GET
    @Path("/pms/report-product")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getEditionPrestationVendue(@Context UriInfo info) {
        String dateStart = info.getQueryParameters().getFirst("dateStart");
        String dateEnd = info.getQueryParameters().getFirst("dateEnd");
        JsonArray prestaVendue = reportingDao.getEditionPrestationVendue(dateStart, dateEnd);
        if (prestaVendue.isEmpty()) {
            throw new NotFoundException();
        }
        return Response.ok(prestaVendue, MediaType.APPLICATION_JSON).build();
    }

    /*
     * @GET
     * 
     * @Path("/pms/report-planned-departure")
     * 
     * @Produces(MediaType.APPLICATION_JSON) public Response
     * getAllEditionDepartPrevu(@Context UriInfo info) { String dateStart =
     * info.getQueryParameters().getFirst("dateStart"); String dateEnd =
     * info.getQueryParameters().getFirst("dateEnd"); List<VPmsEditionDepartPrevu>
     * listModel = reportingDao.getAllEditionDepartPrevu(dateStart, dateEnd); if
     * (listModel.isEmpty()) { throw new NotFoundException(); } return
     * Response.ok(listModel, MediaType.APPLICATION_JSON).build(); }
     */

    @GET
    @Path("/pms/report-planned-departure")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getAllEditionDepartPrevu() {
        List<VPmsEditionDepartPrevu> departPrevu = reportingDao.getAllEditionDepartPrevu();
        if (departPrevu.isEmpty()) {
            throw new NotFoundException();
        }
        return Response.ok(departPrevu, MediaType.APPLICATION_JSON).build();
    }

    @GET
    @Path("/pms/report-planned-arrival")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getAllEditionArrivalPrevu() {
        List<VPmsEditionArriveePrevue> arrivalPrevu = reportingDao.getAllEditionArrivalPrevu();
        if (arrivalPrevu.isEmpty()) {
            throw new NotFoundException();
        }
        return Response.ok(arrivalPrevu, MediaType.APPLICATION_JSON).build();
    }

    /*
     * @GET
     * 
     * @Path("/pms/report-arrival-departure")
     * 
     * @Produces(MediaType.APPLICATION_JSON) public Response
     * getEditionRaportArrivalDeparture(@Context UriInfo info) { String
     * dateReference = info.getQueryParameters().getFirst("dateReference");
     * List<VPmsEditionRapportArriveeDepart> listArrivalDeparture =
     * reportingDao.getEditionRaportArrivalDeparture(dateReference); if
     * (listArrivalDeparture.isEmpty()) { throw new NotFoundException(); } return
     * Response.ok(listArrivalDeparture, MediaType.APPLICATION_JSON).build(); }
     */

    @GET
    @Path("/pms/report-arrival-departure")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getEditionRaportArrivalDeparture() {
        List<VPmsEditionRapportArriveeDepart> arrivalDeparture = reportingDao.getEditionRaportArrivalDeparture();
        if (arrivalDeparture.isEmpty()) {
            throw new NotFoundException();
        }
        return Response.ok(arrivalDeparture, MediaType.APPLICATION_JSON).build();
    }

    /*
     * @GET
     * 
     * @Path("/pms/report-client")
     * 
     * @Produces(MediaType.APPLICATION_JSON) public Response
     * getVPmsEditionClientPresent(@Context UriInfo info) { String dateStart =
     * info.getQueryParameters().getFirst("dateStart"); String dateEnd =
     * info.getQueryParameters().getFirst("dateEnd"); List<VPmsEditionClientPresent>
     * listModel = reportingDao.getVPmsEditionClientPresent(dateStart, dateEnd); if
     * (listModel.isEmpty()) { throw new NotFoundException(); } return
     * Response.ok(listModel, MediaType.APPLICATION_JSON).build(); }
     */

    @GET
    @Path("/pms/report-client")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getAllClientPresent() {
        List<VPmsEditionClientPresent> clientPresent = reportingDao.getAllClientPresent();
        if (clientPresent.isEmpty()) {
            throw new NotFoundException();
        }
        return Response.ok(clientPresent, MediaType.APPLICATION_JSON).build();
    }

    /*
     * @GET
     * 
     * @Path("/pms/report-floor")
     * 
     * @Produces(MediaType.APPLICATION_JSON) public Response
     * getAllEditionRapportEtage(@Context UriInfo info) { String dateReference =
     * info.getQueryParameters().getFirst("dateReference");
     * List<VPmsEditionRapportEtage> listModel =
     * reportingDao.getAllEditionRapportEtage(dateReference); if
     * (listModel.isEmpty()) { throw new NotFoundException(); } return
     * Response.ok(listModel, MediaType.APPLICATION_JSON).build(); }
     */

    @GET
    @Path("/pms/report-floor")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getAllRapportEtage(@Context UriInfo info) {
        String dateReference = info.getQueryParameters().getFirst("dateReference");
        JsonArray rapportEtage = reportingDao.getAllRapportEtage(dateReference);
        if (rapportEtage.isEmpty()) {
            throw new NotFoundException();
        }
        return Response.ok(rapportEtage, MediaType.APPLICATION_JSON).build();
    }

    @GET
    @Path("/pms/report-occupation")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getAllOccupation() {
        List<VPmsEditionOccupation> occupation = reportingDao.getAllOccupation();
        if (occupation.isEmpty()) {
            throw new NotFoundException();
        }
        return Response.ok(occupation, MediaType.APPLICATION_JSON).build();
    }

    /*
     * @GET
     * 
     * @Path("/pms/report-list-occupation")
     * 
     * @Produces(MediaType.APPLICATION_JSON) public Response
     * getVPmsListeOccupation(@Context UriInfo info) { String dateStart =
     * info.getQueryParameters().getFirst("dateStart"); String dateEnd =
     * info.getQueryParameters().getFirst("dateEnd");
     * List<VPmsEditionListeOccupation> listModel =
     * reportingDao.getVPmsListeOccupation(dateStart, dateEnd); if
     * (listModel.isEmpty()) { throw new NotFoundException(); } return
     * Response.ok(listModel, MediaType.APPLICATION_JSON).build(); }
     */

    @GET
    @Path("/pms/report-list-occupation")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getAllListeOccupation() {
        List<VPmsEditionListeOccupation> occupation = reportingDao.getAllListeOccupation();
        if (occupation.isEmpty()) {
            throw new NotFoundException();
        }
        return Response.ok(occupation, MediaType.APPLICATION_JSON).build();
    }

    @GET
    @Path("/pms/report-soldeNoteOuverte")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getAllSoldeNoteOuverte() {
        List<VPmsEditionSoldeNoteOuverte> soldeNote = reportingDao.getAllSoldeNoteOuverte();
        if (soldeNote.isEmpty()) {
            throw new NotFoundException();
        }
        return Response.ok(soldeNote, MediaType.APPLICATION_JSON).build();
    }

    @GET
    @Path("/pms/report-etat-debit")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getAllEtatDebit() {
        List<VPmsEditionEtatDebit> etatDebit = reportingDao.getAllEtatDebit();
        if (etatDebit.isEmpty()) {
            throw new NotFoundException();
        }
        return Response.ok(etatDebit, MediaType.APPLICATION_JSON).build();
    }

    @GET
    @Path("/pms/report-etat-debit-room")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getAllEtatDebitRoom() {
        List<VPmsEditionEtatDebitChambre> etatDebitChbr = reportingDao.getAllEtatDebitRoom();
        if (etatDebitChbr.isEmpty()) {
            throw new NotFoundException();
        }
        return Response.ok(etatDebitChbr, MediaType.APPLICATION_JSON).build();
    }

    @GET
    @Path("/pms/report-etat-control")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getAllEtatControl() {
        List<VPmsEditionEtatControl> etatControl = reportingDao.getAllEtatControl();
        if (etatControl.isEmpty()) {
            throw new NotFoundException();
        }
        return Response.ok(etatControl, MediaType.APPLICATION_JSON).build();
    }

    @GET
    @Path("/pms/report-list-arrival")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getAllListArrival(@Context UriInfo info) {
        String dateStart = info.getQueryParameters().getFirst("dateStart");
        String dateEnd = info.getQueryParameters().getFirst("dateEnd");
        List<VPmsEditionListeArrivee> listArrival = reportingDao.getAllListArrival(dateStart, dateEnd);
        if (listArrival.isEmpty()) {
            throw new NotFoundException();
        }
        return Response.ok(listArrival, MediaType.APPLICATION_JSON).build();
    }

    @GET
    @Path("/pms/report-list-global-arrival")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getAllListGlobalArrival(@Context UriInfo info) {
        String dateStart = info.getQueryParameters().getFirst("dateStart");
        String dateEnd = info.getQueryParameters().getFirst("dateEnd");
        JsonArray listGlobalArrival = reportingDao.getAllListGlobalArrival(dateStart, dateEnd);
        if (listGlobalArrival.isEmpty()) {
            throw new NotFoundException();
        }
        return Response.ok(listGlobalArrival, MediaType.APPLICATION_JSON).build();
    }

    @GET
    @Path("/pms/report-list-global-arrivals")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getAllListGlobalArriv() {
        JsonArray listGlobalArrival = reportingDao.getAllListGlobalArriv();
        if (listGlobalArrival.isEmpty()) {
            throw new NotFoundException();
        }
        return Response.ok(listGlobalArrival, MediaType.APPLICATION_JSON).build();
    }

    @GET
    @Path("/pms/report-list-occupation-reservation")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getListOccupationResa(@Context UriInfo info) {
        String dateStart = info.getQueryParameters().getFirst("dateStart");
        String dateEnd = info.getQueryParameters().getFirst("dateEnd");
        JsonArray listeOccResa = reportingDao.getListOccupationResa(dateStart, dateEnd);
        if (listeOccResa.isEmpty()) {
            throw new NotFoundException();
        }
        return Response.ok(listeOccResa, MediaType.APPLICATION_JSON).build();
    }

    @GET
    @Path("/pms/report-list-occupation-resa")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getAllListOccupationResa() {
        JsonArray listeOccResa = reportingDao.getAllListOccupationResa();
        if (listeOccResa.isEmpty()) {
            throw new NotFoundException();
        }
        return Response.ok(listeOccResa, MediaType.APPLICATION_JSON).build();
    }

    @GET
    @Path("/pms/report-list-occupation-reel")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getAllListOccupationReel(@Context UriInfo info) {
        String dateStart = info.getQueryParameters().getFirst("dateStart");
        String dateEnd = info.getQueryParameters().getFirst("dateEnd");
        List<VPmsEditionListeOccupationReel> listeOccReel = reportingDao.getAllListOccupationReel(dateStart, dateEnd);
        if (listeOccReel.isEmpty()) {
            throw new NotFoundException();
        }
        return Response.ok(listeOccReel, MediaType.APPLICATION_JSON).build();
    }

    @GET
    @Path("/pms/report-list-occupation-reel-recep")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getAllListOccupationReelRecep(@Context UriInfo info) {
        String dateStart = info.getQueryParameters().getFirst("dateStart");
        String dateEnd = info.getQueryParameters().getFirst("dateEnd");
        List<VPmsEditionListeOccupationReelRecep> listeOccReelRecep = reportingDao
                .getAllListOccupationReelRecep(dateStart, dateEnd);
        if (listeOccReelRecep.isEmpty()) {
            throw new NotFoundException();
        }
        return Response.ok(listeOccReelRecep, MediaType.APPLICATION_JSON).build();
    }

    @GET
    @Path("/pms/report-list-option")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getAllListOption(@Context UriInfo info) {
        String dateStart = info.getQueryParameters().getFirst("dateStart");
        String dateEnd = info.getQueryParameters().getFirst("dateEnd");
        List<VPmsEditionListeOption> listeOption = reportingDao.getAllListOption(dateStart, dateEnd);
        if (listeOption.isEmpty()) {
            throw new NotFoundException();
        }
        return Response.ok(listeOption, MediaType.APPLICATION_JSON).build();
    }

    @GET
    @Path("/pms/report-list-verif-arrivals")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getAllListVerifArrival() {
        JsonArray listeVerifArrival = reportingDao.getAllListVerifArrival();
        if (listeVerifArrival.isEmpty()) {
            throw new NotFoundException();
        }
        return Response.ok(listeVerifArrival, MediaType.APPLICATION_JSON).build();
    }

    @GET
    @Path("/pms/report-list-verif-arrival")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getListVerifArrival(@Context UriInfo info) {
        String dateStart = info.getQueryParameters().getFirst("dateStart");
        String dateEnd = info.getQueryParameters().getFirst("dateEnd");
        JsonArray listeVerifArrival = reportingDao.getListVerifArrival(dateStart, dateEnd);
        if (listeVerifArrival.isEmpty()) {
            throw new NotFoundException();
        }
        return Response.ok(listeVerifArrival, MediaType.APPLICATION_JSON).build();
    }

    @GET
    @Path("/pms/report-list-product-arrival")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getAllListProductArrivalWithDate(@Context UriInfo info) {
        String dateStart = info.getQueryParameters().getFirst("dateStart");
        String dateEnd = info.getQueryParameters().getFirst("dateEnd");
        JsonArray listeProductArrival = reportingDao.getAllListProductArrivalWithDate(dateStart, dateEnd);
        if (listeProductArrival.isEmpty()) {
            throw new NotFoundException();
        }
        return Response.ok(listeProductArrival, MediaType.APPLICATION_JSON).build();
    }

    @GET
    @Path("/pms/report-list-product-arrivals")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getAllListProductArrival() {
        JsonArray listeProductArrival = reportingDao.getAllListProductArrival();
        if (listeProductArrival.isEmpty()) {
            throw new NotFoundException();
        }
        return Response.ok(listeProductArrival, MediaType.APPLICATION_JSON).build();
    }

    @GET
    @Path("/pms/report-etat-remplissage")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getAllEtatRemplissage() {
        List<VPmsEditionEtatRemplissage> etatRemplissage = reportingDao.getAllEtatRemplissage();
        if (etatRemplissage.isEmpty()) {
            throw new NotFoundException();
        }
        return Response.ok(etatRemplissage, MediaType.APPLICATION_JSON).build();
    }

    @GET
    @Path("/pms/report-remplissage-annuel")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getAllRemplissageAnnuel(@Context UriInfo info) {
        String dateStart = info.getQueryParameters().getFirst("dateStart");
        String dateEnd = info.getQueryParameters().getFirst("dateEnd");
        JsonObject remplissageAnnuel = reportingDao.getAllRemplissageAnnuel(dateStart, dateEnd);
        if (remplissageAnnuel == null) {
            throw new NotFoundException();
        }
        return Response.ok(remplissageAnnuel, MediaType.APPLICATION_JSON).build();
    }

    @GET
    @Path("/pms/report-effectif-prev-debour")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getAllEffectifPrevDebours(@Context UriInfo info) {
        String yearMonthStr = info.getQueryParameters().getFirst("yearMonthStr");
        JsonObject effectifDebour = reportingDao.getAllEffectifPrevDebours(yearMonthStr);
        if (effectifDebour == null) {
            throw new NotFoundException();
        }
        return Response.ok(effectifDebour, MediaType.APPLICATION_JSON).build();
    }

    @GET
    @Path("/pms/report-effectif-prev-famille-tarif")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getAllEffectifPrevFamTarif(@Context UriInfo info) {
        String yearMonthStr = info.getQueryParameters().getFirst("yearMonthStr");
        JsonObject effectifFamTarif = reportingDao.getAllEffectifPrevFamTarif(yearMonthStr);
        if (effectifFamTarif == null) {
            throw new NotFoundException();
        }
        return Response.ok(effectifFamTarif, MediaType.APPLICATION_JSON).build();
    }

    @GET
    @Path("/pms/report-planning-month-room")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getAllPlanningMonthRoom() {
        List<VPmsEditionPlanningMensuelChambre> planningMonth = reportingDao.getAllPlanningMonthRoom();
        if (planningMonth.isEmpty()) {
            throw new NotFoundException();
        }
        return Response.ok(planningMonth, MediaType.APPLICATION_JSON).build();
    }

    @GET
    @Path("/pms/report-etat-prev-real-month")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getAllEtatPrevRealMonth(@Context UriInfo info) {
        String dateStart = info.getQueryParameters().getFirst("dateStart");
        String dateEnd = info.getQueryParameters().getFirst("dateEnd");
        JsonObject etatPrevRealMonth = reportingDao.getAllEtatPrevRealMonth(dateStart, dateEnd);
        if (etatPrevRealMonth.isEmpty()) {
            throw new NotFoundException();
        }
        return Response.ok(etatPrevRealMonth, MediaType.APPLICATION_JSON).build();
    }

    @GET
    @Path("/pms/report-cloture-provisoire")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getAllClosureProvisoire() {
        List<VPmsEditionClotureProvisoire> closureProvisoire = reportingDao.getAllClosureProvisoire();
        if (closureProvisoire.isEmpty()) {
            throw new NotFoundException();
        }
        return Response.ok(closureProvisoire, MediaType.APPLICATION_JSON).build();
    }

    @GET
    @Path("/pms/report-cloture-definitive")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getAllClosureDefinitive() {
        List<VPmsEditionClotureDefinitive> closureDefinitive = reportingDao.getAllClosureDefinitive();
        if (closureDefinitive.isEmpty()) {
            throw new NotFoundException();
        }
        return Response.ok(closureDefinitive, MediaType.APPLICATION_JSON).build();
    }

    @GET
    @Path("/pms/report-balance-appartement")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getAllBalanceAppartement() {
        List<VPmsEditionBalanceAppartement> balanceAppartement = reportingDao.getAllBalanceAppartement();
        if (balanceAppartement.isEmpty()) {
            throw new NotFoundException();
        }
        return Response.ok(balanceAppartement, MediaType.APPLICATION_JSON).build();
    }

    @GET
    @Path("/pms/closure")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getDataClosure(@Context UriInfo info) {
        String dateSaisie = info.getQueryParameters().getFirst("dateSaisie");
        List<TPmsNoteEntete> closure = reportingDao.getDataClosure(dateSaisie);
        if (closure.isEmpty()) {
            throw new NotFoundException();
        }
        return Response.ok(closure, MediaType.APPLICATION_JSON).build();
    }

    @GET
    @Path("/com/report-detail-compte")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getAllDetailCompte() {
        List<VComEditionDetailCompte> detailCompte = reportingDao.getAllDetailCompte();
        if (detailCompte.isEmpty()) {
            throw new NotFoundException();
        }
        return Response.ok(detailCompte, MediaType.APPLICATION_JSON).build();
    }

    @GET
    @Path("/com/report-agee-facture")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getAllAgeeFacture() {
        List<VComEditionAgeeFacture> ageeFacture = reportingDao.getAllAgeeFacture();
        if (ageeFacture.isEmpty()) {
            throw new NotFoundException();
        }
        return Response.ok(ageeFacture, MediaType.APPLICATION_JSON).build();
    }

    @GET
    @Path("/com/report-agee-arrhe")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getAllAgeeArrhe() {
        List<VComEditionAgeeArrhe> ageeArrhe = reportingDao.getAllAgeeArrhe();
        if (ageeArrhe.isEmpty()) {
            throw new NotFoundException();
        }
        return Response.ok(ageeArrhe, MediaType.APPLICATION_JSON).build();
    }

    @GET
    @Path("/com/report-extrait-compte")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getAllExtraitCompte() {
        List<VComEditionExtraitCompte> extraitCompte = reportingDao.getAllExtraitCompte();
        if (extraitCompte.isEmpty()) {
            throw new NotFoundException();
        }
        return Response.ok(extraitCompte, MediaType.APPLICATION_JSON).build();
    }

    @GET
    @Path("/com/report-facture")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getAllFacture() {
        List<VComEditionFacture> facture = reportingDao.getAllFacture();
        if (facture.isEmpty()) {
            throw new NotFoundException();
        }
        return Response.ok(facture, MediaType.APPLICATION_JSON).build();
    }

    @GET
    @Path("/com/report-situation-debtor")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getAllSituationDebtor() {
        List<VComEditionSituationDebiteur> situationDebtor = reportingDao.getAllSituationDebtor();
        if (situationDebtor.isEmpty()) {
            throw new NotFoundException();
        }
        return Response.ok(situationDebtor, MediaType.APPLICATION_JSON).build();
    }

    @GET
    @Path("/com/report-releve-simple-facture")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getAllReleveSimpleFacture() {
        List<VComEditionReleveSimpleFacture> releveFacture = reportingDao.getAllReleveSimpleFacture();
        if (releveFacture.isEmpty()) {
            throw new NotFoundException();
        }
        return Response.ok(releveFacture, MediaType.APPLICATION_JSON).build();
    }

    @GET
    @Path("/com/report-releve-detaile-facture")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getAllReleveDetaileFacture() {
        List<VComEditionReleveDetaileFacture> releveDetailFacture = reportingDao.getAllReleveDetaileFacture();
        if (releveDetailFacture.isEmpty()) {
            throw new NotFoundException();
        }
        return Response.ok(releveDetailFacture, MediaType.APPLICATION_JSON).build();
    }

    // RESTAURANT
    @GET
    @Path("/pos/report-note-solde-jour")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getAllNoteSoldeJour(@Context UriInfo info) {
        String dateNote = info.getQueryParameters().getFirst("dateNote");
        List<VPosEditionNoteSoldeJour> noteSoldeJour = reportingDao.getAllNoteSoldeJour(dateNote);
        if (noteSoldeJour.isEmpty()) {
            throw new NotFoundException();
        }
        return Response.ok(noteSoldeJour, MediaType.APPLICATION_JSON).build();
    }

    @GET
    @Path("/pos/report-journal-offert")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getAllJournalOffer(@Context UriInfo info) {
        String startDate = info.getQueryParameters().getFirst("startDate");
        String endDate = info.getQueryParameters().getFirst("endDate");
        String service = info.getQueryParameters().getFirst("service");
        var journalOffert = reportingDao.getAllJournalOffert(startDate, endDate, service);

        if (journalOffert.isEmpty()) {
            throw new NotFoundException();
        }
        return Response.ok(journalOffert, MediaType.APPLICATION_JSON).build();
    }

    @GET
    @Path("/pos/report-statistique-couvert")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getAllStatistiqueCouvert(@Context UriInfo info) {
        String startDate = info.getQueryParameters().getFirst("startDate");
        String endDate = info.getQueryParameters().getFirst("endDate");
        String activityStr = info.getQueryParameters().getFirst("activity");
        Integer activity = (!Objects.isNull(activityStr)) ? Integer.parseInt(activityStr) : 0;

        JsonObject statistiqueCouvert = reportingDao.getAllStatistiqueCouvert(startDate, endDate, activity);
        if (statistiqueCouvert.isEmpty()) {
            throw new NotFoundException();
        }
        return Response.ok(statistiqueCouvert, MediaType.APPLICATION_JSON).build();
    }

    @GET
    @Path("/pos/report-consolidation")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getAllConsolidation(@Context UriInfo info) {
        String startDate = info.getQueryParameters().getFirst("startDate");
        String endDate = info.getQueryParameters().getFirst("endDate");
        String activityStr = info.getQueryParameters().getFirst("activity");
        Integer activity = (!Objects.isNull(activityStr)) ? Integer.parseInt(activityStr) : 0;
        JsonObject consolidation = reportingDao.getAllConsolidation(startDate, endDate, activity);
        if (consolidation.isEmpty()) {
            throw new NotFoundException();
        }
        return Response.ok(consolidation, MediaType.APPLICATION_JSON).build();
    }

    @GET
    @Path("/pos/report-visualisation-mode-encaissement")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getAllVisualisationModeEncaissement(@Context UriInfo info) {
        String dateEncaissement = info.getQueryParameters().getFirst("dateEncaissement");
        String activityStr = info.getQueryParameters().getFirst("activity");
        Integer activity = (!Objects.isNull(activityStr)) ? Integer.parseInt(activityStr) : 0;
        JsonObject visualisationModeEncaissement = reportingDao
                .getAllVisualisationModeEncaissement(dateEncaissement, activity);
        if (visualisationModeEncaissement.isEmpty()) {
            throw new NotFoundException();
        }
        return Response.ok(visualisationModeEncaissement, MediaType.APPLICATION_JSON).build();
    }

    @GET
    @Path("/pos/report-ca-activite")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getAllCaActivite(@Context UriInfo info) {
        String dateStart = info.getQueryParameters().getFirst("startDate");
        String dateEnd = info.getQueryParameters().getFirst("endDate");
        String activityStr = info.getQueryParameters().getFirst("activity");
        Integer activity = (!Objects.isNull(activityStr)) ? Integer.parseInt(activityStr) : 0;
        JsonObject caActivite = reportingDao.getAllCaByActivity(dateStart, dateEnd, activity);
        if (caActivite.isEmpty()) {
            throw new NotFoundException();
        }
        return Response.ok(caActivite, MediaType.APPLICATION_JSON).build();
    }

    @GET
    @Path("/pos/report-prestation-vendue")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getAllPrestationVendue(@Context UriInfo info) {
        String dateStart = info.getQueryParameters().getFirst("startDate");
        String dateEnd = info.getQueryParameters().getFirst("endDate");
        String activityStr = info.getQueryParameters().getFirst("activity");
        Integer activity = (!Objects.isNull(activityStr)) ? Integer.parseInt(activityStr) : 0;
        JsonArray prestaVendue = reportingDao.getAllPrestationVendue(dateStart, dateEnd, activity);
        if (prestaVendue.isEmpty()) {
            throw new NotFoundException();
        }
        return Response.ok(prestaVendue, MediaType.APPLICATION_JSON).build();
    }

    //EDITION COLLECTIVITE
    @GET
    @Path("/collectivite/lecture-famille")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getEditionFamilyReading(@Context UriInfo info) {
        String dateStart = info.getQueryParameters().getFirst("dateStart");
        String dateEnd = info.getQueryParameters().getFirst("dateEnd");
        JsonArray familyRead = reportingDao.getEditionFamilyReading(dateStart, dateEnd);
        if (familyRead.isEmpty()) {
            throw new NotFoundException();
        }
        return Response.ok(familyRead, MediaType.APPLICATION_JSON).build();
    }

    @GET
    @Path("/collectivite/lecture-sous-famille")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getEditionSousFamilyReading(@Context UriInfo info) {
        String dateStart = info.getQueryParameters().getFirst("dateStart");
        String dateEnd = info.getQueryParameters().getFirst("dateEnd");
        JsonArray sousFamilyRead = reportingDao.getEditionSousFamilyReading(dateStart, dateEnd);
        if (sousFamilyRead.isEmpty()) {
            throw new NotFoundException();
        }
        return Response.ok(sousFamilyRead, MediaType.APPLICATION_JSON).build();
    }

    @GET
    @Path("/collectivite/lecture-prestation")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getEditionProductReading(@Context UriInfo info) {
        String dateStart = info.getQueryParameters().getFirst("dateStart");
        String dateEnd = info.getQueryParameters().getFirst("dateEnd");
        JsonArray sousFamilyRead = reportingDao.getEditionProductReading(dateStart, dateEnd);
        if (sousFamilyRead.isEmpty()) {
            throw new NotFoundException();
        }
        return Response.ok(sousFamilyRead, MediaType.APPLICATION_JSON).build();
    }

    @GET
    @Path("/collectivite/valorisation-bac")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getEditionValorisationBac() {
        List<VCollectiviteValorisationBac> valBac = reportingDao.getEditionValorisationBac();
        if (valBac.isEmpty()) {
            throw new NotFoundException();
        }
        return Response.ok(valBac, MediaType.APPLICATION_JSON).build();
    }

    @GET
    @Path("/collectivite/list-admission-subvention")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getListAdmissionSubvention() {
        JsonArray listeAdmSubv = reportingDao.getListAdmissionSubvention();
        if (listeAdmSubv.isEmpty()) {
            throw new NotFoundException();
        }
        return Response.ok(listeAdmSubv, MediaType.APPLICATION_JSON).build();
    }
}
