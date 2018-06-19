package com.jpmc.trade.persistentQueue;

import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.jpmc.trade.model.TradeEvent;

@Service
public class PendingTradeQueue {


private Map<String,TradeEvent> queue = new HashMap();
	
	
	public void persistAggregatedTrade(TradeEvent tradeEvent){
		if(!tradeEvent.isEmpty())
		{
			queue.put(getTradeKey(tradeEvent.getTradeID(),tradeEvent.getVersion()), tradeEvent);
		}
	}
	
	public TradeEvent fetchTradeEvents(Long tradeID,Integer version){
		TradeEvent tradeEvent = queue.get(getTradeKey(tradeID,version));
		if(tradeEvent ==null){
			tradeEvent = new TradeEvent();
		}
		return tradeEvent;
	}
	
	public void removeTradeEvents(Long tradeID,Integer version){
		queue.remove(getTradeKey(tradeID,version));
	}
	
	private String getTradeKey(Long tradeID,Integer version){
		String tradekey = tradeID+"-"+version;
		return tradekey;
	}
}
