/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cloud.multimicro.mmc.Exception;

import javax.validation.ConstraintViolationException;
import javax.ws.rs.core.Response.Status;
/**
 *
 * @author zo
 */
public class CustomConstraintViolationException  extends AbstractCustomException  {
    private static final long serialVersionUID = 1L;
    
    public CustomConstraintViolationException(String message) {
        super(message);
    }
    
    public CustomConstraintViolationException(String message, Exception exception) {
        super(Status.BAD_REQUEST, message, exception);        
    }  

    public CustomConstraintViolationException(ConstraintViolationException ex) {
        super(Status.BAD_REQUEST, ex);   
    }

    public CustomConstraintViolationException(CustomConstraintViolationException ex) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
}
