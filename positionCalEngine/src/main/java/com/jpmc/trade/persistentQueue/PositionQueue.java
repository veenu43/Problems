package com.jpmc.trade.persistentQueue;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;

import com.jpmc.trade.model.Position;
import com.jpmc.trade.threads.PositionAggregatorThread;

@Service
public class PositionQueue {
	private static final Logger LOG = Logger.getLogger(PositionQueue.class);
	private Map<String,Position> queue = new HashMap<String, Position>();
	
	
	public void persistPosition(Position position){
		if(!position.isEmpty())
		{
			queue.put(position.getPositionKey(), position);
		}
	}
	
	public Position fetchPosition(String account,String instrument){
		Position position = queue.get(positionKey(account,instrument));
		if(position ==null){
			position = new Position();
		}
		return position;
	}
	
	public void flushAllPositions(){
		  Collection<Position> positions =  queue.values();
		  for(Position position: positions){
			  LOG.info("Position: "+position);
		  }
	}
	public String positionKey(String account,String instrument){
		String key = account+"-"+instrument;
		return key;
	}
	
	

}
