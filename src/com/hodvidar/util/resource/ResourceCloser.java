package com.hodvidar.util.resource;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.logging.Level;
import java.util.logging.Logger;


/**
 * Provides methods to ensure closing resources properly.
 */
public final class ResourceCloser {

	private static final Logger logger = Logger.getLogger(ResourceCloser.class.getName());

	private ResourceCloser() {
		throw new IllegalStateException("Utility class");
	}

	/**
	 * Ensures the closing of the given resource with no exception. Otherwise logs the failure.
	 *
	 * @param resource If <code>null</code> nothing happen
	 */
	public static void closeResource(AutoCloseable resource) {
		if (resource == null) {
			return;
		}

		try {
			resource.close();
		} catch (Exception e) {
			logger.log(Level.WARNING, "Failed to close a resource", e);
		}
	}

	/**
	 * Ensures the deletion of the given file with no exception. Otherwise logs the failure.
	 * <p>
	 * Does not handle the "isDirectory()" verification.
	 *
	 * @param aFile If <code>null</code> nothing happen
	 */
	public static void deleteFile(File aFile) {
		if (aFile == null) {
			return;
		}

		String fileName = aFile.getName();
		try {
			if (aFile.delete()) {
				logger.log(Level.INFO, "Successfully deleted a file named '" + fileName + "'");
			} else {
				logger.log(Level.WARNING, "Failed to delete a file named '" + fileName + "'");
			}
		} catch (Exception e) {
			logger.log(Level.WARNING, "Failed to delete a file named '" + fileName + "'", e);
		}
	}

	/**
	 * Ensures the deletion of file associated with the given path with no exception. Otherwise logs the failure.
	 *
	 * @param aPath If <code>null</code> nothing happen
	 */
	public static void deleteFile(Path aPath) {
		if (aPath == null) {
			return;
		}

		String fileName = aPath.toString();
		try {
			Files.delete(aPath);
			logger.log(Level.INFO, "Successfully deleted a file named '" + fileName + "'");
		} catch (Exception e) {
			logger.log(Level.WARNING, "Failed to delete a file named '" + fileName + "'", e);
		}
	}

}
