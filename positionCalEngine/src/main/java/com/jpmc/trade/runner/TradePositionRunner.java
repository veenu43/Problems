package com.jpmc.trade.runner;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.stereotype.Service;

import com.jpmc.trade.enums.TradeDirection;
import com.jpmc.trade.enums.TradeOperation;
import com.jpmc.trade.manager.ThreadManager;
import com.jpmc.trade.model.AggregateTrade;
import com.jpmc.trade.model.TradeEvent;
import com.jpmc.trade.utils.Constant;
import com.trade.AppConfig;

@Service
public class TradePositionRunner {
	
	@Autowired
	ThreadManager threadManager;
	
	public void initilaize() {
		AnnotationConfigApplicationContext ctx = new AnnotationConfigApplicationContext();
        ctx.register(AppConfig.class);
        ctx.refresh();
        threadManager = ctx.getBean(ThreadManager.class);
	}
	
	public void processTrades(BlockingQueue<TradeEvent> tradeEventsQueue){
		BlockingQueue<AggregateTrade> aggregateTradeQueue = new ArrayBlockingQueue<AggregateTrade>(100);
		threadManager.startTradeThread(tradeEventsQueue, aggregateTradeQueue);
		threadManager.startPositionThread(aggregateTradeQueue);
		threadManager.awaitForThreadCompletion();
	}
		

}
