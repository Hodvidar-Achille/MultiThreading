package com.hodvidar.openclassroom.multithreading;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import com.hodvidar.openclassroom.multithreading.sync.SyncIncrement;


/**
 *  https://openclassrooms.com/fr/courses/2654566-java-et-le-multithreading/2667835-protegeons-nos-variables#/id/r-2667749
 */
public class LockyRunnable implements Runnable  {

	public static SyncIncrement counter = new SyncIncrement();

	public static SyncIncrement expectedCounter = new SyncIncrement();

	public LockyRunnable() {
	}

	public void run() {
		expectedCounter.incrementAndGet(10);
		for (int i = 0; i < 10; i++) {
			System.out.println(Thread.currentThread().getName() + " - " + (counter.incrementAndGet(1)));
			try {
				Thread.sleep(2000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
}
