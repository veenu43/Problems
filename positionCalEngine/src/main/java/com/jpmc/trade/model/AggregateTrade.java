package com.jpmc.trade.model;

import com.jpmc.trade.utils.Constant;

public class AggregateTrade implements Cloneable{
	Long tradeID;
	String type = Constant.TRADE;
	TradeEvent latestTradeEvent = new TradeEvent();
	TradeEvent previousTradeEvent  = new TradeEvent();
	
	@Override
	public AggregateTrade clone(){
		AggregateTrade aggregateTrade = new AggregateTrade();
		aggregateTrade.setTradeID(this.tradeID);
		aggregateTrade.setLatestTradeEvent(this.latestTradeEvent.clone());
		aggregateTrade.setPreviousTradeEvent(this.previousTradeEvent.clone());
		return aggregateTrade;
	}
	public boolean isEmpty(){
		boolean isEmpty = false;
		if(tradeID==null  || tradeID ==0){
			isEmpty = true;
		}
		return isEmpty;
	}
	
	public Long getTradeID() {
		return tradeID;
	}
	public void setTradeID(Long tradeID) {
		this.tradeID = tradeID;
	}
	
	public TradeEvent getLatestTradeEvent() {
		return latestTradeEvent;
	}
	public void setLatestTradeEvent(TradeEvent latestTradeEvent) {
		this.latestTradeEvent = latestTradeEvent;
	}
	public TradeEvent getPreviousTradeEvent() {
		return previousTradeEvent;
	}
	public void setPreviousTradeEvent(TradeEvent previousTradeEvent) {
		this.previousTradeEvent = previousTradeEvent;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	
	
	
	
}
