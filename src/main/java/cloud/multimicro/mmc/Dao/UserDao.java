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
    
    public List<TMmcUser> getUsers() {
         List<TMmcUser> users = entityManager.createQuery("FROM TMmcUser u where u.dateDeletion = null").getResultList();
        return users;
    }
    
    public TMmcUser checkCredentials(String login, String password) {
        try {
            final String pepper = Util.getEnvString("pepper");
            TMmcUser user = (TMmcUser) entityManager.createQuery("FROM TMmcUser WHERE login =:login and dateDeletion = null").setParameter("login", login).getSingleResult();
            String hashedPassword = Util.sha256(pepper + password + user.getSalt());
            if (user.getPassword().equals(hashedPassword)) {
                return user;
            }
        } catch (NoResultException e) {
            return null;
        }
        return null;
    }

    public TMmcUser getUsersById(int id){
       return entityManager.find(TMmcUser.class, id);
    }
    
    public TMmcUser create(TMmcUser user) throws CustomConstraintViolationException { 
        try {
            final String salt = Util.randomSalt();
            final String pepper = Util.getEnvString("pepper");
            user.setSalt(salt);
            String hashedPassword = Util.sha256(pepper + user.getPassword() + user.getSalt());
            user.setPassword(hashedPassword);
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
            if(pUser.getPassword().trim().length() > 0){
                final String pepper = Util.getEnvString("pepper");
                pUser.setPassword(Util.sha256(pepper + pUser.getPassword() + user.getSalt()));
                pUser.setSalt(user.getSalt());
            }
            else {
                pUser.setPassword(user.getPassword());
                pUser.setSalt(user.getSalt());
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
