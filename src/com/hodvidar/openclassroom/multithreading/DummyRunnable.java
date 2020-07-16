package com.hodvidar.openclassroom.multithreading;

/**
 * https://openclassrooms.com/fr/courses/2654566-java-et-le-multithreading/2667696-avant-toutes-choses#/id/r-2667578
 */
public class DummyRunnable implements Runnable {

	public static Integer counter = 0;

	public static Integer expectedCounter = 0;

	public DummyRunnable() {
	}

	public void run() {
		expectedCounter += 10;
		for (int i = 0; i < 10; i++) {
			counter += 1;
			System.out.println(Thread.currentThread().getName() + " - " + (counter));
			try {
				Thread.sleep(2000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
}