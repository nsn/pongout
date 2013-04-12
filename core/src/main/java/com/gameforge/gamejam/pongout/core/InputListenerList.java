package com.gameforge.gamejam.pongout.core;

import java.util.ArrayList;

import playn.core.Keyboard;
import playn.core.Keyboard.Event;
import playn.core.Keyboard.Listener;
import playn.core.Keyboard.TypedEvent;

public class InputListenerList implements Keyboard.Listener {
	private final ArrayList<Keyboard.Listener> listeners;

	public InputListenerList() {
		super();
		this.listeners = new ArrayList<Keyboard.Listener>();
	}

	public void addListener(Keyboard.Listener l) {
		listeners.add(l);
	}

	@Override
	public void onKeyDown(Event event) {
		for (Listener l : listeners) {
			l.onKeyDown(event);
		}
	}

	@Override
	public void onKeyUp(Event event) {
		for (Listener l : listeners) {
			l.onKeyUp(event);
		}
	}

	@Override
	public void onKeyTyped(TypedEvent event) {
		for (Listener l : listeners) {
			l.onKeyTyped(event);
		}
	}

}