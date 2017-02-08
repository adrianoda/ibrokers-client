package com.kyoku.ibrokers.ui.console;

import java.io.FileWriter;
import java.io.IOException;
import java.util.List;
import java.util.Scanner;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.ib.client.Contract;
import com.ib.client.ContractDetails;
import com.kyoku.ibrokers.client.IBClient;

public class ConsoleUI {

	private final static Logger LOGGER = LoggerFactory.getLogger(ConsoleUI.class);

	public static void main(String[] args) {
		LOGGER.info("START");
		IBClient ibClient = new IBClient();

		// Handle user interaction
		System.out.print("Type server address or type ENTER to use default address 127.0.0.1: ");
		Scanner sc = new Scanner(System.in);
		String ip = sc.nextLine();
		ip = ip != null && !"".equals(ip) ? ip : "127.0.0.1";
		System.out.print("Type server port or type ENTER to use default port 7497: ");
		String port = sc.nextLine();
		port = port != null && !"".equals(port) ? port : "7497";
		sc.close();

		try {
			ibClient.connect(ip, new Integer(port), 0);

			Contract contract = new Contract();
			contract.symbol("FISV");
			contract.secType("OPT");
			contract.currency("USD");
			contract.exchange("SMART");
			List<ContractDetails> reqContractDetails = ibClient.reqContractDetails(211, contract);
			if(reqContractDetails.size() > 0) {
				writeFile(reqContractDetails);
			}
			// for (ContractDetails cdet : reqContractDetails) {
			// LOGGER.info("Contract: " + cdet.toString());
			// }
			LOGGER.info("Retrieved {} contract details", reqContractDetails.size());
		} catch (Throwable t) {
			LOGGER.info("An error occurred during request processing...", t);
		} finally {
			LOGGER.info("disconnecting...");
			ibClient.disconnect();
		}
		LOGGER.info("END");
	}

	private static void writeFile(List<ContractDetails> reqContractDetails) throws IOException {
		FileWriter writer = new FileWriter("contract-details.txt");
		try {
			for(ContractDetails cdet : reqContractDetails) {
				writer.write(cdet.toString());
				writer.write("**********************************************\n");
			}
		} finally {
			writer.close();
		}
		
	}

}
