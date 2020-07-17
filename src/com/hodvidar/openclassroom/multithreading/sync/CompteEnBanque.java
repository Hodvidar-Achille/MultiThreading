package com.hodvidar.openclassroom.multithreading.sync;

import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;


/**
 * https://openclassrooms.com/fr/courses/2654566-java-et-le-multithreading/2667835-protegeons-nos-variables#/id/r-2667779
 */
public class CompteEnBanque {

	private AtomicLong solde = new AtomicLong(1_000L);

	private final long decouvert = -130L;

	/**
	 * cette variable va nous servir à savoir le nombre de tentatives de retrait successives
	 */
	private AtomicLong tentativeDeRetrait = new AtomicLong(0);

	private Lock verrou = new ReentrantLock();

	private Condition condition = verrou.newCondition();

	/**
	 * C'est sur cette méthode que nous allons devoir travailler
	 * dans nos threads et vérifier le solde avant de retirer de l'argent
	 */
	public void retrait(long montant){
		verrou.lock();
		String threadName = Thread.currentThread().getName();
		try {
			while((solde.get() - montant) < decouvert){

				//dans ce cas, le thread qui tente de retirer ce montant
				//mettra notre solde en deçà du découvert autorisé
				System.err.println(threadName + " tente de retirer " + montant);

				//on stock le cumul des tentatives de retrait car
				//lorsque le verrou sera levé, tous les threads en attente
				//seront autorisés à faire leur retrait, il faut donc contrôler le cumul
				//de toutes les tentatives de retrait
				tentativeDeRetrait.addAndGet(montant);

				//on pose un verrou via la condition
				//cette instruction rend le thread inéligible
				//à travailler
				condition.await();
			}
			long avant = solde.get();
			solde.set((avant - montant));
			solde();
		} catch (Exception e) {
			e.printStackTrace();
		} finally{
			verrou.unlock();
		}
	}

	//Puisqu’on utilise un objet AtomicLong
	//Inutile de synchroniser. ^^
	//C'est dans cette méthode que la condition sera libérée
	public void depot(long montant){
		//On utilise le même verrou que celui qui a engendré la condition
		//sans cela, la condition créée à partir de ce verrou
		//lèvera une exception si nous tentons de la libérer
		verrou.lock();

		try{

			//Nous faisons notre traitement
			long result = solde.addAndGet(montant);
			solde();

			//Nous vérifions si le solde après les tentatives de retraits
			//sera toujours au dessus de l'autorisation de découvert
			long soldeApresRetrait = getSolde() - tentativeDeRetrait.get();

			//Si tel est le cas, libération du verrou
			if(soldeApresRetrait > decouvert){
				//on réinitialise notre variable de contrôle à 0
				tentativeDeRetrait.set(0);
				//on libère le verrou posé par la condition
				//cette instruction va libérer tous les threads mis en attente
				condition.signalAll();
				System.err.println("\n Montant après retrait (" + soldeApresRetrait + ") > découvert \n");
			}

		}finally{
			//on n’oublie pas de libérer le verrou général
			verrou.unlock();
		}
	}


	public synchronized void solde(){
		System.out.println("Solde actuel, dans " + Thread.currentThread().getName()
				+ " : " +  solde.longValue());
	}

	public synchronized long getSolde(){
		return solde.longValue();
	}

	public long getDecouvert(){
		return decouvert;
	}
}