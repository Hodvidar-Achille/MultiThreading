package com.hodvidar.openclassroom.multithreading.sync;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;

import com.hodvidar.util.resource.GenericLogger;
import com.hodvidar.util.resource.ResourceCloser;


public class PersonneInterroge extends Thread implements AutoCloseable {

	private String nom;
	private Lock verrou;
	private Condition question, reponse;

	private final int nbResponses;

	public PersonneInterroge(String pNom, Lock pVerrou, Condition pQuestion, Condition pReponse, int pNbResponses){
		nom = pNom;
		verrou = pVerrou;
		question = pQuestion;
		reponse = pReponse;
		nbResponses = pNbResponses;
	}

	public void reponse(){

		try{
			verrou.lock();
			System.out.println(nom + ", votre réponse ?");
			System.out.println("Response ! "+System.currentTimeMillis());
			//On informe redemande une question
			question.signalAll();
			//On bloque la réponse
			reponse.await();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}finally{
			//et on libère le verrou !
			verrou.unlock();
		}
	}

	public void run(){
		int i = 1;
		while(true){
			reponse();
			i += 1;
			if(i > nbResponses) {
				ResourceCloser.closeResource(this);
				break;
			}
		}
	}

	@Override
	public void close() throws Exception {
		System.out.println("End of thread PersonneInterroge");
		try {
			this.question.signalAll();
		} catch (Exception e) {
			GenericLogger.logException(e);
		}
		try {
			this.reponse.signalAll();
		} catch (Exception e) {
			GenericLogger.logException(e);
		}
		this.interrupt();
	}
}