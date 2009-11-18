package org.red5.core;

import java.util.List;
import java.util.Map;

import org.red5.server.api.IAttributeStore;
import org.red5.server.api.so.ISharedObject;
import org.red5.server.api.so.ISharedObjectBase;
import org.red5.server.api.so.ISharedObjectListener;


public class MyCustomListener implements ISharedObjectListener {
	
	public void onSharedObjectConnect(ISharedObjectBase so) {
		System.out.println("shared object connect");
	}

	/**
	 * Called when a client disconnects from a shared object.
	 * 
	 * @param so
	 *            the shared object
	 */
	public void onSharedObjectDisconnect(ISharedObjectBase so) {
		System.out.println("shared object disconnect");
	}

	/**
	 * Called when a shared object attribute is updated.
	 * 
	 * @param so
	 *            the shared object
	 * @param key
	 *            the name of the attribute
	 * @param value
	 *            the value of the attribute
	 */
	public void onSharedObjectUpdate(ISharedObjectBase so, String key, Object value) {
		System.out.println("shared object update");
	}

	@Override
	public void onSharedObjectClear(ISharedObjectBase so) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onSharedObjectDelete(ISharedObjectBase so, String key) {
		// TODO Auto-generated method stub
		System.out.println("shared object deleted");
		
	}

	@Override
	public void onSharedObjectSend(ISharedObjectBase so, String method,
			List<?> params) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onSharedObjectUpdate(ISharedObjectBase so,
			IAttributeStore values) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onSharedObjectUpdate(ISharedObjectBase so,
			Map<String, Object> values) {
		// TODO Auto-generated method stub
		
	}

	/**
	 * Called when multiple attributes of a shared object are updated.
	 * 
	 * @param so
	 *            the shared object
	 * @param values
	 *            the new attributes of the shared object
	 */
	}
