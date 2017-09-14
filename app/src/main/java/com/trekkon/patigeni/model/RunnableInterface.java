package com.trekkon.patigeni.model;

import java.lang.Runnable;

public abstract class RunnableInterface implements Runnable{
	protected EventQueue eventQueue;

	public RunnableInterface() {
		eventQueue = new EventQueue();
	}

	public abstract void run();
	 
	public abstract void tick(long time);
	 
	public abstract void handleEvents(); 
	
	public void addEvent(Event e) {
		eventQueue.addEvent(e);
	}
}