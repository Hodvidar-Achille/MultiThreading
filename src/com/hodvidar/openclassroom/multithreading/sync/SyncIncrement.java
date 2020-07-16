package com.hodvidar.openclassroom.multithreading.sync;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;


/**
 * Thread Safe integer using a lock
 */
public class SyncIncrement {

	private int entier = 0;
	//Nous créons notre objet qui va nous fournir un verrou
	private Lock verrou = new ReentrantLock();

	public int incrementAndGet(int i){

		//Nous verrouillons le code qui suit cette instruction
		verrou.lock();

		//Nous utilisons un bloc try surtout
		//pour pouvoir avoir un bloc finally
		try{

			//tout ce code est maintenant considéré comme atomique !
			entier += i;
		}finally{
			//ainsi, même s'il y a eu une interruption sur notre thread
			//le verrou sera relâché, dans le cas contraire
			//tous les autres threads ne pourraient plus travailler !
			verrou.unlock();
		}
		return entier;
	}

	public synchronized int get(){
		return entier;
	}
}
