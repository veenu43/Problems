package com.jpmc.trade.threads;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.flash;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.CountDownLatch;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import com.jpmc.trade.model.AggregateTrade;
import com.jpmc.trade.service.PositionAggregateService;
import com.jpmc.trade.utils.Constant;

public class PositionAggregatorThread extends Thread{
	private static final Logger LOG = Logger.getLogger(PositionAggregatorThread.class);
	@Autowired
	PositionAggregateService positionAggregateService;
	
	BlockingQueue<AggregateTrade> aggregateTradeQueue;
	CountDownLatch latch;
	
	public PositionAggregatorThread(BlockingQueue<AggregateTrade> aggregateTradeQueue,CountDownLatch countDownLatch) {
		super();
		this.aggregateTradeQueue = aggregateTradeQueue;
		this.latch = countDownLatch;
	}

	@Override
	public void run(){
		boolean terminateThread = false;
		try
		{
			while(!terminateThread)
			{
				try {
					AggregateTrade aggregateTrade = aggregateTradeQueue.take();
					if(Constant.POISON.equalsIgnoreCase(aggregateTrade.getType())){
						terminateThread = true;
						LOG.info("Trade Thread got POISON value.");
						positionAggregateService.flushAllPositions();
						break;
					}
					positionAggregateService.processAggregatedTrade(aggregateTrade);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}finally {
			latch.countDown();
		}
			
	}

	public PositionAggregateService getPositionAggregateService() {
		return positionAggregateService;
	}

	public BlockingQueue<AggregateTrade> getAggregateTradeQueue() {
		return aggregateTradeQueue;
	}

	public CountDownLatch getLatch() {
		return latch;
	}

}
