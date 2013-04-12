package com.gameforge.gamejam.pongout.core.play;

import playn.core.Key;
import playn.core.Keyboard;
import playn.core.Keyboard.Event;
import playn.core.Keyboard.TypedEvent;

public class UserInput implements Keyboard.Listener {

	public boolean up = false;
	public boolean down = false;
	public boolean action = false;
	private final Key[] upKeys;
	private final Key[] downKeys;
	private final Key[] actionKeys;

	public UserInput(Key[] upKeys, Key[] downKeys, Key[] actionsKeys) {
		super();
		this.upKeys = upKeys;
		this.downKeys = downKeys;
		this.actionKeys = actionsKeys;
	}

	@Override
	public void onKeyDown(Event event) {
		checkKeys(event, true);
	}

	@Override
	public void onKeyUp(Event event) {
		checkKeys(event, false);
	}

	private void checkKeys(Event event, boolean targetState) {
		// up?
		if (inArray(upKeys, event.key())) {
			up = targetState;
			return;
		}
		// down?
		if (inArray(downKeys, event.key())) {
			down = targetState;
			return;
		}
		// action?
		if (inArray(actionKeys, event.key())) {
			action = targetState;
			return;
		}

	}

	private static boolean inArray(Key[] keys, Key eventKey) {
		for (Key key : keys) {
			if (key == eventKey) {
				return true;
			}
		}
		return false;
	}

	@Override
	public void onKeyTyped(TypedEvent event) {
	}

}