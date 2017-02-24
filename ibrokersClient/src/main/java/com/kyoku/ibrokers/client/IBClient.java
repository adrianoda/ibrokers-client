package com.kyoku.ibrokers.client;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.ib.client.Contract;
import com.ib.client.ContractDetails;
import com.ib.client.EClientSocket;
import com.ib.client.EJavaSignal;
import com.ib.client.EReader;
import com.ib.client.EReaderSignal;
import com.ib.client.TagValue;
import com.kyoku.ibrokers.client.wrapper.IBEWrapper;
import com.kyoku.ibrokers.model.PriceData;
import com.kyoku.ibrokers.service.RequestIdGenerator;
import com.kyoku.ibrokers.service.impl.RequestIdGeneratorImpl;

/**
 * Handle TWS connection and requests
 * 
 * @author kyoku
 *
 */
public class IBClient {

	private final static Logger logger = LoggerFactory.getLogger(IBClient.class);

	private RequestIdGenerator requestIdGenerator = RequestIdGeneratorImpl.getInstance();

	private EReaderSignal readerSignal;
	private EClientSocket clientSocket;
	private IBEWrapper ewrapper;

	/**
	 * Initialize connection to TWS
	 * @param host
	 * @param port
	 * @param clientId
	 * @return
	 */
	public boolean connect(String host, int port, int clientId) {
		// Setup services
		readerSignal = new EJavaSignal();
		ewrapper = new IBEWrapper();
		clientSocket = new EClientSocket(ewrapper, readerSignal);
		ewrapper.setupEClientSocket(clientSocket);

		// Connect to TWS
		logger.info("Connecting to host:[{}], port[{}]", host, port);
		clientSocket.eConnect(host, port, clientId);

		if (!clientSocket.isConnected()) {
			logger.warn("Unable to connect");
			return false;
		}

		// Wait for data from TWS
		logger.info("Start reading data from socket...");
		final EReader reader = new EReader(clientSocket, readerSignal);
		reader.start();
		Thread readerThread = new Thread(new Runnable() {
			@Override
			public void run() {
				while (clientSocket.isConnected()) {
					readerSignal.waitForSignal();
					try {
						reader.processMsgs();
					} catch (Exception e) {
						throw new RuntimeException(e);
					}
				}
			}
		});
		readerThread.start();

		// Wait one of managedAccounts or nextValidId invocation before read anything from TWS (see documentation)
		ewrapper.waitRequestCompletion();
		return true;
	}

	/**
	 * Disconnect from TWS
	 */
	public void disconnect() {
		readerSignal.issueSignal();
		clientSocket.eDisconnect();
	}

	/**
	 * Request contract details of specified contract search criteria
	 * @param reqId
	 * @param contract
	 * @return
	 */
	public List<ContractDetails> reqContractDetails(Contract contract) {
		int reqId = requestIdGenerator.nextId();
		logger.debug("[{}] - reqContractDetails invoked, contract {}", reqId, contract);
		List<ContractDetails> contractDetailsResponse = ewrapper.setupContractDetailsResponse();
		clientSocket.reqContractDetails(reqId, contract);
		ewrapper.waitRequestCompletion();
		return contractDetailsResponse;
	}

	/**
	 * Request market data of specified contract
	 * @param reqId
	 * @param contract
	 * @return
	 */
	public PriceData reqMarketData(Contract contract) {
		int reqId = requestIdGenerator.nextId();
		logger.debug("[{}] - reqMarketData invoked, contract {}", reqId, contract);
		PriceData priceData = ewrapper.setupReqMktData(contract);
		clientSocket.reqMktData(reqId, contract, "", true, new ArrayList<TagValue>());
		ewrapper.waitRequestCompletion();
		return priceData;
	}

}
