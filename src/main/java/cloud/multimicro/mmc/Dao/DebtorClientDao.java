/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cloud.multimicro.mmc.Dao;

import cloud.multimicro.mmc.Entity.VPmsClientDebiteur;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 *
 * @author Tsiory
 */
@Stateless
public class DebtorClientDao {
    @PersistenceContext
    EntityManager entityManager;

    public List<VPmsClientDebiteur> getAll() {
        List<VPmsClientDebiteur> result = entityManager.createQuery("FROM VPmsClientDebiteur ORDER BY compteDebiteur").getResultList();
        return result;
    } 
}

