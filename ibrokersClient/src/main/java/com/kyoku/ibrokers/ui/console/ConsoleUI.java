package com.kyoku.ibrokers.ui.console;

import java.util.List;
import java.util.Scanner;
import java.util.Set;
import java.util.TreeSet;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.ib.client.Contract;
import com.ib.client.ContractDetails;
import com.ib.client.Types.Right;
import com.kyoku.ibrokers.client.IBClient;
import com.kyoku.ibrokers.model.PriceData;
import com.kyoku.ibrokers.service.PriceDataFileWriter;
import com.kyoku.ibrokers.service.impl.PriceDataFileWriterImpl;

/**
 * Console user interface entry point
 * 
 * @author kyoku
 *
 */
public class ConsoleUI {

	private final static Logger logger = LoggerFactory.getLogger(ConsoleUI.class);

	private IBClient ibClient = new IBClient();
	private PriceDataFileWriter priceDataFileWriter = new PriceDataFileWriterImpl();

	public static void main(String[] args) {
		ConsoleUI consoleUI = new ConsoleUI();
		consoleUI.run();
	}

	private void run() {
		// Handle user interaction to retrieve TWS server and port
		System.out.print("Type server address or type ENTER to use default address 127.0.0.1: ");
		Scanner sc = new Scanner(System.in);
		String ip = sc.nextLine();
		ip = ip != null && !"".equals(ip) ? ip : "127.0.0.1";
		System.out.print("Type server port or type ENTER to use default port 7497: ");
		String port = sc.nextLine();
		port = port != null && !"".equals(port) ? port : "7497";
		sc.close();

		try {
			boolean connected = ibClient.connect(ip, new Integer(port), 0);
			if (connected) {
				// Retrieve contract list
				Contract contract = new Contract();
				contract.symbol("AAL");
				contract.secType("OPT");
				contract.currency("USD");
				contract.exchange("SMART");
				contract.right(Right.Put);
				contract.lastTradeDateOrContractMonth("20170317");
				List<ContractDetails> reqContractDetails = ibClient.reqContractDetails(contract);
				logger.info("Retrieved {} contract details", reqContractDetails.size());

				// Retrieve market data
				if (reqContractDetails.size() > 0) {
					Set<PriceData> priceDataList = new TreeSet<PriceData>();
					int c = 0;
					for (ContractDetails contractDetails : reqContractDetails) {
						PriceData priceData = ibClient.reqMarketData(contractDetails.contract());
						priceDataList.add(priceData);
						logger.info("Processed request {} of {} - {}", c += 1, reqContractDetails.size(), priceData);
					}
					priceDataFileWriter.write(contract, priceDataList);
				}
			}
		} catch (Throwable t) {
			logger.error("An error occurred during request processing...", t);
		} finally {
			logger.info("disconnecting...");
			ibClient.disconnect();
		}
		logger.info("EXIT");
	}

}
