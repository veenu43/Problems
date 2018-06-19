package com.jpmc.trade.model;

import com.jpmc.trade.enums.TradeDirection;
import com.jpmc.trade.enums.TradeOperation;

public class TradeEvent implements Cloneable{
	private Long tradeID;
	Integer version;
	String securityIdentifier;
	Integer quanity;
	String account;
	TradeDirection tradeDirection;
	TradeOperation tradeOperation;
	
	public boolean isEmpty(){
		return tradeID == null;
	}
	
	@Override
	public TradeEvent clone(){
		TradeEvent tradeEvent = new TradeEvent();
		tradeEvent.setAccount(this.account);
		tradeEvent.setQuanity(this.quanity);
		tradeEvent.setSecurityIdentifier(this.securityIdentifier);
		tradeEvent.setTradeID(this.tradeID);
		tradeEvent.setVersion(this.version);
		tradeEvent.setTradeDirection(this.tradeDirection);
		tradeEvent.setTradeOperation(this.tradeOperation);
		return tradeEvent;
	}
	
	
	public TradeEvent() {
		super();
		// TODO Auto-generated constructor stub
	}


	public TradeEvent(Long tradeID, Integer version, String securityIdentifier, Integer quanity, String account,
			TradeDirection tradeDirection, TradeOperation tradeOperation) {
		super();
		this.tradeID = tradeID;
		this.version = version;
		this.securityIdentifier = securityIdentifier;
		this.quanity = quanity;
		this.account = account;
		this.tradeDirection = tradeDirection;
		this.tradeOperation = tradeOperation;
	}


	public Long getTradeID() {
		return tradeID;
	}
	public void setTradeID(Long tradeID) {
		this.tradeID = tradeID;
	}
	public Integer getVersion() {
		return version;
	}
	public void setVersion(Integer version) {
		this.version = version;
	}
	public String getSecurityIdentifier() {
		return securityIdentifier;
	}
	public void setSecurityIdentifier(String securityIdentifier) {
		this.securityIdentifier = securityIdentifier;
	}
	public Integer getQuanity() {
		return quanity;
	}
	public void setQuanity(Integer quanity) {
		this.quanity = quanity;
	}
	public String getAccount() {
		return account;
	}
	public void setAccount(String account) {
		this.account = account;
	}
	public TradeDirection getTradeDirection() {
		return tradeDirection;
	}
	public void setTradeDirection(TradeDirection tradeDirection) {
		this.tradeDirection = tradeDirection;
	}
	public TradeOperation getTradeOperation() {
		return tradeOperation;
	}
	public void setTradeOperation(TradeOperation tradeOperation) {
		this.tradeOperation = tradeOperation;
	}
	
	
}
