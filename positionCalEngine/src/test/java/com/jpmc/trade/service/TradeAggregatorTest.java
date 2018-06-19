package com.jpmc.trade.service;


import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.jpmc.trade.enums.TradeDirection;
import com.jpmc.trade.enums.TradeOperation;
import com.jpmc.trade.exception.InvalidTradeOperation;
import com.jpmc.trade.exception.OutOfSynchTrade;
import com.jpmc.trade.model.AggregateTrade;
import com.jpmc.trade.model.TradeEvent;
import com.trade.AppConfig;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {AppConfig.class})
public class TradeAggregatorTest {

	private static final String ARBITRARY_INSTRUMENT = "1-1";
	private static final String ARBITRARY_ACCOUNT = "Test";
	@Autowired
	TradeAggregator tradeAggregator;

	@Test
	public void aggregate_whenAggregateTradeDontExist() throws InvalidTradeOperation, OutOfSynchTrade{
		TradeEvent tradeEvent = getTradeEvent(1L,1,ARBITRARY_ACCOUNT,100,ARBITRARY_INSTRUMENT,TradeDirection.BUY,TradeOperation.NEW);
		AggregateTrade trade = tradeAggregator.aggregate(getAggregateTrade(null,null,null,null,null), tradeEvent);
		Assert.assertEquals(trade.getTradeID(), tradeEvent.getTradeID());
		Assert.assertEquals(trade.getLatestTradeEvent().getQuanity(), tradeEvent.getQuanity());
		Assert.assertEquals(trade.getLatestTradeEvent().getVersion(), tradeEvent.getVersion());
		Assert.assertEquals(trade.getLatestTradeEvent().getAccount(), tradeEvent.getAccount());
		Assert.assertEquals(trade.getLatestTradeEvent().getSecurityIdentifier(), tradeEvent.getSecurityIdentifier());
	}
	
	@Test(expected = InvalidTradeOperation.class) 
	public void aggregate_whenInvalidTrade() throws InvalidTradeOperation, OutOfSynchTrade{
		 TradeEvent tradeEvent = getTradeEvent(1L,1,ARBITRARY_ACCOUNT,100,ARBITRARY_INSTRUMENT,TradeDirection.BUY,TradeOperation.AMEND);
		AggregateTrade trade = tradeAggregator.aggregate(getAggregateTrade(null,null,null,null,null), tradeEvent);
	}
	@Test(expected = InvalidTradeOperation.class) 
	public void aggregate_whenInvalidTradeOfCancel() throws InvalidTradeOperation, OutOfSynchTrade{
		 TradeEvent tradeEvent = getTradeEvent(1L,1,ARBITRARY_ACCOUNT,100,ARBITRARY_INSTRUMENT,TradeDirection.BUY,TradeOperation.CANCEL);
		AggregateTrade trade = tradeAggregator.aggregate(getAggregateTrade(null,null,null,null,null), tradeEvent);
	}
	@Test
	public void aggregate_whenAggregateTradeExist() throws InvalidTradeOperation, OutOfSynchTrade{
		TradeEvent tradeEvent = getTradeEvent(1L,2,ARBITRARY_ACCOUNT,200,ARBITRARY_INSTRUMENT,TradeDirection.BUY,TradeOperation.AMEND);
		TradeEvent previousTradeEvent = getTradeEvent(1L,1,ARBITRARY_ACCOUNT,100,ARBITRARY_INSTRUMENT,TradeDirection.BUY,TradeOperation.NEW);
		AggregateTrade existingAggregateTrade = getAggregateTrade(ARBITRARY_INSTRUMENT,ARBITRARY_ACCOUNT,100,1,1L);
		existingAggregateTrade.setLatestTradeEvent(previousTradeEvent);
		existingAggregateTrade.setPreviousTradeEvent(new TradeEvent());
		AggregateTrade trade = tradeAggregator.aggregate(existingAggregateTrade,tradeEvent);
		Assert.assertEquals(trade.getLatestTradeEvent().getTradeID(), tradeEvent.getTradeID());
		Assert.assertEquals(trade.getLatestTradeEvent().getQuanity(), tradeEvent.getQuanity());
		Assert.assertEquals(trade.getPreviousTradeEvent().getQuanity(),new Integer(100));
		Assert.assertEquals(trade.getLatestTradeEvent().getVersion(), tradeEvent.getVersion());
		Assert.assertEquals(trade.getLatestTradeEvent().getAccount(), tradeEvent.getAccount());
		Assert.assertEquals(trade.getLatestTradeEvent().getSecurityIdentifier(), tradeEvent.getSecurityIdentifier());
	}
	@Test
	public void aggregate_whenTradeCancelled() throws InvalidTradeOperation, OutOfSynchTrade{
		TradeEvent tradeEvent = getTradeEvent(1L,2,ARBITRARY_ACCOUNT,0,ARBITRARY_INSTRUMENT,TradeDirection.BUY,TradeOperation.CANCEL);
		AggregateTrade existingAggregateTrade = getAggregateTrade(ARBITRARY_INSTRUMENT,ARBITRARY_ACCOUNT,100,1,1L);
		TradeEvent previousTradeEvent = getTradeEvent(1L,1,ARBITRARY_ACCOUNT,100,ARBITRARY_INSTRUMENT,TradeDirection.BUY,TradeOperation.NEW);
		existingAggregateTrade.setLatestTradeEvent(previousTradeEvent);
		existingAggregateTrade.setPreviousTradeEvent(new TradeEvent());
		AggregateTrade trade = tradeAggregator.aggregate(existingAggregateTrade,tradeEvent);
		Assert.assertEquals(trade.getTradeID(), tradeEvent.getTradeID());
		Assert.assertEquals(trade.getLatestTradeEvent().getQuanity(), new Integer(0));
		Assert.assertEquals(trade.getPreviousTradeEvent().getQuanity(),previousTradeEvent.getQuanity());
		Assert.assertEquals(trade.getPreviousTradeEvent().getVersion(),previousTradeEvent.getVersion());
		Assert.assertEquals(trade.getLatestTradeEvent().getVersion(), tradeEvent.getVersion());
		Assert.assertEquals(trade.getLatestTradeEvent().getAccount(), tradeEvent.getAccount());
		Assert.assertEquals(trade.getLatestTradeEvent().getSecurityIdentifier(), tradeEvent.getSecurityIdentifier());
	}
	
	@Test(expected = OutOfSynchTrade.class) 
	public void aggregate_whenTradeNotInSynch() throws InvalidTradeOperation, OutOfSynchTrade{
		TradeEvent tradeEvent = getTradeEvent(1L,3,ARBITRARY_ACCOUNT,100,ARBITRARY_INSTRUMENT,TradeDirection.BUY,TradeOperation.AMEND);
		AggregateTrade existingAggregateTrade = getAggregateTrade(ARBITRARY_INSTRUMENT,ARBITRARY_ACCOUNT,100,1,1L);
		TradeEvent previousTradeEvent = getTradeEvent(1L,1,ARBITRARY_ACCOUNT,100,ARBITRARY_INSTRUMENT,TradeDirection.BUY,TradeOperation.NEW);
		existingAggregateTrade.setLatestTradeEvent(previousTradeEvent);
		existingAggregateTrade.setPreviousTradeEvent(new TradeEvent());
		AggregateTrade trade = tradeAggregator.aggregate(existingAggregateTrade,tradeEvent);
		Assert.assertEquals(trade.getTradeID(), tradeEvent.getTradeID());
		Assert.assertEquals(trade.getLatestTradeEvent().getQuanity(), tradeEvent.getQuanity());
		Assert.assertEquals(trade.getPreviousTradeEvent().getQuanity(),new Integer(100));
		Assert.assertEquals(trade.getLatestTradeEvent().getVersion(), tradeEvent.getVersion());
		Assert.assertEquals(trade.getLatestTradeEvent().getAccount(), tradeEvent.getAccount());
		Assert.assertEquals(trade.getLatestTradeEvent().getSecurityIdentifier(), tradeEvent.getSecurityIdentifier());
	}
	
	private AggregateTrade getAggregateTrade(String account,String instrument,Integer quantity,Integer version,Long tradeID){
		AggregateTrade aggregateTrade = new AggregateTrade();
		aggregateTrade.setTradeID(tradeID);
		return aggregateTrade;
	}
	
	private TradeEvent getTradeEvent(Long tradeID, Integer version, String securityIdentifier, Integer quanity, String account,
			TradeDirection tradeDirection, TradeOperation tradeOperation){
		TradeEvent tradeEvent = new TradeEvent(tradeID,version,securityIdentifier,quanity,account,tradeDirection,tradeOperation);
		return tradeEvent;
	}
}
