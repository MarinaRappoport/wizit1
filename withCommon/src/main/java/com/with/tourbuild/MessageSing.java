package com.with.tourbuild;

import java.util.Vector;


public class MessageSing {

	private static MessageSing instance = null;

	
	private Vector<Message> victor;
	
	public static MessageSing getInstance() {
    	  if(instance == null){
    		  instance = new MessageSing();
    	  }
		return instance;
	}

	private MessageSing(){
		victor = new Vector<Message>();
      }

	public Vector<Message> getVictor() {
		return victor;
	}

}
