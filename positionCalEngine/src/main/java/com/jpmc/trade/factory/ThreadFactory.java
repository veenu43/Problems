package com.jpmc.trade.factory;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.CountDownLatch;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.AutowireCapableBeanFactory;
import org.springframework.stereotype.Service;

import com.jpmc.trade.model.AggregateTrade;
import com.jpmc.trade.model.TradeEvent;
import com.jpmc.trade.threads.PositionAggregatorThread;
import com.jpmc.trade.threads.TradeAggregatorThread;

@Service
public class ThreadFactory {
	@Autowired
	AutowireCapableBeanFactory autowireCapableBeanFactory;
	
	public List<PositionAggregatorThread>  createPositionThreads(BlockingQueue<AggregateTrade> tradeQueue,CountDownLatch latch,int noOfThreads){
		List<PositionAggregatorThread> threads = new ArrayList<PositionAggregatorThread>();
		for(int count=0; count<noOfThreads; count++) 
		{ 
			threads.add(createPositionThread(tradeQueue, latch));
		}
		return threads;

	}
	
	public PositionAggregatorThread  createPositionThread(BlockingQueue<AggregateTrade> tradeQueue,CountDownLatch latch){
		PositionAggregatorThread positionThread = new PositionAggregatorThread(tradeQueue,latch);
		autowireCapableBeanFactory.autowireBean(positionThread);
		return positionThread;
	}
	
	public List<TradeAggregatorThread>  createTradeThreads(BlockingQueue<TradeEvent> tradeQueue,BlockingQueue<AggregateTrade> aggregateTradeQueue,CountDownLatch latch,int noOfThreads){
		List<TradeAggregatorThread> threads = new ArrayList<TradeAggregatorThread>();
		for(int count=0; count<noOfThreads; count++) 
		{ 
			threads.add(createTradeThread(tradeQueue,aggregateTradeQueue,latch));
		}
		return threads;

	}
	
	public TradeAggregatorThread  createTradeThread(BlockingQueue<TradeEvent> tradeQueue,BlockingQueue<AggregateTrade> aggregateTradeQueue,CountDownLatch latch){
		TradeAggregatorThread tradeThread = new TradeAggregatorThread(tradeQueue,aggregateTradeQueue,latch);
		autowireCapableBeanFactory.autowireBean(tradeThread);
		return tradeThread;
	}
	
	
}
