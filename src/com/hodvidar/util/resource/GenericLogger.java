package com.hodvidar.util.resource;


import java.util.logging.Level;
import java.util.logging.Logger;


/**
 * Provides methods to ensure login where there is no logger.
 * <p>
 * This class has mainly been created to fill all the empty catch clause in the code.
 *
 * @author a.genet
 */
public class GenericLogger {

	private static final Logger logger = Logger.getLogger(GenericLogger.class.getName());

	private static final String default_message = "An exception was met";

	private GenericLogger() {
		throw new IllegalStateException("Utility class");
	}

	/**
	 * Logs an exception without rethrow with a Warning level.
	 *
	 * @param e an exception
	 */
	public static void logException(Exception e) {
		logger.log(Level.WARNING, default_message, e);
	}

	/**
	 * Logs an exception without rethrow with a Warning level.
	 *
	 * @param aLogger a java.util.logging.Logger
	 * @param e
	 */
	public static void logException(Logger aLogger, Exception e) {
		if(aLogger == null) {
			logException(e);
			return;
		}
		aLogger.log(Level.WARNING, default_message, e);
	}

	public static void logInfo(String msg) {
		logger.log(Level.INFO, msg);
	}

}
