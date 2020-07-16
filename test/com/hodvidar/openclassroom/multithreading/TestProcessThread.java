package com.hodvidar.openclassroom.multithreading;

import org.junit.Test;
import static org.assertj.core.api.Assertions.assertThat;

public class TestProcessThread {

	@Test
	public void testDummyThread() throws InterruptedException {
		System.out.println("testDummyThread - START");
		Thread t1 = new Thread(new DummyRunnable());
		Thread t2 = new Thread(new DummyRunnable());
		Thread t3 = new Thread(new DummyRunnable());
		Thread t4 = new Thread(new DummyRunnable());

		t1.start();
		t2.start();
		t3.start();
		t4.start();

		boolean isTestDone = false;
		while(!isTestDone) {
			Thread.State st1 = t1.getState();
			Thread.State st2 = t2.getState();
			Thread.State st3 = t3.getState();
			Thread.State st4 = t4.getState();
			isTestDone = Thread.State.TERMINATED.equals(st1)
					&& Thread.State.TERMINATED.equals(st2)
					&& Thread.State.TERMINATED.equals(st3)
					&& Thread.State.TERMINATED.equals(st4);
		}
		System.out.println("testDummyThread - END");
		assertThat(DummyRunnable.counter).isEqualTo(DummyRunnable.expectedCounter);
	}

	@Test
	public void testLessDummyThread() throws InterruptedException {
		System.out.println("testLessDummyThread - START");
		Thread t1 = new Thread(new LessDummyRunnable());
		Thread t2 = new Thread(new LessDummyRunnable());
		Thread t3 = new Thread(new LessDummyRunnable());
		Thread t4 = new Thread(new LessDummyRunnable());

		t1.start();
		t2.start();
		t3.start();
		t4.start();

		boolean isTestDone = false;
		while(!isTestDone) {
			Thread.State st1 = t1.getState();
			Thread.State st2 = t2.getState();
			Thread.State st3 = t3.getState();
			Thread.State st4 = t4.getState();
			isTestDone = Thread.State.TERMINATED.equals(st1)
					&& Thread.State.TERMINATED.equals(st2)
					&& Thread.State.TERMINATED.equals(st3)
					&& Thread.State.TERMINATED.equals(st4);
		}
		System.out.println("testLessDummyThread - END");
		assertThat(LessDummyRunnable.counter).isEqualTo(LessDummyRunnable.expectedCounter);
	}
}
