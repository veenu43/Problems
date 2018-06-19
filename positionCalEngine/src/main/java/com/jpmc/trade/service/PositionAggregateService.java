package com.jpmc.trade.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.jpmc.trade. persistentQueue.PositionQueue;
import com.jpmc.trade.model.AggregateTrade;
import com.jpmc.trade.model.Position;

@Service
public class PositionAggregateService {

	@Autowired
	PositionQueue positionQueue;
	
	@Autowired
	PositionAggregator positionAggregator;
	
	
	public Position processAggregatedTrade(AggregateTrade aggregateTrade){
		
		if(!aggregateTrade.getPreviousTradeEvent().isEmpty())
		{
			Position previousPosition = positionQueue.fetchPosition(aggregateTrade.getPreviousTradeEvent().getAccount(), aggregateTrade.getPreviousTradeEvent().getSecurityIdentifier());
			positionAggregator.adjustPosition(previousPosition, aggregateTrade.getPreviousTradeEvent());
			positionQueue.persistPosition(previousPosition);
		}
		Position position=null;
		if(!aggregateTrade.getLatestTradeEvent().isEmpty())
		{
			position = positionQueue.fetchPosition(aggregateTrade.getLatestTradeEvent().getAccount(), aggregateTrade.getLatestTradeEvent().getSecurityIdentifier());
			positionAggregator.aggregatePosition(position, aggregateTrade.getLatestTradeEvent());
			positionQueue.persistPosition(position);
		}
		return position;
	}
	
	public void flushAllPositions(){
		positionQueue.flushAllPositions();
	}
}
