/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ru.tehkode.mualauncher.utils;

import java.io.File;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.logging.ConsoleHandler;
import java.util.logging.FileHandler;
import java.util.logging.Formatter;
import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.LogRecord;

/**
 *
 * @author t3hk0d3
 */
public class Logger {

    private static java.util.logging.Logger logger = java.util.logging.Logger.getLogger("MCLauncher");

    public static void info(String message, Object... values) {
        if (values != null) {
            message = String.format(message, values);
        }

        logger.info(message);
    }

    public static void warning(String message, Object... values) {
        if (values != null) {
            message = String.format(message, values);
        }

        logger.warning(message);
    }

    public static void error(String message, Object... values) {
        if (values != null) {
            message = String.format(message, values);
        }

        logger.severe(message);
    }

    public static void initialize(File logFile) throws IOException {
        Formatter formatter = new LogFormatter();

        logger.setUseParentHandlers(false);
        Handler consoleHandler = new ConsoleHandler();
        consoleHandler.setFormatter(formatter);
        consoleHandler.setLevel(Level.INFO);
        logger.addHandler(consoleHandler);

        if (!logFile.getParentFile().exists()) {
            logFile.getParentFile().mkdirs();
        }

        Handler fileHandler = new FileHandler(logFile.getAbsolutePath(), false);
        fileHandler.setFormatter(formatter);
        fileHandler.setLevel(Level.INFO);

        logger.addHandler(fileHandler);
    }

    protected static class LogFormatter extends Formatter {

        private static final DateFormat df = new SimpleDateFormat("dd/MM/yyyy hh:mm:ss.SSS");

        @Override
        public String format(LogRecord record) {
            StringBuilder builder = new StringBuilder(1000);
            builder.append(df.format(new Date(record.getMillis()))).append(" - ");
            builder.append("[").append(record.getLevel()).append("] - ");
            builder.append(formatMessage(record));
            builder.append("\r\n");
            return builder.toString();
        }
    }
}
