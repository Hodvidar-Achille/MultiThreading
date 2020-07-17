package com.hodvidar.openclassroom.multithreading;

import org.junit.Test;
import static org.assertj.core.api.Assertions.assertThat;

import java.util.Random;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.FutureTask;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
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
		Journaliste j = new Journaliste("Journaliste", lock, question, response, 5);
		PersonneInterroge p = new PersonneInterroge("Interviewé", lock, question, response, 5);

		j.start();
		p.start();

		MyThreadUtil.waitForEndOfThread(j, p);

		System.out.println("testInterview - END");
	}

	@Test
	/**
	 * https://openclassrooms.com/fr/courses/2654566-java-et-le-multithreading/2668018-ameliorez-la-gestion-de-vos-threads#/id/r-2667879
 	 */
	public void testCallable() {
		System.out.println("testCallable - START");

		//Nous créons un objet Callable basique
		Callable<Integer> c1 = new Callable<Integer>(){
			public Integer call() throws Exception {
				Random rand = new Random();
				int result = rand.nextInt(2_000);
				System.out.println("Dans l'objet Callable : " + result);
				try {
					Thread.sleep(3_000);
				} catch (Exception e) {
					e.printStackTrace();
				}
				return result;
			}
		};

		//nous l'associons à un objet FutureTask
		//du même type générique
		FutureTask<Integer> ft1 = new FutureTask<>(c1);

		System.out.println(" - Lancement de notre premier test.");
		//Pour que cette tâche soit lancée dans un thread
		//nous devons tout de même utiliser la classe Thread
		//qui autorise un objet de type FutureTask dans son constructeur
		Thread t = new Thread(ft1);

		//Nous lançons maintenant le thread
		t.start();
		System.out.println("Traitement…");
		try {
			//Ici, notre objet Future attend la fin de la tâche pour
			//retourner le résultat, en attendant
			//le thread courant est bloqué
			System.out.println("Résultat : " + ft1.get());
		} catch (Exception e) {
			e.printStackTrace();
		}

		showStatus(ft1);

		System.out.println("\n - Lancement de notre second test.");
		ft1 = new FutureTask<>(c1);
		t = new Thread(ft1);
		t.start();
		System.out.println("Traitement…");

		//Ici, nous mettons un délai, il y aura donc une exception de levée
		//car le délai est inférieur à la pause dans l'objet Callable
		try {
			System.out.println("Résultat : " + ft1.get(500, TimeUnit.MILLISECONDS));
		} catch (InterruptedException | ExecutionException | TimeoutException e) {
			System.err.println("La tâche à mis trop de temps à répondre.");
		}
		//Cette instruction n'affichera rien car le statut
		//de la tâche n'est ni OK ni annulée...
		showStatus(ft1);

		MyThreadUtil.waitForEndOfThread(t);

		showStatus(ft1);

		System.out.println("testCallable - END");
	}

	private static void showStatus(FutureTask<Integer> ft1){
		if(ft1.isDone()) {
			System.out.println("La tâche c'est déroulée correctement");
			return;
		}
		if(ft1.isCancelled()) {
			System.out.println("La tâche a été annulée");
			return;
		}
		System.out.println("La tâche est dans un état inconnu");
	}


}
