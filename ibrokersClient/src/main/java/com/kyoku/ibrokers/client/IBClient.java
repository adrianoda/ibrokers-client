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

public class IBClient {

	private final static Logger logger = LoggerFactory.getLogger(IBClient.class);

	private EReaderSignal readerSignal;
	private EClientSocket clientSocket;
	private IBEWrapper ewrapper;

	public void connect(String host, int port, int clientId) {
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
			return;
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
		ewrapper.waitRequestComplention();
	}

	public void disconnect() {
		readerSignal.issueSignal();
		clientSocket.eDisconnect();
	}

	public List<ContractDetails> reqContractDetails(int reqId, Contract contract) {
		logger.info("reqContractDetails invoked, contract {}", contract);
		List<ContractDetails> contractDetailsResponse = ewrapper.setupContractDetailsResponse();
		clientSocket.reqContractDetails(reqId, contract);
		ewrapper.waitRequestComplention();
		return contractDetailsResponse;
	}
	
	public void reqMarketData(int reqId, Contract contract) {
		logger.info("reqMarketData invoked, contract {}", contract);
		ewrapper.setupReqMktData();
		clientSocket.reqMktData(reqId, contract, "", true, new ArrayList<TagValue>());
		ewrapper.waitRequestComplention();
	}

}
