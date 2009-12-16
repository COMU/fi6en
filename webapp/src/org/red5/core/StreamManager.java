package org.red5.core;

import org.red5.logging.Red5LoggerFactory;
import org.red5.server.api.IConnection;
import org.red5.server.service.ServiceInvoker;
import org.red5.server.stream.ClientBroadcastStream;


public class StreamManager extends ServiceInvoker{
	//private static final Logger logger= Red5LoggerFactory.getLogger(StreamManager.class, "streamManagerContext");
	private Application app;
	
	public void showStreamNames (IConnection conn) {
		//logger.debug("Stream for : " +conn.getScope().getContextPath());
		ClientBroadcastStream stream= (ClientBroadcastStream) app.getBroadcastStreamNames(conn.getScope());
		try {
			//logger.debug(stream.getPublishedName());
		}
		catch (Exception e) {
			// TODO: handle exception
			//logger.error("error with the stream", e);
		}
	}

	public Application getApp() {
		return app;
	}

	public void setApp(Application app) {
		this.app = app;
	}
	
	
	
}