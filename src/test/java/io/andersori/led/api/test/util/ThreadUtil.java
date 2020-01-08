package io.andersori.led.api.test.util;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.TimeUnit;

public abstract class ThreadUtil {
	
	public static void awaitTerminationAfterShutdown(ExecutorService threadPool, int seconds) {
		threadPool.shutdown();
		try {
			if (!threadPool.awaitTermination(seconds, TimeUnit.SECONDS)) {
				threadPool.shutdownNow();
			}
		} catch (InterruptedException ex) {
			threadPool.shutdownNow();
			Thread.currentThread().interrupt();
		}
	}
	
}
