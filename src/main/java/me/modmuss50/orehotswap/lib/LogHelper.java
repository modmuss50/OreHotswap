package me.modmuss50.orehotswap.lib;

import cpw.mods.fml.common.FMLLog;
import org.apache.logging.log4j.Level;

/**
 * Created by modmuss50 on 21/04/2016.
 */
public class LogHelper {

    public void log(Level logLevel, Object object) {
        FMLLog.log("orehotswap", logLevel, String.valueOf(object), new Object[0]);
    }

    public void all(Object object) {
        this.log(Level.ALL, object);
    }

    public void debug(Object object) {
        this.log(Level.DEBUG, object);
    }

    public void error(Object object) {
        this.log(Level.ERROR, object);
    }

    public void fatal(Object object) {
        this.log(Level.FATAL, object);
    }

    public void info(Object object) {
        this.log(Level.INFO, object);
    }

    public void off(Object object) {
        this.log(Level.OFF, object);
    }

    public void trace(Object object) {
        this.log(Level.TRACE, object);
    }

    public void warn(Object object) {
        this.log(Level.WARN, object);
    }
}
