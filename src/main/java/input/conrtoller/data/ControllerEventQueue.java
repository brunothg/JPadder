package input.conrtoller.data;

import java.util.EventListener;
import java.util.concurrent.atomic.AtomicLong;

import javax.swing.event.EventListenerList;

import org.lwjgl.input.Controller;
import org.lwjgl.input.Controllers;

import input.conrtoller.Constants;

public class ControllerEventQueue {

	private static EventListenerList listenerList = new EventListenerList();
	private static AtomicLong pollTime = new AtomicLong(Constants.CONTROLLER_POLL_INTERVAL);
	private static EventThread thread;

	public static long getPollTime() {
		return pollTime.get();
	}

	public static long setPollTime(long pollTime) {
		return ControllerEventQueue.pollTime.getAndSet(pollTime);
	}

	public static void addControllerListener(ControllerListener l) {
		synchronized (listenerList) {
			listenerList.add(ControllerListener.class, l);
		}
	}

	public static void removeControllerListener(ControllerListener l) {
		synchronized (listenerList) {
			listenerList.remove(ControllerListener.class, l);
		}
	}

	public static boolean create() {
		if (thread != null) {
			return false;
		}

		thread = new EventThread();
		thread.start();

		return true;
	}

	public static boolean destroy() {
		return destroy(0);
	}

	public static boolean destroy(long millisWait) {
		if (thread == null) {
			return false;
		}

		thread.interrupt();
		try {
			thread.join(millisWait);
		} catch (InterruptedException e) {
		}

		return true;
	}

	private static class EventThread extends Thread {
		@Override
		public void run() {
			while (!isInterrupted()) {
				poll();
				try {
					Thread.sleep(getPollTime());
				} catch (InterruptedException e) {
					interrupt();
				}
			}
		}

		private void poll() {

			Controllers.poll();
			while (Controllers.next()) {
				Controller source = Controllers.getEventSource();

				ControllerEvent event = new ControllerEvent(source);
				event.setButtonEvent(Controllers.isEventButton());
				event.setAxisEvent(Controllers.isEventAxis());
				event.setxAxisEvent(Controllers.isEventXAxis());
				event.setyAxisEvent(Controllers.isEventYAxis());
				event.setPovEvent(Controllers.isEventPovX() || Controllers.isEventPovY());
				event.setxPovEvent(Controllers.isEventPovX());
				event.setyPovEvent(Controllers.isEventPovY());
				event.setPressed(Controllers.getEventButtonState());
				event.setControlIndex(Controllers.getEventControlIndex());

				fireControllerEvent(event);
			}
		}

		private void fireControllerEvent(ControllerEvent event) {
			synchronized (listenerList) {
				ControllerListener[] listeners = listenerList.getListeners(ControllerListener.class);
				for (ControllerListener listener : listeners) {
					if (listener == null) {
						listenerList.remove(ControllerListener.class, listener);
						continue;
					}
					listener.action(event);
				}
			}
		}
	}

	public static class ControllerEvent {

		private Controller source;
		private boolean buttonEvent;
		private boolean axisEvent;
		private boolean xAxisEvent;
		private boolean yAxisEvent;
		private boolean povEvent;
		private boolean xPovEvent;
		private boolean yPovEvent;
		private boolean pressed;
		private int controlIndex;

		public ControllerEvent(Controller source) {
			this.source = source;
		}

		public Controller getSource() {
			return this.source;
		}

		public boolean isButtonEvent() {
			return buttonEvent;
		}

		public void setButtonEvent(boolean buttonEvent) {
			this.buttonEvent = buttonEvent;
		}

		public boolean isAxisEvent() {
			return axisEvent;
		}

		public void setAxisEvent(boolean axisEvent) {
			this.axisEvent = axisEvent;
		}

		public boolean isxAxisEvent() {
			return xAxisEvent;
		}

		public void setxAxisEvent(boolean xAxisEvent) {
			this.xAxisEvent = xAxisEvent;
		}

		public boolean isyAxisEvent() {
			return yAxisEvent;
		}

		public void setyAxisEvent(boolean yAxisEvent) {
			this.yAxisEvent = yAxisEvent;
		}

		public boolean isPovEvent() {
			return povEvent;
		}

		public void setPovEvent(boolean povEvent) {
			this.povEvent = povEvent;
		}

		public boolean isxPovEvent() {
			return xPovEvent;
		}

		public void setxPovEvent(boolean xPovEvent) {
			this.xPovEvent = xPovEvent;
		}

		public boolean isyPovEvent() {
			return yPovEvent;
		}

		public void setyPovEvent(boolean yPovEvent) {
			this.yPovEvent = yPovEvent;
		}

		public boolean isPressed() {
			return pressed;
		}

		public void setPressed(boolean pressed) {
			this.pressed = pressed;
		}

		public int getControlIndex() {
			return controlIndex;
		}

		public void setControlIndex(int controlIndex) {
			this.controlIndex = controlIndex;
		}

		@Override
		public String toString() {
			return "ControllerEvent [source=" + String.format("%d: %s", source.getIndex(), source.getName())
					+ ", buttonEvent=" + buttonEvent + ", axisEvent=" + axisEvent + ", xAxisEvent=" + xAxisEvent
					+ ", yAxisEvent=" + yAxisEvent + ", povEvent=" + povEvent + ", xPovEvent=" + xPovEvent
					+ ", yPovEvent=" + yPovEvent + ", pressed=" + pressed + ", controlIndex=" + controlIndex + "]";
		}

	}

	public static interface ControllerListener extends EventListener {
		void action(ControllerEvent event);
	}
}
