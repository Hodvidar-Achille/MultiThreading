package com.hodvidar.openclassroom.multithreading;

public class LessDummyRunnable implements Runnable  {

	public static volatile  Integer counter = 0;

	public static volatile  Integer expectedCounter = 0;

	public LessDummyRunnable() {
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
