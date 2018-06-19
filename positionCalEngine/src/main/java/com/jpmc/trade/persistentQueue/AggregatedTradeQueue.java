package com.jpmc.trade.persistentQueue;

import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.jpmc.trade.model.AggregateTrade;

@Service
public class AggregatedTradeQueue {
	
	private Map<Long,AggregateTrade> queue = new HashMap();
	
	
	public void persistAggregatedTrade(AggregateTrade aggregatedTrade){
		if(!aggregatedTrade.isEmpty())
		{
			queue.put(aggregatedTrade.getTradeID(), aggregatedTrade);
		}
	}
	
	public AggregateTrade fetchAggregatedTrade(Long tradeID){
		AggregateTrade aggregatedTrade = queue.get(tradeID);
		if(aggregatedTrade ==null){
			aggregatedTrade = new AggregateTrade();
		}
		return aggregatedTrade.clone();
	}

}
