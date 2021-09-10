package cloud.multimicro.mmc.Dao;

import java.util.List;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.validation.ConstraintViolationException;

import cloud.multimicro.mmc.Entity.TMmcUser;
import cloud.multimicro.mmc.Exception.CustomConstraintViolationException;
import cloud.multimicro.mmc.Util.Util;

/**
 * UserDao
 */
@Stateless
public class UserDao {
    @PersistenceContext
    EntityManager entityManager;
     private static final org.jboss.logging.Logger LOGGER = org.jboss.logging.Logger.getLogger(UserDao.class);
    
    public List<TMmcUser> getUsers() {
         List<TMmcUser> users = entityManager.createQuery("FROM TMmcUser u where u.dateDeletion = null").getResultList();
        return users;
    }
    
    public TMmcUser checkCredentials(String pinCode) {
        try {           
            final String pepper = Util.getEnvString("pepper");
            String hashedpinCode = Util.sha256(pepper + pinCode);           
            TMmcUser user = (TMmcUser) entityManager.createQuery("FROM TMmcUser WHERE pinCode =:hashedPinCode AND dateDeletion = null").setParameter("hashedPinCode", hashedpinCode ).getSingleResult();                  
            return user;
            /*if (user.getPinCode().equals(hashedpinCode)) {
                return user;
            }*/

        } catch (NoResultException e) {
            LOGGER.info("test d'enter");
            return null;
        }
        //return null;
    }

    public TMmcUser getUsersById(int id){
       return entityManager.find(TMmcUser.class, id);
    }
    
    public TMmcUser create(TMmcUser user) throws CustomConstraintViolationException { 
        try {
            //final String salt = Util.randomSalt();
            final String pepper = Util.getEnvString("pepper");
            String hashedPassword = Util.sha256(pepper + user.getPinCode());
            user.setPinCode(hashedPassword);
            entityManager.persist(user);
            return user;
        }
        catch(ConstraintViolationException ex) {
            throw new CustomConstraintViolationException(ex);
        }
    }
    
      //UPDATE PRINTER
    public void update(TMmcUser pUser) throws CustomConstraintViolationException {   
        try {
            TMmcUser user = getUsersById(pUser.getId());
            if(pUser.getPinCode().trim().length() > 0){
                final String pepper = Util.getEnvString("pepper");
                String hashedPassword = Util.sha256(pepper + user.getPinCode());
                pUser.setPinCode(hashedPassword);
                //pUser.setSalt(user.getSalt());
            }
            else {
                pUser.setPinCode(user.getPinCode());
                //pUser.setSalt(user.getSalt());
            }
            entityManager.merge(pUser);
        }
        catch(ConstraintViolationException ex) {
            throw new CustomConstraintViolationException(ex);
        }
    }
    
    public void delete(int id) {         
        entityManager.createNativeQuery("UPDATE t_mmc_user SET date_deletion = CURRENT_TIMESTAMP WHERE id=:id")              
             .setParameter("id", id)               
             .executeUpdate();
    }
}
