import java.util.LinkedList;
import java.util.List;

/* 
 *  Symulacja problemu przejazdu przez wąski most
 *  
 *
 *  Autor: Leanid Paulouski
 *   Data: styczeń 2020 r.
 */

public class Bridge {

	private static int allowedAutoNumber;
	private static AutoDirection lastDirection=AutoDirection.EAST;
	private static int oneDirectionNumber=0;

	List<Auto> allAutos = new LinkedList<Auto>();

	static List<Auto> autoWaiting = new LinkedList<Auto>();

	static List<Auto> autoOnBridge = new LinkedList<Auto>();

	
	
	void getIdWaiting() {
		StringBuilder sb=new StringBuilder();
		for(Auto auto:autoWaiting) {
			sb.append(auto.id+" ");
		}
		BridgeApp.waitingAutosNumbers.setText(sb.toString());
	}
	
	void getIdOnBridge() {
		StringBuilder sb=new StringBuilder();
		for(Auto auto:autoOnBridge) {
			sb.append(auto.id+" ["+auto.dir+"] ,");
		}
		BridgeApp.bridgeAutosNumbers.setText(sb.toString());
	}
	
	static int getNumberWaiting() {
		return autoWaiting.size();
	}

	static int getNumberOnBridge() {
		return autoOnBridge.size();
	}

	void printBridgeInfo(Auto auto, String message) {
		BridgeApp.addStringInfo("Auto[" + auto.id + "->" + auto.dir + "]: " + message);
		BridgeApp.setLabels(autoWaiting.size(), autoOnBridge.size());
	}

	private void setAllowedAutoNumber() {
		if ((BridgeType) BridgeApp.box.getSelectedItem() == BridgeType.oneWay) {
			allowedAutoNumber = 1;
		} else if ((BridgeType) BridgeApp.box.getSelectedItem() == BridgeType.oneWay1) {
			allowedAutoNumber = 3;
		} else if ((BridgeType) BridgeApp.box.getSelectedItem() == BridgeType.twoWay) {
			allowedAutoNumber = 3;
		} else if ((BridgeType) BridgeApp.box.getSelectedItem() == BridgeType.noLimits) {
			allowedAutoNumber = 10000;
			notify();
		}
	}
	
	private void setPermit(Auto auto) {
		if ((BridgeType) BridgeApp.box.getSelectedItem() == BridgeType.oneWay1) {
			if(auto.dir==lastDirection && oneDirectionNumber<=9 ) {
				auto.setPermit(true);
				lastDirection=auto.dir;
				++oneDirectionNumber;
			}
			else if((auto.dir!=lastDirection && oneDirectionNumber>9 && autoOnBridge.isEmpty()) || autoOnBridge.isEmpty() ) {
				auto.setPermit(true);
				lastDirection=auto.dir;
				oneDirectionNumber=0;
			}
			else  {
				auto.setPermit(false);
			}
		}else {
			auto.setPermit(true);
			lastDirection=auto.dir;
		}
	}


	synchronized void getOnTheBridge1(Auto auto) {
		setAllowedAutoNumber();
		setPermit(auto);
		while (autoOnBridge.size() >= allowedAutoNumber || auto.getPemit()==false) {
			autoWaiting.add(auto);
			getIdWaiting();
			printBridgeInfo(auto, "CZEKA NA WJAZD");
			try {
				 
				wait();
				
			} catch (InterruptedException e) {
			}
			autoWaiting.remove(auto);
			getIdWaiting();
			setPermit(auto);
		}
		autoOnBridge.add(auto);
		getIdOnBridge();
		printBridgeInfo(auto, "WJEŻDŻA NA MOST");
	}

	synchronized void getOffTheBridge1(Auto auto) {
		autoOnBridge.remove(auto);
		getIdOnBridge();
		printBridgeInfo(auto, "OPUSZCZA MOST");
		notify();
	}

}
