package com.jpmc.trade.manager;

import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.jpmc.trade.factory.ThreadFactory;
import com.jpmc.trade.model.AggregateTrade;
import com.jpmc.trade.model.TradeEvent;
import com.jpmc.trade.threads.PositionAggregatorThread;
import com.jpmc.trade.threads.TradeAggregatorThread;

@Service
public class ThreadManager {

	@Autowired
	ThreadFactory threadFactory;
	
	int noOfPositionThreads=1;
	int noOfTradeThreads=1;
	
	CountDownLatch positionThreadslatch;
	CountDownLatch tradeThreadslatch;
	ThreadPoolExecutor positionExecutor;
	ThreadPoolExecutor tradeExecutor;
	
	public ThreadPoolExecutor startPositionThread(BlockingQueue<AggregateTrade> aggregateTradeQueue){
		positionExecutor =(ThreadPoolExecutor) Executors.newFixedThreadPool(noOfPositionThreads);
		positionThreadslatch = new CountDownLatch(noOfPositionThreads);
		List<PositionAggregatorThread> threads = threadFactory.createPositionThreads(aggregateTradeQueue, positionThreadslatch, noOfPositionThreads);
		for(PositionAggregatorThread thread : threads){
			positionExecutor.execute(thread);
		}
		return positionExecutor;
	}
	
	public ThreadPoolExecutor startTradeThread(BlockingQueue<TradeEvent> tradeEventsQueue,BlockingQueue<AggregateTrade> aggregateTradeQueue){
		tradeExecutor = (ThreadPoolExecutor)Executors.newFixedThreadPool(noOfTradeThreads);
		tradeThreadslatch = new CountDownLatch(noOfTradeThreads);
		List<TradeAggregatorThread> threads = threadFactory.createTradeThreads(tradeEventsQueue, aggregateTradeQueue,tradeThreadslatch, noOfTradeThreads);
		for(TradeAggregatorThread thread : threads){
			tradeExecutor.execute(thread);
		}
		return tradeExecutor;
	}
	
	public void awaitForThreadCompletion(){
		try {
			if(positionThreadslatch !=null)
				positionThreadslatch.await();
			
			if(tradeThreadslatch !=null)
				tradeThreadslatch.await();
			
			Thread.sleep(100);
			if(tradeExecutor !=null)
			{
				tradeExecutor.shutdown();
				tradeExecutor.awaitTermination(10, TimeUnit.SECONDS);
			}
			
			if(positionExecutor !=null)
			{
				positionExecutor.shutdown();
				positionExecutor.awaitTermination(10, TimeUnit.SECONDS);
			}
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}

}
