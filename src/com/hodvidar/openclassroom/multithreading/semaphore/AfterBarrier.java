package com.hodvidar.openclassroom.multithreading.semaphore;


import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;

public class AfterBarrier implements Runnable {

	List<Callable<Integer>> listCallable = new ArrayList<>();

	public AfterBarrier(List<Callable<Integer>> pList){
		listCallable = pList;
	}

	public void run() {

		//Une petite pause pour bien voir dans la console
		try {
			Thread.currentThread().sleep(1000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

		System.out.println("------------------------------------------------------");
		System.out.println("La barrière vient d'être atteinte par tout le monde ! ");
		System.out.println("Voilà où ils en sont : ");

		//On parcours notre liste d'objets
		for(Callable<Integer> call : listCallable){
			//On cast et on affiche le résultat actuel
			CyclicBarrierExample cbe = (CyclicBarrierExample) call;
			System.out.println("\t -> " + cbe.getName() + " : " + cbe.getResultat());
		}

		System.out.println("------------------------------------------------------");

		try {
			Thread.currentThread().sleep(2000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
}