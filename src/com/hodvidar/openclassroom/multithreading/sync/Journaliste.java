package com.hodvidar.openclassroom.multithreading.sync;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;

import com.hodvidar.util.resource.GenericLogger;
import com.hodvidar.util.resource.ResourceCloser;


public class Journaliste extends Thread implements AutoCloseable {

	private String nom;
	private Lock verrou;
	private Condition question, reponse;

	private final int nbQuestions;

	public Journaliste(String pNom, Lock pVerrou, Condition pQuestion, Condition pReponse, int pNbQuestions){
		nom = pNom;
		verrou = pVerrou;
		question = pQuestion;
		reponse = pReponse;
		nbQuestions = pNbQuestions;
	}

	public void question(){
		verrou.lock();
		try{
			System.out.println(nom + ", posez votre question : ");
			System.out.println("Question ? "+System.currentTimeMillis());
			//On libère le thread de réponse
			reponse.signalAll();
			//On bloque ce thread
			question.await();
		}catch (InterruptedException e) {
			e.printStackTrace();
		}finally{
			//On n’oublie pas de libérer le verrou !
			verrou.unlock();
		}
	}

	public void run(){
		int i = 1;
		while(true){
			question();
			i += 1;
			if(i > nbQuestions) {
				ResourceCloser.closeResource(this);
				break;
			}
		}
	}

	@Override
	public void close() throws Exception {
		System.out.println("End of thread Journaliste");
		verrou.lock();
		try {
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
		} finally {
			verrou.unlock();
		}
		this.interrupt();
	}
}
