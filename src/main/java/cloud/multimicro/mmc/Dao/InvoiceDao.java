/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cloud.multimicro.mmc.Dao;

import cloud.multimicro.mmc.Entity.VPmsFacture;
import cloud.multimicro.mmc.Entity.VPmsFactureDetail;
import java.util.List;
import java.util.Objects;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.ws.rs.core.MultivaluedMap;


/**
 *
 * @author Naly
 */
@Stateless
public class InvoiceDao {
    @PersistenceContext
    EntityManager entityManager;
    
    public VPmsFacture getInvoiceHeader(String numInvoice)throws NoResultException {
        VPmsFacture result = (VPmsFacture) entityManager.createQuery("FROM VPmsFacture WHERE numFacture =:numInvoice")
                .setParameter("numInvoice", numInvoice)
                .getSingleResult();
        return result;

    }
    
    public List<VPmsFactureDetail> getInvoiceDetail(String numInvoice)throws NoResultException {
        List<VPmsFactureDetail> result = entityManager.createQuery("FROM VPmsFactureDetail WHERE numFacture =:numInvoice")
            .setParameter("numInvoice", numInvoice)
            .getResultList();
        return result;

    }
    
    public VPmsFacture getByEnteteId(int id){     
        VPmsFacture invoice = entityManager.find(VPmsFacture.class, id);
        return invoice;
    }
    
    public List<VPmsFacture> getAll(MultivaluedMap<String, String> list) {

        String numFacture = list.getFirst("numFacture");
        String datenote = list.getFirst("datenote");
        String customer = list.getFirst("customer");
        String status = list.getFirst("statutPointage");
        
        Boolean isExist = false;
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("FROM VPmsFacture  ");
     

        if (!Objects.isNull(numFacture)) {
            stringBuilder.append(" WHERE numFacture = '" + numFacture + "'");
            isExist = true;
        }

        if (!Objects.isNull(datenote)) {
            if (isExist == true) {
                stringBuilder.append(" AND dateNote <= '" + datenote + "'");
            } else {
                stringBuilder.append(" WHERE  dateNote <= '" + datenote + "'");
                isExist = true;
            }
        }

        if (!Objects.isNull(customer)) {
            if (isExist == true) {
                stringBuilder.append(" AND mmcClientId = " + customer );
            } else {
                stringBuilder.append(" WHERE mmcClientId = " + customer);
                isExist = true;
            }
        }

        if (!Objects.isNull(status)) {
            if (isExist == true) {
                stringBuilder.append(" AND statutPointage IS NULL OR statutPointage = 1 ");
            } else {
                stringBuilder.append(" WHERE statutPointage  IS NULL OR statutPointage = 1 ");
                isExist = true;
            }
        }
        
        
        List<VPmsFacture> result = entityManager.createQuery(stringBuilder.toString()).getResultList();
        return result;
    }
}
