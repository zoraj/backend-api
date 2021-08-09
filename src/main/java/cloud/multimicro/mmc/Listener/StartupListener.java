package cloud.multimicro.mmc.Listener;

import javax.inject.Inject;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;

import org.jboss.logging.Logger;

/**
 * Author: Zo
 */
@WebListener
public class StartupListener implements ServletContextListener {
    private static final Logger LOGGER = Logger.getLogger(StartupListener.class);
    @Inject
    Helper helper;

    public void contextInitialized(ServletContextEvent event) {
        LOGGER.info("---- Context Initialized --- ");
        helper.init();
   }
    public void contextDestroyed(ServletContextEvent event) {
        LOGGER.info("--- Context Destroyed ----");
    }
}
