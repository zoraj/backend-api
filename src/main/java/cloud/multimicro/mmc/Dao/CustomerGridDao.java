/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cloud.multimicro.mmc.Dao;

import cloud.multimicro.mmc.Entity.TPmsTarifGrilleClient;
import cloud.multimicro.mmc.Exception.CustomConstraintViolationException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.Stateless;
import javax.json.JsonObject;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.validation.ConstraintViolationException;

/**
 *
 * @author Tsiory
 */
@Stateless
@SuppressWarnings("unchecked")
public class CustomerGridDao {

    @PersistenceContext
    private EntityManager entityManager;

    public void postCustomerGrid(JsonObject object) throws CustomConstraintViolationException {
        Integer pmsTarifGrilleId = Integer.parseInt(object.get("pmsTarifGrilleId").toString());
        String clientsListString = object.get("mmcClientId").toString();

        if (!"[]".equals(clientsListString)) {
            String clientsList = clientsListString.substring(1, clientsListString.length() - 1);
            List<String> clientArrayString = new ArrayList<String>();
            String str[] = clientsList.split(",");
            clientArrayString = Arrays.asList(str);
            for (String client : clientArrayString) {
                try {
                    create(pmsTarifGrilleId, Integer.parseInt(client));
                } catch (CustomConstraintViolationException ex) {
                    Logger.getLogger(CustomerGridDao.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
    }

    public void create(Integer pmsTarifGrilleId, Integer mmcClientId) throws CustomConstraintViolationException {
        TPmsTarifGrilleClient pmsModelTarifOption = new TPmsTarifGrilleClient(pmsTarifGrilleId, mmcClientId);
        try {
            entityManager.persist(pmsModelTarifOption);
        } catch (ConstraintViolationException ex) {
            throw new CustomConstraintViolationException(ex);
        }
    }
}
