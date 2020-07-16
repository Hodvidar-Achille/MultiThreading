package com.hodvidar.openclassroom.multithreading;

/**
 * https://openclassrooms.com/fr/courses/2654566-java-et-le-multithreading/2667835-protegeons-nos-variables#/id/r-2667702
 */
public class StillDummyRunnable implements Runnable  {

	public static volatile  Integer counter = 0;

	public static volatile  Integer expectedCounter = 0;

	public StillDummyRunnable() {
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
