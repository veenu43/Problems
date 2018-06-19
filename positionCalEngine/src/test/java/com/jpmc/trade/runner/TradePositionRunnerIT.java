package com.jpmc.trade.runner;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.jpmc.trade.enums.TradeDirection;
import com.jpmc.trade.enums.TradeOperation;
import com.jpmc.trade.model.TradeEvent;
import com.jpmc.trade.utils.Constant;
import com.trade.AppConfig;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {AppConfig.class})
public class TradePositionRunnerIT {
	
	@Autowired
	TradePositionRunner runner;
	
	@Test
	public void processSampleTrades() throws InterruptedException{
		runner.processTrades(createTradeEventsSample());
	}
	
	@Test
	public void processTrades() throws InterruptedException{
		runner.processTrades(createTradeEventsData());
	}
	
	@Test
	public void processTradesOutOfSynch() throws InterruptedException{
		runner.processTrades(createTradeEventsOutOfSynch());
	}
	
	private BlockingQueue<TradeEvent> createTradeEventsSample() throws InterruptedException{
		BlockingQueue<TradeEvent> tradeEventsQueue = new LinkedBlockingQueue<TradeEvent>();
		TradeEvent trade1 = new TradeEvent(1L,1,"Test",100,"1-1",TradeDirection.BUY,TradeOperation.NEW);
		tradeEventsQueue.put(trade1);
		TradeEvent trade2 = new TradeEvent(1L,2,"Test",150,"1-1",TradeDirection.BUY,TradeOperation.AMEND);
		tradeEventsQueue.put(trade2);
		TradeEvent trade3 = new TradeEvent(1L,3,"Test",200,"1-1",TradeDirection.BUY,TradeOperation.AMEND);
		tradeEventsQueue.put(trade3);
		TradeEvent trade4 = new TradeEvent(1L,4,"Test",100,"1-1",TradeDirection.BUY,TradeOperation.CANCEL);
		tradeEventsQueue.put(trade4);
		TradeEvent trade5 = new TradeEvent(1L,5,Constant.POISON,100,"1-1",TradeDirection.BUY,TradeOperation.CANCEL);
		tradeEventsQueue.put(trade5);
		return tradeEventsQueue;
	}
	
	private BlockingQueue<TradeEvent> createTradeEventsOutOfSynch() throws InterruptedException{
		BlockingQueue<TradeEvent> tradeEventsQueue = new LinkedBlockingQueue<TradeEvent>();
		TradeEvent trade6 = new TradeEvent(2233L,2,"RET",400,"ACC-3456",TradeDirection.SELL,TradeOperation.AMEND);
		tradeEventsQueue.put(trade6);
		TradeEvent trade7 = new TradeEvent(2233L,3,"RET",0,"ACC-3456",TradeDirection.SELL,TradeOperation.CANCEL);
		tradeEventsQueue.put(trade7);
		TradeEvent trade5 = new TradeEvent(2233L,1,"RET",100,"ACC-3456",TradeDirection.SELL,TradeOperation.NEW);
		tradeEventsQueue.put(trade5);
		TradeEvent trade28 = new TradeEvent(2233L,1,Constant.POISON,100,"1-1",TradeDirection.BUY,TradeOperation.CANCEL);
		tradeEventsQueue.put(trade28);
		return tradeEventsQueue;
	}
	private BlockingQueue<TradeEvent> createTradeEventsData() throws InterruptedException{
		BlockingQueue<TradeEvent> tradeEventsQueue = new LinkedBlockingQueue<TradeEvent>();
		TradeEvent trade1 = new TradeEvent(1234L,1,"XYZ",100,"ACC-1234",TradeDirection.BUY,TradeOperation.NEW);
		tradeEventsQueue.put(trade1);
		TradeEvent trade2 = new TradeEvent(1234L,2,"XYZ",150,"ACC-1234",TradeDirection.BUY,TradeOperation.AMEND);
		tradeEventsQueue.put(trade2);
		
		TradeEvent trade3 = new TradeEvent(5678L,1,"QED",200,"ACC-2345",TradeDirection.BUY,TradeOperation.NEW);
		tradeEventsQueue.put(trade3);
		TradeEvent trade4 = new TradeEvent(5678L,2,"QED",0,"ACC-2345",TradeDirection.BUY,TradeOperation.CANCEL);
		tradeEventsQueue.put(trade4);
		
		TradeEvent trade6 = new TradeEvent(2233L,2,"RET",400,"ACC-3456",TradeDirection.SELL,TradeOperation.AMEND);
		tradeEventsQueue.put(trade6);
		TradeEvent trade7 = new TradeEvent(2233L,3,"RET",0,"ACC-3456",TradeDirection.SELL,TradeOperation.CANCEL);
		tradeEventsQueue.put(trade7);
		
		TradeEvent trade8 = new TradeEvent(8896L,1,"YUI",300,"ACC-4567",TradeDirection.BUY,TradeOperation.NEW);
		tradeEventsQueue.put(trade8);
		TradeEvent trade9 = new TradeEvent(6638L,1,"YUI",100,"ACC-4567",TradeDirection.SELL,TradeOperation.NEW);
		tradeEventsQueue.put(trade9);
		
		TradeEvent trade10 = new TradeEvent(6363L,1,"HJK",200,"ACC-5678",TradeDirection.BUY,TradeOperation.NEW);
		tradeEventsQueue.put(trade10);
		TradeEvent trade11 = new TradeEvent(7666L,1,"HJK",200,"ACC-5678",TradeDirection.BUY,TradeOperation.NEW);
		tradeEventsQueue.put(trade11);
		TradeEvent trade12 = new TradeEvent(6363L,2,"HJK",100,"ACC-5678",TradeDirection.BUY,TradeOperation.AMEND);
		tradeEventsQueue.put(trade12);
		TradeEvent trade13 = new TradeEvent(7666L,2,"HJK",50,"ACC-5678",TradeDirection.SELL,TradeOperation.AMEND);
		tradeEventsQueue.put(trade13);
		
		TradeEvent trade14 = new TradeEvent(8686L,1,"FVB",100,"ACC-6789",TradeDirection.BUY,TradeOperation.NEW);
		tradeEventsQueue.put(trade14);
		TradeEvent trade15 = new TradeEvent(8686L,2,"GBN",100,"ACC-6789",TradeDirection.BUY,TradeOperation.AMEND);
		tradeEventsQueue.put(trade15);
		TradeEvent trade16 = new TradeEvent(9654L,1,"FVB",200,"ACC-6789",TradeDirection.BUY,TradeOperation.NEW);
		tradeEventsQueue.put(trade16);
		
		TradeEvent trade17 = new TradeEvent(1025L,1,"JKL",100,"ACC-7789",TradeDirection.BUY,TradeOperation.NEW);
		tradeEventsQueue.put(trade17);
		TradeEvent trade18 = new TradeEvent(1036L,1,"JKL",100,"ACC-7789",TradeDirection.BUY,TradeOperation.NEW);
		tradeEventsQueue.put(trade18);
		TradeEvent trade19 = new TradeEvent(1025L,2,"JKL",100,"ACC-8877",TradeDirection.SELL,TradeOperation.AMEND);
		tradeEventsQueue.put(trade19);
		
		TradeEvent trade20 = new TradeEvent(1122L,1,"KLO",100,"ACC-9045",TradeDirection.BUY,TradeOperation.NEW);
		tradeEventsQueue.put(trade20);
		TradeEvent trade21 = new TradeEvent(1122L,2,"HJK",100,"ACC-9045",TradeDirection.SELL,TradeOperation.AMEND);
		tradeEventsQueue.put(trade21);
		TradeEvent trade22 = new TradeEvent(1122L,3,"KLO",100,"ACC-9045",TradeDirection.SELL,TradeOperation.AMEND);
		tradeEventsQueue.put(trade22);
		
		TradeEvent trade23 = new TradeEvent(1144L,1,"KLO",300,"ACC-9045",TradeDirection.BUY,TradeOperation.NEW);
		tradeEventsQueue.put(trade23);
		TradeEvent trade24= new TradeEvent(1144L,2,"KLO",400,"ACC-9045",TradeDirection.BUY,TradeOperation.AMEND);
		tradeEventsQueue.put(trade24);
		
		TradeEvent trade25= new TradeEvent(1155L,1,"KLO",600,"ACC-9045",TradeDirection.SELL,TradeOperation.NEW);
		tradeEventsQueue.put(trade25);
		TradeEvent trade26= new TradeEvent(1155L,2,"KLO",0,"ACC-9045",TradeDirection.BUY,TradeOperation.CANCEL);
		tradeEventsQueue.put(trade26);
		TradeEvent trade5 = new TradeEvent(2233L,1,"RET",100,"ACC-3456",TradeDirection.SELL,TradeOperation.NEW);
		tradeEventsQueue.put(trade5);
		TradeEvent trade28 = new TradeEvent(2233L,1,Constant.POISON,100,"1-1",TradeDirection.BUY,TradeOperation.CANCEL);
		tradeEventsQueue.put(trade28);
		return tradeEventsQueue;
	}
	
}
