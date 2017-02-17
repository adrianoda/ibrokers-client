package com.kyoku.ibrokers.service;

import java.io.IOException;
import java.util.Set;

import com.ib.client.Contract;
import com.kyoku.ibrokers.model.PriceData;

/**
 * Service to create price data file
 * @author kyoku
 *
 */
public interface PriceDataFileWriter {

	/**
	 * Write price data to file system
	 * @param contract
	 * @param priceDataList
	 * @throws IOException
	 */
	void write(Contract contract, Set<PriceData> priceDataList) throws IOException;

}
