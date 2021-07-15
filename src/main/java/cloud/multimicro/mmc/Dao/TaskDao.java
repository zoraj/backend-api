/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cloud.multimicro.mmc.Dao;

import cloud.multimicro.mmc.Entity.TMmcTache;
import java.text.ParseException;
import java.util.List;
import java.util.logging.Level;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 *
 * @author Naly
 */
@Stateless
@SuppressWarnings("unchecked")
public class TaskDao {
    
    @PersistenceContext
    private EntityManager entityManager;
    
    public List<TMmcTache> getTache(String module) {
        List<TMmcTache> tache = entityManager.createQuery("FROM TMmcTache  WHERE module=:module AND dateDeletion = null")
                .setParameter("module", module)
                .getResultList();
        return tache;
    }
    
}
