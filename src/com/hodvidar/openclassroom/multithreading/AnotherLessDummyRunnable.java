package com.hodvidar.openclassroom.multithreading;

/**
 * https://openclassrooms.com/fr/courses/2654566-java-et-le-multithreading/2667835-protegeons-nos-variables#/id/r-2667725
 */
public class AnotherLessDummyRunnable implements Runnable  {

	private static Integer counter = 0;

	private static Integer expectedCounter = 0;

	public AnotherLessDummyRunnable() {
	}

	private static synchronized int incrementCounterAndGet(int i) {
		counter += i;
		return counter;
	}

	private static synchronized void incrementExpectedCounter(int i) {
		expectedCounter += i;
	}

	public static synchronized int getCounter() {
		return counter;
	}

	public static synchronized int getExpectedCounter() {
		return expectedCounter;
	}

	public void run() {
		incrementExpectedCounter(10);
		for (int i = 0; i < 10; i++) {
			System.out.println(Thread.currentThread().getName() + " - " + (incrementCounterAndGet(1)));
			try {
				Thread.sleep(2000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
}