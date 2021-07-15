package cloud.multimicro.mmc.Exception;

import java.util.function.Consumer;
import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import javax.ws.rs.core.Response.Status;

/**
 *
 * @author Zo Rajaonarivony <zo@multimicro.fr>
 */
public abstract class AbstractCustomException extends Exception {
    private static final long serialVersionUID = 1L;
    private String message;
    private Status status;

    public AbstractCustomException() {
        
    }
    
    public AbstractCustomException(String message) {
        this.message = message;
    }

    public AbstractCustomException(Status status, String message, Exception exception) {
        if (message == null) {
            setMessage(exception.getMessage());
        }
        exception = (Exception) exception.getCause();
        setMessage(message);
    }
    
    public AbstractCustomException(Status status, ConstraintViolationException exception) {
        String[] value = {""};
        exception.getConstraintViolations().forEach((Consumer<? super ConstraintViolation<?>>) cv -> {
                value[0] += cv.getPropertyPath().toString() + " " + cv.getMessage() + "\n";
        });
        setMessage(value[0]);
    }

    private void setMessage(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    @Override
    public synchronized Throwable getCause() {
        return super.getCause();
    }

    @Override
    public String getLocalizedMessage() {
        return super.getLocalizedMessage();
    }

    @Override
    public StackTraceElement[] getStackTrace() {
        return super.getStackTrace();
    }

    @Override
    public void printStackTrace() {
        super.printStackTrace();
    }

    @Override
    public String toString() {
        return super.toString();
    }
}