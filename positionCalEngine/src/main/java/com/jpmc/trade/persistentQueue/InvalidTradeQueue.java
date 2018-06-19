package com.jpmc.trade.persistentQueue;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

import com.jpmc.trade.model.TradeEvent;

@Service
public class InvalidTradeQueue {

private List<TradeEvent> invalidTradeList = new ArrayList<TradeEvent>();
	
	
	public void persistTradeEvent(TradeEvent tradeEvent){
		if(tradeEvent.isEmpty())
		{
			invalidTradeList.add(tradeEvent);
		}
	}

}
