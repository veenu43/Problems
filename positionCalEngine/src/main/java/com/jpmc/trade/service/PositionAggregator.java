package com.jpmc.trade.service;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;

import com.jpmc.trade.enums.TradeDirection;
import com.jpmc.trade.enums.TradeOperation;
import com.jpmc.trade.model.AggregateTrade;
import com.jpmc.trade.model.Position;
import com.jpmc.trade.model.TradeEvent;

@Service
public class PositionAggregator {
	private static final Logger LOG = Logger.getLogger(PositionAggregator.class);
	
	public Position aggregatePosition(Position position, TradeEvent tradeEvent){
		if(position.isEmpty())	{
			createPosition(position, tradeEvent);
		}else{
			updatePosition(position, tradeEvent);
		}
		return position;
	}
	
	public Position adjustPosition(Position position, TradeEvent previousTradeEvent){
		if(!position.isEmpty())	{
			adjustPositionValue(position, previousTradeEvent);
		}
		return position;
	}
	public void createPosition(Position position, TradeEvent tradeEvent){
		position.setAccount(tradeEvent.getAccount());
		position.setInstrument(tradeEvent.getSecurityIdentifier());
		position.setQuantity(calculateTradeQuantity(tradeEvent));
		position.addTradeID(tradeEvent.getTradeID());
	}
	
	public void updatePosition(Position position,TradeEvent tradeEvent){
		position.setQuantity(calculatePositionQuantity(position,tradeEvent));
		position.addTradeID(tradeEvent.getTradeID());
	}
	public Integer calculatePositionQuantity(Position position, TradeEvent tradeEvent)
	{
		Integer calculatedTradeQuantity = calculateTradeQuantity(tradeEvent);
		Integer calculatedPositionQuantity = position.getQuantity()+calculatedTradeQuantity;
		return calculatedPositionQuantity;
	}

	public Integer adjustPositionQuantity(Position position, TradeEvent previousTradeEvent)
	{
		Integer calculatedTradeQuantity = -calculateTradeQuantity(previousTradeEvent);
		Integer calculatedPositionQuantity = position.getQuantity()+calculatedTradeQuantity;
		return calculatedPositionQuantity;
	}

	private Integer calculateTradeQuantity(TradeEvent tradeEvent) {
		Integer calculatedTradeQuantity =0;
		if(TradeDirection.BUY.equals(tradeEvent.getTradeDirection()) &&(TradeOperation.NEW.equals(tradeEvent.getTradeOperation()))){
			calculatedTradeQuantity = tradeEvent.getQuanity();
		}else if(TradeDirection.BUY.equals(tradeEvent.getTradeDirection()) &&(TradeOperation.AMEND.equals(tradeEvent.getTradeOperation()))){
			calculatedTradeQuantity = tradeEvent.getQuanity();
		}else if(TradeDirection.BUY.equals(tradeEvent.getTradeDirection()) &&(TradeOperation.CANCEL.equals(tradeEvent.getTradeOperation()))){
			calculatedTradeQuantity = tradeEvent.getQuanity();
		}else if(TradeDirection.SELL.equals(tradeEvent.getTradeDirection()) &&(TradeOperation.NEW.equals(tradeEvent.getTradeOperation()))){
			calculatedTradeQuantity = -tradeEvent.getQuanity();
		}else if(TradeDirection.SELL.equals(tradeEvent.getTradeDirection()) &&(TradeOperation.AMEND.equals(tradeEvent.getTradeOperation()))){
			calculatedTradeQuantity = -tradeEvent.getQuanity();
		}else if(TradeDirection.SELL.equals(tradeEvent.getTradeDirection()) &&(TradeOperation.CANCEL.equals(tradeEvent.getTradeOperation()))){
			calculatedTradeQuantity = tradeEvent.getQuanity();
		}
		return calculatedTradeQuantity;
	}
	
	public void adjustPositionValue(Position position, TradeEvent previousTradeEvent){
		position.setQuantity(adjustPositionQuantity(position,previousTradeEvent));
		position.addTradeID(previousTradeEvent.getTradeID());
	}

}
