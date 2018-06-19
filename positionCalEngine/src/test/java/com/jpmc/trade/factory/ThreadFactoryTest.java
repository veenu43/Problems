package com.jpmc.trade.factory;

import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.LinkedBlockingQueue;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.jpmc.trade.model.AggregateTrade;
import com.jpmc.trade.model.TradeEvent;
import com.jpmc.trade.threads.PositionAggregatorThread;
import com.jpmc.trade.threads.TradeAggregatorThread;
import com.trade.AppConfig;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {AppConfig.class})
public class ThreadFactoryTest {

	private static final int noOfThreads = 3;
	@Autowired
	ThreadFactory threadFactory;
	
	@Test
	public void createPositionThreads_whenTypical(){
		BlockingQueue<AggregateTrade> aggregateQueue = new LinkedBlockingQueue<AggregateTrade>();
		CountDownLatch latch = new CountDownLatch(noOfThreads);
		List<PositionAggregatorThread> positionThreads = threadFactory.createPositionThreads(aggregateQueue, latch,noOfThreads);
		Assert.assertEquals(positionThreads.size(), noOfThreads);
		Assert.assertEquals(positionThreads.get(0).getLatch().getCount(), noOfThreads);
		Assert.assertTrue(positionThreads.get(0).getAggregateTradeQueue() !=null);
		Assert.assertTrue(positionThreads.get(0).getPositionAggregateService() !=null);
	}
	
	@Test
	public void createTradeThreads_whenTypical(){
		BlockingQueue<AggregateTrade> aggregateQueue = new LinkedBlockingQueue<AggregateTrade>();
		BlockingQueue<TradeEvent> tradeQueue = new LinkedBlockingQueue<TradeEvent>();
		CountDownLatch latch = new CountDownLatch(noOfThreads);
		List<TradeAggregatorThread> tradeThreads = threadFactory.createTradeThreads(tradeQueue, aggregateQueue, latch,noOfThreads);
		Assert.assertEquals(tradeThreads.size(), noOfThreads);
		Assert.assertEquals(tradeThreads.get(0).getLatch().getCount(), noOfThreads);
		Assert.assertTrue(tradeThreads.get(0).getAggregateTradeQueue() !=null);
		Assert.assertTrue(tradeThreads.get(0).getTradeQueue() !=null);
		Assert.assertTrue(tradeThreads.get(0).getTradeAggregateService() !=null);
	}
}
