package com.hodvidar.openclassroom.multithreading;

import org.junit.Test;
import static org.assertj.core.api.Assertions.assertThat;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import com.hodvidar.openclassroom.multithreading.sync.CompteEnBanque;
import com.hodvidar.openclassroom.multithreading.sync.Journaliste;
import com.hodvidar.openclassroom.multithreading.sync.PersonneInterroge;
import com.hodvidar.openclassroom.multithreading.util.MyThreadUtil;


/**
 *  https://openclassrooms.com/fr/courses/2654566-java-et-le-multithreading/2667835-protegeons-nos-variables
 */
public class TestProcessThread {

	@Test
	// Will fail
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

		MyThreadUtil.waitForEndOfThread(t1, t2, t3, t4);

		System.out.println("testDummyRunnable - END");
		assertThat(DummyRunnable.counter).isEqualTo(DummyRunnable.expectedCounter);
	}

	@Test
	// Will fail
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

		MyThreadUtil.waitForEndOfThread(t1, t2, t3, t4);

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

		MyThreadUtil.waitForEndOfThread(t1, t2, t3, t4);

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

		MyThreadUtil.waitForEndOfThread(t1, t2, t3, t4);

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

		MyThreadUtil.waitForEndOfThread(t1, t2, t3, t4);

		System.out.println("testLockyRunnable - END");
		assertThat(LockyRunnable.counter.get()).isEqualTo(LockyRunnable.expectedCounter.get());
	}

	@Test
	public void testCompteEnBanque() {
		System.out.println("testCompteEnBanque - START");
		CompteEnBanque ceb = new CompteEnBanque();

		//On crée deux threads de retrait
		Thread t1 = new ThreadRetrait(ceb, 30);
		t1.start();

		Thread t2 = new ThreadRetrait(ceb, 30);
		t2.start();

		//et un thread de dépôt
		Thread t3 = new ThreadDepot(ceb, 40);
		t3.start();

		MyThreadUtil.waitForEndOfThread(t1, t2, t3);
		System.out.println("testCompteEnBanque - END");
		ceb.solde();
		assertThat(ceb.getSolde()).isGreaterThan(ceb.getDecouvert());
	}

	@Test
	public void testInterview() {
		System.out.println("testInterview - START");
		Lock lock = new ReentrantLock();
		Condition question = lock.newCondition();
		Condition response = lock.newCondition();
		Journaliste j = new Journaliste("Journaliste", lock, question, response, 3);
		PersonneInterroge p = new PersonneInterroge("Interviewé", lock, question, response, 3);

		j.start();
		p.start();

		MyThreadUtil.waitForEndOfThread(j, p);

		System.out.println("testInterview - END");
	}


}
