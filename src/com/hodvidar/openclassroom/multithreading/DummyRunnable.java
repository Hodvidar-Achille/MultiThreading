package com.hodvidar.openclassroom.multithreading;

class DummyRunnable implements Runnable {

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