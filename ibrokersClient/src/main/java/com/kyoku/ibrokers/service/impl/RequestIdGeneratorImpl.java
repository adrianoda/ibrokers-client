package com.kyoku.ibrokers.service.impl;

import java.util.Random;
import java.util.concurrent.atomic.AtomicInteger;

import com.kyoku.ibrokers.service.RequestIdGenerator;

public class RequestIdGeneratorImpl implements RequestIdGenerator {

	private static RequestIdGeneratorImpl requestIdGenerator;

	private AtomicInteger id;

	private RequestIdGeneratorImpl(AtomicInteger id) {
		this.id = id;
	}

	public static synchronized RequestIdGeneratorImpl getInstance() {
		if (requestIdGenerator == null) {
			requestIdGenerator = new RequestIdGeneratorImpl(new AtomicInteger(new Random().nextInt()));
		}
		return requestIdGenerator;
	}

	@Override
	public int nextId() {
		return id.getAndIncrement();
	}

}
