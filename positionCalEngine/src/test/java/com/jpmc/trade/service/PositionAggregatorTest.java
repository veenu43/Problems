package com.jpmc.trade.service;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.jpmc.trade.enums.TradeDirection;
import com.jpmc.trade.enums.TradeOperation;
import com.jpmc.trade.model.AggregateTrade;
import com.jpmc.trade.model.Position;
import com.jpmc.trade.model.TradeEvent;
import com.trade.AppConfig;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {AppConfig.class})
public class PositionAggregatorTest {
	private static final String ARBITRARY_INSTRUMENT = "1-1";
	private static final String ARBITRARY_INSTRUMENT_2 = "2-2";
	private static final String ARBITRARY_ACCOUNT = "Test";
	@Autowired
	PositionAggregator positionAggregator;
	@Test
	public void aggregate_whenPositionDontExist(){
		Position existingPosition = getPosition(null, null,  null, null);
		AggregateTrade aggregatedTrade = getAggregateTrade(ARBITRARY_ACCOUNT,ARBITRARY_INSTRUMENT,100,1,1L,0);
		aggregatedTrade.setLatestTradeEvent(getTradeEvent(1L, 1, ARBITRARY_INSTRUMENT, 100, ARBITRARY_ACCOUNT, TradeDirection.BUY, TradeOperation.NEW));
		aggregatedTrade.setPreviousTradeEvent(new TradeEvent());
		Position previousPosition = positionAggregator.adjustPosition(existingPosition, aggregatedTrade.getPreviousTradeEvent());
		Position position = positionAggregator.aggregatePosition(existingPosition, aggregatedTrade.getLatestTradeEvent());
		Assert.assertTrue(position.getTradeIDs().contains(aggregatedTrade.getTradeID()));
		Assert.assertEquals(position.getQuantity(), aggregatedTrade.getLatestTradeEvent().getQuanity());
		Assert.assertEquals(position.getInstrument(), aggregatedTrade.getLatestTradeEvent().getSecurityIdentifier());
		Assert.assertEquals(position.getAccount(), aggregatedTrade.getLatestTradeEvent().getAccount());
	}
	
	@Test
	public void aggregate_whenPositionExistButDiffTrade(){
		Set<Long> tradeIDs = new HashSet<Long>();
		tradeIDs.add(2L);
		Position existingPosition = getPosition(ARBITRARY_ACCOUNT,ARBITRARY_INSTRUMENT,100, tradeIDs);
		AggregateTrade aggregatedTrade = getAggregateTrade(ARBITRARY_ACCOUNT,ARBITRARY_INSTRUMENT,200,1,1L,0);
		aggregatedTrade.setLatestTradeEvent(getTradeEvent(1L, 1, ARBITRARY_INSTRUMENT, 100, ARBITRARY_ACCOUNT, TradeDirection.BUY, TradeOperation.NEW));
		aggregatedTrade.setPreviousTradeEvent(new TradeEvent());
		Position previousPosition = positionAggregator.adjustPosition(existingPosition, aggregatedTrade.getPreviousTradeEvent());
		Position position = positionAggregator.aggregatePosition(existingPosition, aggregatedTrade.getLatestTradeEvent());
		Assert.assertTrue(position.getTradeIDs().contains(aggregatedTrade.getTradeID()));
		Assert.assertEquals(position.getQuantity(), new Integer(200));
		Assert.assertEquals(position.getInstrument(), aggregatedTrade.getLatestTradeEvent().getSecurityIdentifier());
		Assert.assertEquals(position.getAccount(), aggregatedTrade.getLatestTradeEvent().getAccount());
		
	}
	
	@Test
	public void aggregate_whenTradeCancelled(){
		Set<Long> tradeIDs = new HashSet<Long>();
		tradeIDs.add(2L);
		Position existingPosition = getPosition(ARBITRARY_ACCOUNT,ARBITRARY_INSTRUMENT,300, tradeIDs);
		AggregateTrade aggregatedTrade = getAggregateTrade(ARBITRARY_ACCOUNT,ARBITRARY_INSTRUMENT,200,1,1L,0);
		aggregatedTrade.setLatestTradeEvent(getTradeEvent(1L, 2, ARBITRARY_INSTRUMENT, 0, ARBITRARY_ACCOUNT, TradeDirection.BUY, TradeOperation.CANCEL));
		aggregatedTrade.setPreviousTradeEvent(getTradeEvent(1L, 1, ARBITRARY_INSTRUMENT, 100, ARBITRARY_ACCOUNT, TradeDirection.BUY, TradeOperation.NEW));
		Position previousPosition = positionAggregator.adjustPosition(existingPosition, aggregatedTrade.getPreviousTradeEvent());
		Position position = positionAggregator.aggregatePosition(existingPosition, aggregatedTrade.getLatestTradeEvent());
		Assert.assertTrue(position.getTradeIDs().contains(aggregatedTrade.getTradeID()));
		Assert.assertEquals(position.getQuantity(), new Integer(200));
		Assert.assertEquals(position.getInstrument(), aggregatedTrade.getLatestTradeEvent().getSecurityIdentifier());
		Assert.assertEquals(position.getAccount(), aggregatedTrade.getLatestTradeEvent().getAccount());
		
	}
	
	@Test
	public void aggregate_whenTradeAmendedFromBuyToSell(){
		Set<Long> tradeIDs = new HashSet<Long>();
		tradeIDs.add(2L);
		Position existingPosition = getPosition(ARBITRARY_ACCOUNT,ARBITRARY_INSTRUMENT,300, tradeIDs);
		AggregateTrade aggregatedTrade = getAggregateTrade(ARBITRARY_ACCOUNT,ARBITRARY_INSTRUMENT,200,1,1L,0);
		aggregatedTrade.setLatestTradeEvent(getTradeEvent(1L, 2, ARBITRARY_INSTRUMENT, 100, ARBITRARY_ACCOUNT, TradeDirection.SELL, TradeOperation.AMEND));
		aggregatedTrade.setPreviousTradeEvent(getTradeEvent(1L, 1, ARBITRARY_INSTRUMENT, 100, ARBITRARY_ACCOUNT, TradeDirection.BUY, TradeOperation.NEW));
		
		Position previousPosition = positionAggregator.adjustPosition(existingPosition, aggregatedTrade.getPreviousTradeEvent());
		Assert.assertTrue(previousPosition.getTradeIDs().contains(aggregatedTrade.getPreviousTradeEvent().getTradeID()));
		Assert.assertEquals(previousPosition.getQuantity(), new Integer(200));
		Assert.assertEquals(previousPosition.getInstrument(), aggregatedTrade.getPreviousTradeEvent().getSecurityIdentifier());
		Assert.assertEquals(previousPosition.getAccount(), aggregatedTrade.getPreviousTradeEvent().getAccount());
		
		Position position = positionAggregator.aggregatePosition(existingPosition, aggregatedTrade.getLatestTradeEvent());
		Assert.assertTrue(position.getTradeIDs().contains(aggregatedTrade.getTradeID()));
		Assert.assertEquals(position.getQuantity(), new Integer(100));
		Assert.assertEquals(position.getInstrument(), aggregatedTrade.getLatestTradeEvent().getSecurityIdentifier());
		Assert.assertEquals(position.getAccount(), aggregatedTrade.getLatestTradeEvent().getAccount());
		
	}
	
	@Test
	public void aggregate_whenTradeAmendedWithDiffIntrument(){
		Set<Long> tradeIDs = new HashSet<Long>();
		tradeIDs.add(2L);
		Position existingPositionForInstrument = getPosition(ARBITRARY_ACCOUNT,ARBITRARY_INSTRUMENT,200, tradeIDs);
		Position newPositionForInstrument = getPosition(ARBITRARY_ACCOUNT,ARBITRARY_INSTRUMENT_2,400, tradeIDs);
		Position existingPosition = getPosition(ARBITRARY_ACCOUNT,ARBITRARY_INSTRUMENT,300, tradeIDs);
		AggregateTrade aggregatedTrade = getAggregateTrade(ARBITRARY_ACCOUNT,ARBITRARY_INSTRUMENT,200,1,1L,0);
		aggregatedTrade.setLatestTradeEvent(getTradeEvent(1L, 2, ARBITRARY_INSTRUMENT_2, 100, ARBITRARY_ACCOUNT, TradeDirection.SELL, TradeOperation.AMEND));
		aggregatedTrade.setPreviousTradeEvent(getTradeEvent(1L, 1, ARBITRARY_INSTRUMENT, 100, ARBITRARY_ACCOUNT, TradeDirection.BUY, TradeOperation.NEW));
		
		Position previousPosition = positionAggregator.adjustPosition(existingPositionForInstrument, aggregatedTrade.getPreviousTradeEvent());
		Assert.assertTrue(previousPosition.getTradeIDs().contains(aggregatedTrade.getPreviousTradeEvent().getTradeID()));
		Assert.assertEquals(previousPosition.getQuantity(), new Integer(100));
		Assert.assertEquals(previousPosition.getInstrument(), aggregatedTrade.getPreviousTradeEvent().getSecurityIdentifier());
		Assert.assertEquals(previousPosition.getAccount(), aggregatedTrade.getPreviousTradeEvent().getAccount());
		
		Position position = positionAggregator.aggregatePosition(newPositionForInstrument, aggregatedTrade.getLatestTradeEvent());
		Assert.assertTrue(position.getTradeIDs().contains(aggregatedTrade.getTradeID()));
		Assert.assertEquals(position.getQuantity(), new Integer(300));
		Assert.assertEquals(position.getInstrument(), aggregatedTrade.getLatestTradeEvent().getSecurityIdentifier());
		Assert.assertEquals(position.getAccount(), aggregatedTrade.getLatestTradeEvent().getAccount());
		
	}
	
	private AggregateTrade getAggregateTrade(String account,String instrument,Integer quantity,Integer version,Long tradeID,Integer previousQuantity){
		AggregateTrade aggregateTrade = new AggregateTrade();
		aggregateTrade.setTradeID(tradeID);
		return aggregateTrade;
	}
	private TradeEvent getTradeEvent(Long tradeID, Integer version, String securityIdentifier, Integer quanity, String account,
			TradeDirection tradeDirection, TradeOperation tradeOperation){
		TradeEvent tradeEvent = new TradeEvent(tradeID,version,securityIdentifier,quanity,account,tradeDirection,tradeOperation);
		return tradeEvent;
	}
	private Position getPosition(String account, String instrument, Integer quantity, Set<Long> tradeIDs){
		Position position = new Position(account, instrument,  quantity, tradeIDs);
		return position;
	}
}
