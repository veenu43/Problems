package com.jpmc.trade.service;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;

import com.jpmc.trade.enums.TradeOperation;
import com.jpmc.trade.exception.InvalidTradeOperation;
import com.jpmc.trade.exception.OutOfSynchTrade;
import com.jpmc.trade.model.AggregateTrade;
import com.jpmc.trade.model.TradeEvent;
import com.jpmc.trade.utils.Constant;

@Service
public class TradeAggregator {

	private static final Logger LOG = Logger.getLogger(TradeAggregator.class);
	public AggregateTrade aggregate(AggregateTrade aggregateTrade,TradeEvent tradeEvent) throws InvalidTradeOperation, OutOfSynchTrade{
		if(isValidSynchTrade(aggregateTrade,tradeEvent)){
			if(aggregateTrade.isEmpty()){
				createAggregatedPosition(aggregateTrade,tradeEvent);
			}else{
				updateAggregatedPosition(aggregateTrade,tradeEvent);
			}
		}else{
			LOG.info("Trade version["+tradeEvent.getVersion()+"] not in synch for trade["+tradeEvent.getTradeID()+"]");
			throw new OutOfSynchTrade("Trade version["+tradeEvent.getVersion()+"] not in synch for trade["+tradeEvent.getTradeID()+"]");
		}
		return aggregateTrade;
	}
	
	public void createAggregatedPosition(AggregateTrade aggregateTrade,TradeEvent tradeEvent) throws InvalidTradeOperation{
		isValidTrade(tradeEvent,Constant.NewTrade);
		aggregateTrade.setTradeID(tradeEvent.getTradeID());
		aggregateTrade.setLatestTradeEvent(tradeEvent);
		aggregateTrade.setPreviousTradeEvent(new TradeEvent());
	}
	
	public void updateAggregatedPosition(AggregateTrade aggregateTrade,TradeEvent tradeEvent) throws InvalidTradeOperation{
		aggregateTrade.setPreviousTradeEvent(aggregateTrade.getLatestTradeEvent());
		aggregateTrade.setLatestTradeEvent(tradeEvent);
	}
	
	public boolean isValidTrade(TradeEvent tradeEvent,String type) throws InvalidTradeOperation{
		boolean valid= true;
		if(Constant.NewTrade.equalsIgnoreCase(type))
		{
			if(!TradeOperation.NEW.equals(tradeEvent.getTradeOperation())){
				LOG.error("Invalide Trade Operation["+tradeEvent.getTradeOperation()+ "] for trade["+tradeEvent.getTradeID()+"]");
				throw new InvalidTradeOperation("Invalide Trade Operation["+tradeEvent.getTradeOperation()+ "] for trade["+tradeEvent.getTradeID()+"]");
			}
		}else{
			if(TradeOperation.CANCEL.equals(tradeEvent.getTradeOperation()) && tradeEvent.getQuanity() >0){
				LOG.error("Invalide Trade Operation["+tradeEvent.getTradeOperation()+ "] for trade["+tradeEvent.getTradeID()+"]");
				throw new InvalidTradeOperation("Invalide Trade Operation["+tradeEvent.getTradeOperation()+ "] for trade["+tradeEvent.getTradeID()+"]");
			}
		}
		return valid;
	}
	
	public boolean isValidSynchTrade(AggregateTrade aggregateTrade,TradeEvent tradeEvent) throws InvalidTradeOperation{
		boolean isValid = true;
		if(aggregateTrade.isEmpty() && tradeEvent.getVersion() >1){
			isValid = false;
		}else if(!aggregateTrade.isEmpty() && !(aggregateTrade.getLatestTradeEvent().getVersion()+1 == tradeEvent.getVersion())){
			isValid = false;
		}
		isValidTrade(tradeEvent,Constant.TRADE);
		return isValid;
	}
}
