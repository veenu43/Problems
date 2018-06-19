package com.jpmc.trade.model;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class Position {
	
	String account;
	String instrument;
	Integer quantity;
	Set<Long> tradeIDs = new HashSet<Long>();
	
	public Position(String account, String instrument, Integer quantity, Set<Long> tradeIDs) {
		super();
		this.account = account;
		this.instrument = instrument;
		this.quantity = quantity;
		this.tradeIDs = tradeIDs;
	}

	public Position() {
		super();
		// TODO Auto-generated constructor stub
	}

	public boolean isEmpty(){
		boolean isEmpty = false;
		if(instrument ==null || account == null){
			isEmpty = true;
		}
		return isEmpty;
	}
	
	public String getPositionKey(){
		String key = account+"-"+instrument;
		return key;
	}
	public String getAccount() {
		return account;
	}
	public void setAccount(String account) {
		this.account = account;
	}
	public String getInstrument() {
		return instrument;
	}
	public void setInstrument(String instrument) {
		this.instrument = instrument;
	}
	public Integer getQuantity() {
		return quantity;
	}
	public void setQuantity(Integer quantity) {
		this.quantity = quantity;
	}
	public Set<Long> getTradeIDs() {
		return tradeIDs;
	}
	public void addTradeID(Long tradeID) {
		if (tradeIDs == null){
			tradeIDs = new HashSet<Long>();
		}
		tradeIDs.add(tradeID);
	}

	@Override
	public String toString() {
		return "Position [account=" + account + ", instrument=" + instrument + ", quantity=" + quantity + ", tradeIDs="
				+ tradeIDs + "]";
	}

}
