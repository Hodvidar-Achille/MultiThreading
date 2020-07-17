package com.hodvidar.openclassroom.multithreading;

import java.util.Random;

import com.hodvidar.openclassroom.multithreading.sync.CompteEnBanque;


public class ThreadDepot extends Thread{

	private CompteEnBanque ceb;
	private Random rand = new Random();

	private final int duree;

	public ThreadDepot(CompteEnBanque c, int dureeEnSeconde){
		this.ceb = c;
		this.duree = dureeEnSeconde;
		this.setName("Dépôt");
	}

	public void run() {
		long start = System.currentTimeMillis();
		while(true){
			int nb = rand.nextInt(100);
			long montant = Integer.valueOf(nb).longValue();
			ceb.depot(montant);
			try {
				Thread.sleep(2000);
			} catch (InterruptedException e) {}

			long current = System.currentTimeMillis();
			if( (current - start) > (1000*duree)) {
				this.interrupt();
				break;
			}
		}
	}
}