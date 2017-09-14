package com.trekkon.patigeni.model;

import java.util.*;
import java.util.concurrent.*;

public class EventQueue{
	private Queue<Event> q;
	private Semaphore lock;

	public EventQueue() {
		q = new LinkedList<Event>();
		lock = new Semaphore(1);
	}
	
	public void addEvent(Event e){
		try {
			lock.acquire();
			q.offer(e);
			lock.release();
		} catch(InterruptedException ex) {
			ex.printStackTrace();
		}
	}

	public Event pollEvent(){
		Event e = null;

		try {
			lock.acquire();
			e = q.poll();
			lock.release();
		} catch(InterruptedException ex) {
			ex.printStackTrace();
		}

		return e;		
	}
	
	public Event peekEvent(){
		Event e = null;

		try {
			lock.acquire();
			e = q.peek();
			lock.release();
		} catch(InterruptedException ex) {
			ex.printStackTrace();
		}

		return e;			
	}
}