package com.hodvidar.openclassroom.multithreading.util;

public final class MyThreadUtil {

	private MyThreadUtil() {
		throw new IllegalStateException("Utility class");
	}

	/**
	 * Will wait for each given thread to be TERMINATED.
	 * @param threads one or more thread(s).
	 */
	public static void waitForEndOfThread(Thread... threads) {
		boolean isTestDone = false;
		while(!isTestDone) {
			isTestDone = areThreadTerminated(threads);
		}
	}

	/**
	 * Returns <code>true</code> if all given threads are TERMINATED.
	 * @param threads one or more thread(s).
	 * @return <code>false</code> at the first thread not TERMINATED
	 */
	public static boolean areThreadTerminated(Thread... threads) {
		for(Thread t : threads) {
			if(!Thread.State.TERMINATED.equals(t.getState())) {
				return false;
			}
		}
		return true;
	}
}
