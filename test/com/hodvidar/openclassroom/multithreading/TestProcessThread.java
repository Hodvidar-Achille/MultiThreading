package com.hodvidar.openclassroom.multithreading;

import org.junit.Test;
import static org.assertj.core.api.Assertions.assertThat;


/**
 *  https://openclassrooms.com/fr/courses/2654566-java-et-le-multithreading/2667835-protegeons-nos-variables
 */
public class TestProcessThread {

	@Test
	public void testDummyRunnable() throws InterruptedException {
		System.out.println("testDummyRunnable - START");
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
		System.out.println("testDummyRunnable - END");
		assertThat(DummyRunnable.counter).isEqualTo(DummyRunnable.expectedCounter);
	}

	@Test
	public void testStillDummyRunnable() throws InterruptedException {
		System.out.println("testStillDummyRunnable - START");
		Thread t1 = new Thread(new StillDummyRunnable());
		Thread t2 = new Thread(new StillDummyRunnable());
		Thread t3 = new Thread(new StillDummyRunnable());
		Thread t4 = new Thread(new StillDummyRunnable());

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
		System.out.println("testStillDummyRunnable - END");
		assertThat(StillDummyRunnable.counter).isEqualTo(StillDummyRunnable.expectedCounter);
	}

	@Test
	public void testLessDummyRunnable() throws InterruptedException {
		System.out.println("testLessDummyRunnable - START");
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
		System.out.println("testLessDummyRunnable - END");
		assertThat(LessDummyRunnable.counter.get()).isEqualTo(LessDummyRunnable.expectedCounter.get());
	}

	@Test
	public void testAnotherLessDummyRunnable() throws InterruptedException {
		System.out.println("testAnotherLessDummyRunnable - START");
		Thread t1 = new Thread(new AnotherLessDummyRunnable());
		Thread t2 = new Thread(new AnotherLessDummyRunnable());
		Thread t3 = new Thread(new AnotherLessDummyRunnable());
		Thread t4 = new Thread(new AnotherLessDummyRunnable());

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
		System.out.println("testAnotherLessDummyRunnable - END");
		assertThat(AnotherLessDummyRunnable.getCounter()).isEqualTo(AnotherLessDummyRunnable.getExpectedCounter());
	}

	@Test
	public void testLockyRunnable() throws InterruptedException {
		System.out.println("testLockyRunnable - START");
		Thread t1 = new Thread(new LockyRunnable());
		Thread t2 = new Thread(new LockyRunnable());
		Thread t3 = new Thread(new LockyRunnable());
		Thread t4 = new Thread(new LockyRunnable());

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
		System.out.println("testLockyRunnable - END");
		assertThat(LockyRunnable.counter.get()).isEqualTo(LockyRunnable.expectedCounter.get());
	}
}
