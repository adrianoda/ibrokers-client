package com.kyoku.ibrokers.ui.console;

import java.util.ArrayList;
import java.util.List;

public class TestVari {

	public static void main(String[] args) throws InterruptedException {
		final List<String> stringList = new ArrayList<String>();

		System.out.println("thread 1 START");

		Thread t = new Thread(new Runnable() {

			@Override
			public void run() {
				System.out.println("thread 2 START");
				try {
					Thread.sleep(1000);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				System.out.println("thread 2 add to stringList");
				stringList.add("TEST");
				synchronized (stringList) {
					stringList.notify();
				}
				System.out.println("thread 2 END");
			}
		});
		t.start();
		
		System.out.println("MEOW");
		
		System.out.println("thread 1 WAITING");
		synchronized (stringList) {
			stringList.wait();
		}
		System.out.println("thread 1 stringList size" + stringList.size());

		t.interrupt();
		
		System.out.println("thread 1 END");
	}

}
