package com.hodvidar.openclassroom.multithreading;

import java.util.Random;

import com.hodvidar.openclassroom.multithreading.sync.CompteEnBanque;


public class ThreadRetrait extends Thread {

	private CompteEnBanque ceb;
	private Random rand = new Random();

	private final int duree;

	private static int nbThread = 1;

	public ThreadRetrait(CompteEnBanque c, int dureeEnSeconde){
		this.ceb = c;
		this.duree = dureeEnSeconde;
		this.setName("Retrait" + nbThread++);
	}

	public void run() {
		long start = System.currentTimeMillis();
		while(true){
			int nb = rand.nextInt(300);
			long montant = Integer.valueOf(nb).longValue();
			ceb.retrait(montant);

			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {}
			long current = System.currentTimeMillis();
			if( (current - start) > (1000*duree)) {
				this.interrupt();
				break;
			}
		}
	}
}
