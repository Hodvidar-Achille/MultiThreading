package com.hodvidar.openclassroom.multithreading;

import static org.assertj.core.api.Assertions.assertThat;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Random;
import java.util.concurrent.Callable;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.FutureTask;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.Semaphore;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import org.junit.Test;

import com.hodvidar.openclassroom.multithreading.callable.FolderScanner;
import com.hodvidar.openclassroom.multithreading.semaphore.AfterBarrier;
import com.hodvidar.openclassroom.multithreading.semaphore.Client;
import com.hodvidar.openclassroom.multithreading.semaphore.CyclicBarrierExample;
import com.hodvidar.openclassroom.multithreading.sync.CompteEnBanque;
import com.hodvidar.openclassroom.multithreading.sync.Journaliste;
import com.hodvidar.openclassroom.multithreading.sync.PersonneInterroge;
import com.hodvidar.openclassroom.multithreading.util.MyThreadUtil;


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

		MyThreadUtil.waitForEndOfThread(t1, t2, t3, t4);

		System.out.println("Result is : "+DummyRunnable.expectedCounter);
		System.out.println("testDummyRunnable - END");
		assertThat(DummyRunnable.counter).isLessThanOrEqualTo(DummyRunnable.expectedCounter);
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

		System.out.println("Result is : " + StillDummyRunnable.expectedCounter);
		System.out.println("testStillDummyRunnable - END");
		assertThat(StillDummyRunnable.counter).isLessThanOrEqualTo(StillDummyRunnable.expectedCounter);
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

		System.out.println("Result is : " + LessDummyRunnable.expectedCounter.get());
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

		System.out.println("Result is : " + AnotherLessDummyRunnable.getExpectedCounter());
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

		System.out.println("Result is : " + LockyRunnable.expectedCounter.get());
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

		ceb.solde();
		System.out.println("testCompteEnBanque - END");
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
			System.out.println("La tâche s'est déroulée correctement");
			return;
		}
		if(ft1.isCancelled()) {
			System.out.println("La tâche a été annulée");
			return;
		}
		System.out.println("La tâche est dans un état inconnu");
	}

	/**
	 * https://openclassrooms.com/fr/courses/2654566-java-et-le-multithreading/2668018-ameliorez-la-gestion-de-vos-threads#/id/r-2667980
	 */
	@Test
	public void testExecutor_Single() {
		System.out.println("testExecutor_Single - START");
		long start = System.currentTimeMillis();
		//Notre executor mono-thread
		ExecutorService execute = Executors.newSingleThreadExecutor();

		//Nous créons maintenant nos objets
		Path chemin = Paths.get("C:\\w");
		Path chemin2 = Paths.get("C:\\data");
		Path chemin3 = Paths.get("C:\\_H");

		//La méthode submit permet de récupérer un objet Future
		//qui contiendra le résultat obtenu
		Future<Long> ft1 = execute.submit(new FolderScanner(chemin));
		Future<Long> ft2 = execute.submit(new FolderScanner(chemin2));
		Future<Long> ft3 = execute.submit(new FolderScanner(chemin3));

		Long total;
		try {
			//Nous ajoutons tous les résultats
			total = ft1.get() + ft2.get() + ft3.get();
			System.out.println("nombre total de fichiers trouvés : " + total);
		} catch (InterruptedException | ExecutionException e) {
			e.printStackTrace();
		}

		//Dès que nos tâches sont terminées, nous fermons le pool
		//Sans cette ligne, ce programme restera en cours d'exécution
		execute.shutdown();
		long end = System.currentTimeMillis();
		long duree = end - start;
		System.out.println("Temps d'execution = "+duree+"ms");
		System.out.println("testExecutor_Single - END");
	}

	/**
	 * https://openclassrooms.com/fr/courses/2654566-java-et-le-multithreading/2668018-ameliorez-la-gestion-de-vos-threads#/id/r-2667971
	 */
	@Test
	public void testExecutor_CachedThreadPool() {
		System.out.println("testExecutor_CachedThreadPool - START");
		long start = System.currentTimeMillis();
		//Notre executor
		ExecutorService execute = Executors.newCachedThreadPool();

		//Nous créons une liste stockant les objets Future<Long>
		ArrayList<Future<Long>> listFuture = new ArrayList<>();

		//Nous créons maintenant nos objets
		Path chemin = Paths.get("C:\\w");
		Path chemin2 = Paths.get("C:\\data");
		Path chemin3 = Paths.get("C:\\_H");

		//On change un peu le code en utilisant une boucle
		Path[] chemins = new Path[]{chemin, chemin2, chemin3};

		Long total = 0L;

		for(Path path : chemins){
			//Nous laçons le traitement
			Future<Long> ft = execute.submit(new FolderScanner(path));
			//Nous stockons l'objet Future<Long>
			//si nous avions utilisé la méthode get() directement
			//Les tâches se seraient lancées de façon séquentielle
			//car la méthode get() attend la fin du traitement
			listFuture.add(ft);
		}

		//Afin d'avoir un traitement en parallèle
		//nous parcourons maintenant la liste de nos objets Future<T>
		Iterator<Future<Long>> it = listFuture.iterator();
		while(it.hasNext()){
			try {
				total += it.next().get();
			} catch (InterruptedException | ExecutionException e) {
				e.printStackTrace();
			}
		}

		System.out.println("nombre total de fichiers trouvés : " + total);

		//Dès que nos tâches sont terminées, nous fermons le pool
		//Sans cette ligne, ce programme restera en cours d'exécution
		execute.shutdown();
		long end = System.currentTimeMillis();
		long duree = end - start;
		System.out.println("Temps d'execution = "+duree+"ms");
		System.out.println("testExecutor_CachedThreadPool - END");
	}

	/**
	 * https://openclassrooms.com/fr/courses/2654566-java-et-le-multithreading/2668018-ameliorez-la-gestion-de-vos-threads#/id/r-2667980
	 */
	@Test
	public void testExecutor_FixedThreadPool() {
		System.out.println("testExecutor_FixedThreadPool - START");
		long start = System.currentTimeMillis();
		//Notre executor
		ExecutorService execute = Executors.newCachedThreadPool();

		//Nous créons une liste stockant les objets Future<Long>
		ArrayList<Future<Long>> listFuture = new ArrayList<>();

		//Nous créons maintenant nos objets
		Path chemin = Paths.get("C:\\w");
		Path chemin2 = Paths.get("C:\\data");
		Path chemin3 = Paths.get("C:\\_H");

		//On change un peu le code en utilisant une boucle
		Path[] chemins = new Path[]{chemin, chemin2, chemin3};

		Long total = 0L;

		for(Path path : chemins){
			//Nous laçons le traitement
			Future<Long> ft = execute.submit(new FolderScanner(path));
			//Nous stockons l'objet Future<Long>
			//si nous avions utilisé la méthode get() directement
			//Les tâches se seraient lancées de façon séquentielle
			//car la méthode get() attend la fin du traitement
			listFuture.add(ft);
		}

		//Afin d'avoir un traitement en parallèle
		//nous parcourons maintenant la liste de nos objets Future<T>
		Iterator<Future<Long>> it = listFuture.iterator();
		while(it.hasNext()){
			try {
				total += it.next().get();
			} catch (InterruptedException | ExecutionException e) {
				e.printStackTrace();
			}
		}

		System.out.println("nombre total de fichiers trouvés : " + total);

		//Dès que nos tâches sont terminées, nous fermons le pool
		//Sans cette ligne, ce programme restera en cours d'exécution
		execute.shutdown();
		long end = System.currentTimeMillis();
		long duree = end - start;
		System.out.println("Temps d'execution = "+duree+"ms");
		System.out.println("testExecutor_FixedThreadPool - END");
	}

	/**
	 * https://openclassrooms.com/fr/courses/2654566-java-et-le-multithreading/2668018-ameliorez-la-gestion-de-vos-threads#/id/r-2668003
	 */
	@Test
	public void testExecutor_ScheduledThreadPool() {
		System.out.println("testExecutor_ScheduledThreadPool - START");
		long start = System.currentTimeMillis();

		//Cette instruction permet de lister le nombre de processeurs disponibles
		//sur la machine exécutant le programme
		int corePoolSize = Runtime.getRuntime().availableProcessors();
		System.out.println("Nombre de processeurs disponibles : " + corePoolSize);

		//Notre executor avec un nombre de processeurs fixés dynamiquement
		ScheduledExecutorService execute = Executors.newScheduledThreadPool(corePoolSize);

		//Nous créons une liste stockant les objets Future<Long>
		ArrayList<Future<Long>> listFuture = new ArrayList<>();

		//Nous créons maintenant nos objets
		Path chemin = Paths.get("C:\\w");
		Path chemin2 = Paths.get("C:\\data");
		Path chemin3 = Paths.get("C:\\_H");

		Long total = 0L;

		//Ici, nous lançons la tâche N° 1 dans 10 secondes
		Future<Long> ft = execute.schedule(new FolderScanner(chemin), 10, TimeUnit.SECONDS);
		listFuture.add(ft);

		//Ici, nous lançons la tâche N° 2 dans 1 secondes
		Future<Long> ft2 = execute.schedule(new FolderScanner(chemin2), 1000, TimeUnit.MILLISECONDS);
		listFuture.add(ft2);

		//Ici, nous lançons la tâche N° 3 dans 1 minute
		Future<Long> ft3 = execute.schedule(new FolderScanner(chemin3), 1, TimeUnit.MINUTES);
		listFuture.add(ft3);


		//Afin d'avoir un traitement en parallèle
		//nous parcourons maintenant la liste de nos objets Future<T>
		Iterator<Future<Long>> it = listFuture.iterator();
		while(it.hasNext()){
			try {
				total += it.next().get();
			} catch (InterruptedException | ExecutionException e) {
				e.printStackTrace();
			}
		}

		System.out.println("nombre total de fichiers trouvés : " + total);


		//Dès que nos tâches sont terminées, nous fermons le pool
		//Sans cette ligne, ce programme restera en cours d'exécution
		execute.shutdown();
		long end = System.currentTimeMillis();
		long duree = end - start;
		System.out.println("Temps d'execution = "+duree+"ms");
		System.out.println("testExecutor_ScheduledThreadPool - END");
	}

	/**
	 * https://openclassrooms.com/fr/courses/2654566-java-et-le-multithreading/2668018-ameliorez-la-gestion-de-vos-threads#/id/r-2668007
	 */
	@Test
	public void testExecutor_ScheduledThreadPool_2() {
		System.out.println("testExecutor_ScheduledThreadPool_2 - START");
		long start = System.currentTimeMillis();
		// TODO
		long end = System.currentTimeMillis();
		long duree = end - start;
		System.out.println("Temps d'execution = "+duree+"ms");
		System.out.println("testExecutor_ScheduledThreadPool_2 - END");
	}

	// SEMAPHORE

	/**
	 * https://openclassrooms.com/fr/courses/2654566-java-et-le-multithreading/2668158-les-synchronyzers#/id/r-2668079
	 */
	@Test
	public void testExecutor_SemaphoreSyncRestaurant() {
		System.out.println("testExecutor_SemaphoreSyncRestaurant - START");
		long start = System.currentTimeMillis();
		//Bon, c'est un restaurant à 5 places...
		//C'est petit, mais en Bretagne, il y en a. ;)
		Semaphore sem = new Semaphore(5);

		ExecutorService execute = Executors.newCachedThreadPool();
		List<Future<?>> futureList = new ArrayList<>();
		int i = 0;
		while(i < 11){
			i += 1;
			Client cli = new Client("Client N°" + i, sem);
			// execute.submit(...) == execute.execute.execute(...)
			// Mais renvoie un Futur correspondant à la tâche (Runnable) donnée.
			futureList.add(execute.submit(cli));

			try {
				Thread.sleep(100);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}

		MyThreadUtil.waitForEndOfFuture(futureList.toArray(new Future<?>[futureList.size()]));
		long end = System.currentTimeMillis();
		long duree = end - start;
		System.out.println("Temps d'execution = "+duree+"ms");
		System.out.println("testExecutor_SemaphoreSyncRestaurant - END");
	}

	/**
	 * https://openclassrooms.com/fr/courses/2654566-java-et-le-multithreading/2668158-les-synchronyzers#/id/r-2668092
	 */
	@Test
	public void testBarrier() {
		System.out.println("testBarrier - START");
		long start = System.currentTimeMillis();
		ExecutorService execute = Executors.newFixedThreadPool(4);
		CyclicBarrier barrier = new CyclicBarrier(4);

		CyclicBarrierExample cbe1, cbe2, cbe3, cbe4;
		cbe1 = new CyclicBarrierExample(0, 100, barrier, "Thread-0-100");
		cbe2 = new CyclicBarrierExample(1_000, 5_000, barrier, "Thread-1000-5000");
		cbe3 = new CyclicBarrierExample(5_000, 15_000, barrier, "Thread-5000-15000");
		cbe4 = new CyclicBarrierExample(10_000, 50_000, barrier, "Thread-10000-50000");

		Future<Integer> ft1 = execute.submit(cbe1);
		Future<Integer> ft2 = execute.submit(cbe2);
		Future<Integer> ft3 = execute.submit(cbe3);
		Future<Integer> ft4 = execute.submit(cbe4);

		try {
			System.out.println("Total = " + (ft1.get() + ft2.get() + ft3.get() + ft4.get()));
		} catch (InterruptedException |ExecutionException e) {
			e.printStackTrace();
		}

		execute.shutdown();
		long end = System.currentTimeMillis();
		long duree = end - start;
		System.out.println("Temps d'execution = "+duree+"ms");
		System.out.println("testBarrier - END");
	}

	/**
	 * https://openclassrooms.com/fr/courses/2654566-java-et-le-multithreading/2668158-les-synchronyzers#/id/r-2668101
	 */
	@Test
	public void testBarrierWithTrigger() {
		System.out.println("testBarrierWithTrigger - START");
		long start = System.currentTimeMillis();
		CyclicBarrierExample cbe1, cbe2, cbe3, cbe4;
		cbe1 = new CyclicBarrierExample(0, 100, "Thread-0-100");
		cbe2 = new CyclicBarrierExample(1_000, 5_000, "Thread-1000-5000");
		cbe3 = new CyclicBarrierExample(5_000, 15_000, "Thread-5000-15000");
		cbe4 = new CyclicBarrierExample(10_000, 50_000, "Thread-10000-50000");

		//Nous allons utiliser une liste pour lancer tous nos threads
		ArrayList<Callable<Integer>> tasks = new ArrayList<>();
		tasks.add(cbe1);
		tasks.add(cbe2);
		tasks.add(cbe3);
		tasks.add(cbe4);

		ExecutorService execute = Executors.newFixedThreadPool(4);

		//Cet objet accepte un deuxième argument qui est un Runnable
		//permettant de faire une action lorsque la barrière cède
		CyclicBarrier barrier = new CyclicBarrier(4, new AfterBarrier(tasks));

		//Nous mettons maintenant notre barrière dans nos objets Callable<Integer>
		cbe1.setBarrier(barrier);
		cbe2.setBarrier(barrier);
		cbe3.setBarrier(barrier);
		cbe4.setBarrier(barrier);

		try {
			//Cette méthode est nouvelle pour vous
			//Vous pouvez ainsi lancer une lister de threads
			//Et récupérer une liste d'objet Future<T> : un par objet Callable<T>
			List<Future<Integer>> listFuture = execute.invokeAll(tasks);

			int resultat = 0;

			//On parcourt les résultats
			for(Future<Integer> ft : listFuture)
				resultat += ft.get();

			System.out.println("Total : " + resultat);

		} catch (InterruptedException |ExecutionException e) {
			e.printStackTrace();
		}
		execute.shutdown();

		long end = System.currentTimeMillis();
		long duree = end - start;
		System.out.println("Temps d'execution = "+duree+"ms");
		System.out.println("testBarrierWithTrigger - END");
	}

	/**
	 * https://openclassrooms.com/fr/courses/2654566-java-et-le-multithreading/2668158-les-synchronyzers#/id/r-2668112
	 */
	@Test
	public void testCountDownLatch()
	{
		System.out.println("testCountDownLatch - START");
		long start = System.currentTimeMillis();

		int numberOfCount = 5;
		CountDownLatch lock = new CountDownLatch(numberOfCount);

		Thread t1 = new Thread(new Runnable(){
			public void run(){
				System.out.println("Premier thread en attente !..");
				try {
					lock.await();
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				System.out.println("Premier thread libéré  après le compte à rebours");
			}
		});

		Thread t2 = new Thread(new Runnable(){
			public void run(){
				System.out.println("Deuxième thread en attente !..");
				try {
					lock.await();
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				System.out.println("Deuxième thread libéré  après le compte à rebours");
			}
		});

		Thread unlockingTread = new Thread(new Runnable(){
			public void run(){
				System.out.println("Thread de décompte démarre...");
				try {
					for(int i = 0 ; i < numberOfCount; i++) {
						Thread.sleep(1000);
						lock.countDown();
						System.out.println((numberOfCount-i)+"...");
					}
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				System.out.println("Thread de décompte fini.");
			}
		});


		t1.start();
		t2.start();
		unlockingTread.start();

		MyThreadUtil.waitForEndOfThread(t1, t2, unlockingTread);
		long end = System.currentTimeMillis();
		long duree = end - start;
		System.out.println("Temps d'execution = "+duree+"ms");
		System.out.println("testCountDownLatch - END");
	}

	/**
	 * https://openclassrooms.com/fr/courses/2654566-java-et-le-multithreading/2668158-les-synchronyzers#/id/r-2668135
	 */
	@Test
	public void testExchanger() {
		System.out.println("testCountDownLatch - START");
		long start = System.currentTimeMillis();

		// TODO

		MyThreadUtil.waitForEndOfThread();
		long end = System.currentTimeMillis();
		long duree = end - start;
		System.out.println("Temps d'execution = "+duree+"ms");
		System.out.println("testCountDownLatch - END");
	}

}
