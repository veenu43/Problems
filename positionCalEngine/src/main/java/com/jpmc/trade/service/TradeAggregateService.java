package com.jpmc.trade.service;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.jpmc.trade. persistentQueue.AggregatedTradeQueue;
import com.jpmc.trade. persistentQueue.InvalidTradeQueue;
import com.jpmc.trade. persistentQueue.PendingTradeQueue;
import com.jpmc.trade.threads.TradeAggregatorThread;
import com.jpmc.trade.exception.InvalidTradeOperation;
import com.jpmc.trade.exception.OutOfSynchTrade;
import com.jpmc.trade.model.AggregateTrade;
import com.jpmc.trade.model.TradeEvent;

@Service
public class TradeAggregateService {
	private static final Logger LOG = Logger.getLogger(TradeAggregatorThread.class);
	
	@Autowired
	AggregatedTradeQueue aggregatedTradeQueue;
	
	@Autowired
	TradeAggregator tradeAggregator;
	
	@Autowired
	PendingTradeQueue pendingTradeQueue;
	
	@Autowired
	InvalidTradeQueue invalidTradeQueue;
	
	public AggregateTrade processTrade(TradeEvent tradeEvent){
		AggregateTrade aggregateTrade = aggregatedTradeQueue.fetchAggregatedTrade(tradeEvent.getTradeID());
		aggregateTrade(tradeEvent, aggregateTrade);
		processPendingTrade(aggregateTrade);
		aggregatedTradeQueue.persistAggregatedTrade(aggregateTrade);
		return aggregateTrade;
	}

	public void aggregateTrade(TradeEvent tradeEvent, AggregateTrade aggregateTrade) {
		try {
			tradeAggregator.aggregate(aggregateTrade, tradeEvent);
		} catch (InvalidTradeOperation e) {
			invalidTradeQueue.persistTradeEvent(tradeEvent);
		} catch (OutOfSynchTrade e) {
			pendingTradeQueue.persistAggregatedTrade(tradeEvent);
		}
	}
	
	public void processPendingTrade(AggregateTrade aggregateTrade){
		if(!aggregateTrade.isEmpty())
		{
			Integer newVersion = aggregateTrade.getLatestTradeEvent().getVersion()+1;
			TradeEvent tradeEvent = pendingTradeQueue.fetchTradeEvents(aggregateTrade.getTradeID(), newVersion);
			if(!tradeEvent.isEmpty())
			{
				aggregateTrade(tradeEvent, aggregateTrade);
				pendingTradeQueue.removeTradeEvents(tradeEvent.getTradeID(), tradeEvent.getVersion());
				processPendingTrade(aggregateTrade);
			}
		}
	}
}
