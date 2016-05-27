package input.controller.logic;

import java.util.EventListener;
import java.util.concurrent.atomic.AtomicLong;

import javax.swing.event.EventListenerList;

import org.lwjgl.input.Controller;
import org.lwjgl.input.Controllers;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import input.controller.Constants;

public class ControllerEventQueue {

	private static final Logger LOG = LoggerFactory
			.getLogger(ControllerEventQueue.class);

	private static ControllerEventQueue INSTANCE;

	private EventListenerList listenerList = new EventListenerList();
	private AtomicLong pollTime = new AtomicLong(
			Constants.CONTROLLER_POLL_INTERVAL);
	private EventThread eventThread;

	private ControllerEventQueue() {
	}

	public static ControllerEventQueue getInstance() {
		if (INSTANCE == null) {
			INSTANCE = new ControllerEventQueue();
		}
		return INSTANCE;
	}

	public long getPollTime() {
		return pollTime.get();
	}

	public long setPollTime(long pollTime) {
		LOG.info("Set poll time {}", pollTime);
		return this.pollTime.getAndSet(pollTime);
	}

	public void addControllerListener(ControllerListener l) {
		synchronized (listenerList) {
			listenerList.add(ControllerListener.class, l);
		}
	}

	public void removeControllerListener(ControllerListener l) {
		synchronized (listenerList) {
			listenerList.remove(ControllerListener.class, l);
		}
	}

	/**
	 * If not started starts the event queue.
	 * 
	 * @return true, if a new event queue was started, false otherwise
	 */
	public boolean start() {
		if (eventThread != null) {
			return false;
		}

		eventThread = new EventThread();
		eventThread.start();

		return true;
	}

	public boolean destroy() {
		return destroy(0);
	}

	public boolean destroy(long millisWait) {
		if (eventThread == null) {
			return false;
		}

		eventThread.interrupt();
		try {
			eventThread.join(millisWait);
		} catch (InterruptedException e) {
		}

		return true;
	}

	private class EventThread extends Thread {

		public EventThread() {
			super("ControllerEventQueue");
		}

		@Override
		public void run() {
			while (!isInterrupted()) {
				synchronized (listenerList) {
					poll();
				}
				try {
					Thread.sleep(getPollTime());
				} catch (InterruptedException e) {
					interrupt();
				}
			}
		}

		private void poll() {

			synchronized (listenerList) {
				if (listenerList.getListenerCount() <= 0) {
					return;
				}
			}

			Controllers.poll();

			// Event driven
			while (Controllers.next()) {
				Controller source = Controllers.getEventSource();

				ControllerEvent event = new ControllerEvent(source);
				event.setButtonEvent(Controllers.isEventButton());
				event.setAxisEvent(Controllers.isEventAxis());
				event.setxAxisEvent(Controllers.isEventXAxis());
				event.setyAxisEvent(Controllers.isEventYAxis());
				event.setPovEvent(
						Controllers.isEventPovX() || Controllers.isEventPovY());
				event.setxPovEvent(Controllers.isEventPovX());
				event.setyPovEvent(Controllers.isEventPovY());
				event.setPressed(Controllers.getEventButtonState());
				event.setControlIndex(Controllers.getEventControlIndex());

				fireControllerEvent(event);
			}

			// Poll driven
			for (int i = 0; i < Controllers.getControllerCount(); i++) {
				ControllerEvent event = new ControllerEvent(
						Controllers.getController(i));
				fireControllerEvent(event);
			}
		}

		private void fireControllerEvent(ControllerEvent event) {
			synchronized (listenerList) {
				ControllerListener[] listeners = listenerList
						.getListeners(ControllerListener.class);
				for (ControllerListener listener : listeners) {
					if (listener == null) {
						listenerList.remove(ControllerListener.class, listener);
						continue;
					}

					try {
						listener.action(event);
					} catch (Exception e) {
						LOG.warn("Controllerevent listener failed", e);
					}
				}
			}
		}
	}

	public static class ControllerEvent {

		private Controller source;
		private boolean buttonEvent = false;
		private boolean axisEvent = false;
		private boolean xAxisEvent = false;
		private boolean yAxisEvent = false;
		private boolean povEvent = false;
		private boolean xPovEvent = false;
		private boolean yPovEvent = false;
		private boolean pressed = false;
		private int controlIndex = -1;

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
			return "ControllerEvent [source="
					+ String.format("%d: %s", source.getIndex(),
							source.getName())
					+ ", buttonEvent=" + buttonEvent + ", axisEvent="
					+ axisEvent + ", xAxisEvent=" + xAxisEvent + ", yAxisEvent="
					+ yAxisEvent + ", povEvent=" + povEvent + ", xPovEvent="
					+ xPovEvent + ", yPovEvent=" + yPovEvent + ", pressed="
					+ pressed + ", controlIndex=" + controlIndex + "]";
		}

	}

	public static interface ControllerListener extends EventListener {
		void action(ControllerEvent event);
	}
}
