package com.jpmc.trade.manager;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.jpmc.trade.enums.TradeDirection;
import com.jpmc.trade.enums.TradeOperation;
import com.jpmc.trade.model.AggregateTrade;
import com.jpmc.trade.model.TradeEvent;
import com.jpmc.trade.utils.Constant;
import com.trade.AppConfig;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {AppConfig.class})
public class ThreadManagerTest {

	@Autowired
	ThreadManager threadManager;
	
	@Test
	public void startPositionThread_whenTypical(){
		BlockingQueue<AggregateTrade> aggregateQueue = new LinkedBlockingQueue<AggregateTrade>();
		ThreadPoolExecutor executorService = threadManager.startPositionThread(aggregateQueue);
		Assert.assertEquals(executorService.getActiveCount(),1);
		Assert.assertEquals(executorService.getTaskCount(),1);
		AggregateTrade aggregateTrade = new AggregateTrade();
		aggregateTrade.setType(Constant.POISON);
		aggregateQueue.add(aggregateTrade);
		threadManager.awaitForThreadCompletion();
		Assert.assertEquals(executorService.getActiveCount(),0);
		Assert.assertEquals(executorService.getCompletedTaskCount(),1);
	}
	
	@Test
	public void startTradeThread_whenTypical() throws InterruptedException{
		BlockingQueue<AggregateTrade> aggregateQueue = new LinkedBlockingQueue<AggregateTrade>();
		BlockingQueue<TradeEvent> tradeQueue = new LinkedBlockingQueue<TradeEvent>();
		ThreadPoolExecutor executorService = threadManager.startTradeThread(tradeQueue, aggregateQueue);
		Assert.assertEquals(executorService.getActiveCount(),1);
		Assert.assertEquals(executorService.getTaskCount(),1);
		TradeEvent trade = new TradeEvent(2233L,1,Constant.POISON,100,"1-1",TradeDirection.BUY,TradeOperation.CANCEL);
		tradeQueue.put(trade);
		threadManager.awaitForThreadCompletion();
		Assert.assertEquals(executorService.getActiveCount(),0);
		Assert.assertEquals(executorService.getCompletedTaskCount(),1);
	}
}
