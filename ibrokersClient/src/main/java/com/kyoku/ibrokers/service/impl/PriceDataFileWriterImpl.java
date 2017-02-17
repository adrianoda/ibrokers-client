package com.kyoku.ibrokers.service.impl;

import java.io.FileWriter;
import java.io.IOException;
import java.util.Set;

import com.ib.client.Contract;
import com.kyoku.ibrokers.model.PriceData;
import com.kyoku.ibrokers.service.PriceDataFileWriter;

/**
 * Create CSV file containing price data (strike, bid, delta)
 * 
 * @author kyoku
 *
 */
public class PriceDataFileWriterImpl implements PriceDataFileWriter {
	
	private final static String COL_SEPARATOR = ";";
	private final static String ROW_SEPARATOR = "\n";
	private final static String EMPTY_PRICE = "0";

	@Override
	public void write(Contract contract, Set<PriceData> priceDataList) throws IOException {
		String fileName = contract.symbol() + contract.lastTradeDateOrContractMonth() + contract.right() + ".csv";
		FileWriter writer = new FileWriter(fileName);		
		try {
			writer.write("strike"+COL_SEPARATOR+"bid"+COL_SEPARATOR+"delta"+ROW_SEPARATOR);
			for (PriceData priceData : priceDataList) {
				String strike = priceData.getStrike() != null ? priceData.getStrike().toString() : EMPTY_PRICE;
				String bid = priceData.getBid() != null ? priceData.getBid().toString() : EMPTY_PRICE;
				String delta = priceData.getDelta() != null ? priceData.getDelta().toString() : EMPTY_PRICE;
				writer.write(strike + COL_SEPARATOR + bid + COL_SEPARATOR + delta + ROW_SEPARATOR);
			}
		} finally {
			writer.flush();
			writer.close();
		}
	}

}
