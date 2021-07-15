/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cloud.multimicro.mmc.Exception;

import javax.ws.rs.core.Response.Status;


/**
 *
 * @author Zo Rajaonarivony <zo@multimicro.fr>
 */
public class DataException extends AbstractCustomException  {
    private static final long serialVersionUID = 1L;

    public DataException(String message) {
        super(message);
    }

    public DataException(String message, Exception exception) {
        super(Status.BAD_REQUEST, message, exception);
    }    
}
