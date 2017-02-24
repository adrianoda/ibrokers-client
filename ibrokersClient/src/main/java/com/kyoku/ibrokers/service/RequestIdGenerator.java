package com.kyoku.ibrokers.service;

/**
 * Common service to retrieve 'per execution' unique id
 * 
 * @author kyoku
 *
 */
public interface RequestIdGenerator {
	
	int nextId();

}
