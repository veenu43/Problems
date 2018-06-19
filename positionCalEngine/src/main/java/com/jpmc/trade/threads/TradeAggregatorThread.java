package com.jpmc.trade.threads;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.CountDownLatch;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import com.jpmc.trade.model.AggregateTrade;
import com.jpmc.trade.model.TradeEvent;
import com.jpmc.trade.service.TradeAggregateService;
import com.jpmc.trade.utils.Constant;

public class TradeAggregatorThread extends Thread{
	private static final Logger LOG = Logger.getLogger(TradeAggregatorThread.class);
	@Autowired
	TradeAggregateService tradeAggregateService;
	
	
	BlockingQueue<TradeEvent> tradeQueue;
	BlockingQueue<AggregateTrade> aggregateTradeQueue;
	CountDownLatch latch;
	
	public TradeAggregatorThread(BlockingQueue<TradeEvent> tradeQueue,BlockingQueue<AggregateTrade> aggregateTradeQueue,CountDownLatch countDownLatch) {
		super();
		this.tradeQueue = tradeQueue;
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
					TradeEvent tradeEvent = tradeQueue.take();
					if(Constant.POISON.equalsIgnoreCase(tradeEvent.getSecurityIdentifier())){
						terminateThread = true;
						LOG.info("Trade Thread got POISON value.");
						AggregateTrade aggregateTrade = new AggregateTrade();
						aggregateTrade.setType(Constant.POISON);
						aggregateTradeQueue.add(aggregateTrade);
						break;
					}
					AggregateTrade aggregateTrade = tradeAggregateService.processTrade(tradeEvent);
					aggregateTradeQueue.add(aggregateTrade);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}finally {
			latch.countDown();
		}
	}

	public TradeAggregateService getTradeAggregateService() {
		return tradeAggregateService;
	}

	public BlockingQueue<TradeEvent> getTradeQueue() {
		return tradeQueue;
	}

	public BlockingQueue<AggregateTrade> getAggregateTradeQueue() {
		return aggregateTradeQueue;
	}

	public CountDownLatch getLatch() {
		return latch;
	}

}
