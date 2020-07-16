package com.hodvidar.openclassroom.multithreading;

import java.util.concurrent.atomic.AtomicInteger;


/**
 * https://openclassrooms.com/fr/courses/2654566-java-et-le-multithreading/2667835-protegeons-nos-variables#/id/r-2667723
 */
public class LessDummyRunnable implements Runnable  {

	public static AtomicInteger counter = new AtomicInteger(0);

	public static AtomicInteger expectedCounter = new AtomicInteger(0);

	public LessDummyRunnable() {
	}

	public void run() {
		expectedCounter.addAndGet(10);
		for (int i = 0; i < 10; i++) {
			System.out.println(Thread.currentThread().getName() + " - " + (counter.addAndGet(1)));
			try {
				Thread.sleep(2000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
}
