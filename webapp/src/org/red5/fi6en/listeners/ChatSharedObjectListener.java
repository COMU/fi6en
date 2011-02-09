package org.red5.fi6en.listeners;

import org.red5.fi6en.core.SharedObjectListener;
import org.red5.logging.Red5LoggerFactory;
import org.red5.server.api.so.ISharedObjectBase;
import org.slf4j.Logger;

/**
 * Listener Class for the chat share object in the lobby
 * @author cem
 *
 */

public class ChatSharedObjectListener extends SharedObjectListener{
	Logger log = Red5LoggerFactory.getLogger(ChatSharedObjectListener.class,
	"fi6en");
	
	@Override
	public void onSharedObjectConnect(ISharedObjectBase so) {
		log.info("ChatSharedObjectListener connected");
	}
	
	@Override
	public void onSharedObjectDisconnect(ISharedObjectBase so) {
		log.info("ChatSharedObjectListener disconnected");
	}
	
	@Override
	public void onSharedObjectClear(ISharedObjectBase so) {
		log.info("ChatSharedObjectListener cleared");
	}
}
