import java.util.concurrent.ThreadLocalRandom;

/* 
 *  Symulacja problemu przejazdu przez wąski most
 *  
 *
 *  Autor: Leanid Paulouski
 *   Data: styczeń 2020 r.
 */

public class Auto implements Runnable {

	public static final int MIN_BOARDING_TIME = 1000;
	public static final int MAX_BOARDING_TIME = 10000;
	public static final int GETTING_TO_BRIDGE_TIME = 500;
	public static final int CROSSING_BRIDGE_TIME = 3000;
	public static final int GETTING_PARKING_TIME = 500;
	public static final int UNLOADING_TIME = 500;

	private static int numberOfBuses = 0;
	Bridge bridge;
	int id;
	AutoDirection dir;
	private boolean permit;

	public void setPermit(boolean permit) {
		this.permit=permit;
	}
	
	public boolean getPemit() {
		return this.permit;
	}
	
	public static void sleep(int time) {
		try {
			Thread.sleep(time);
		} catch (InterruptedException e) {
		}
	}

	public static void sleep(int minTime, int maxTime) {
		sleep(ThreadLocalRandom.current().nextInt(minTime, maxTime));
	}

	Auto(Bridge bridge) {
		this.bridge = bridge;
		this.id = ++numberOfBuses;
		if (ThreadLocalRandom.current().nextInt(0, 101) >= BridgeApp.getDirectionValue()) {
			this.dir = AutoDirection.EAST;
		} else {
			this.dir = AutoDirection.WEST;
		}
	}

	void printBusInfo(String message) {
		BridgeApp.addStringInfo("Auto[" + id + "->" + dir + "]: " + message);
	}

	void boarding() {
		printBusInfo("Czeka na nowych pasażerów");
		sleep(MIN_BOARDING_TIME, MAX_BOARDING_TIME);
	}

	void goToTheBridge() {
		printBusInfo("Jazda w stronę mostu");
		sleep(GETTING_TO_BRIDGE_TIME);
	}

	void rideTheBridge() {
		printBusInfo("Przejazd przez most");
		sleep(CROSSING_BRIDGE_TIME);
	}

	void goToTheParking() {
		printBusInfo("Jazda w stronę końcowego parkingu");
		sleep(GETTING_PARKING_TIME);
	}

	void unloading() {
		printBusInfo("Rozładunek pasażerów");
		sleep(UNLOADING_TIME);
	}

	public void run() {
		bridge.allAutos.add(this);
		boarding();
		goToTheBridge();
		bridge.getOnTheBridge1(this);
		rideTheBridge();
		bridge.getOffTheBridge1(this);
		goToTheParking();
		unloading();
		bridge.allAutos.remove(this);
	}

}
