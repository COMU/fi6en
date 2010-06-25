package org.red5.fi6en.streaming;

import org.red5.fi6en.core.Application;
import org.red5.logging.Red5LoggerFactory;
import org.red5.server.api.IScope;
import org.red5.server.api.Red5;
import org.red5.server.stream.ClientBroadcastStream;
import org.slf4j.Logger;

/**
 * <code>StreamManager</code> provides services for recording
 * the broadcast stream.
 */
public class StreamManager{

	private static final Logger log = Red5LoggerFactory.getLogger(StreamManager.class, "fi6en");

	// Application components
	private Application app;

	/**
	 * Start recording the publishing stream for the specified
	 * <code>username</code>
	 * 
	 * @param username
	 */
	public void recordShow(String username) {
		IScope appScope= Red5.getConnectionLocal().getScope();
		log.debug("Recording show for: {}", username);
		String streamName = String.valueOf(System.currentTimeMillis());
		streamName= username+"_"+streamName;
		// Get a reference to the current broadcast stream.
		ClientBroadcastStream stream = (ClientBroadcastStream) app.getBroadcastStream(
				appScope, username);
		try {
			// Save the stream to disk.
			stream.saveAs(streamName, false);
		} catch (Exception e) {
			log.error("Error while saving stream: {}", streamName);
		}
	}

	/**
	 * Stops recording the publishing stream for the specified
	 * <code>username</code>
	 */
	public void stopRecordingShow(String username) {
		IScope appScope= Red5.getConnectionLocal().getScope();
		log.debug("Stop recording show for: {}", username);
		// Get a reference to the current broadcast stream.
		ClientBroadcastStream stream = (ClientBroadcastStream) app.getBroadcastStream(
				appScope, username);
		// Stop recording.
		stream.stopRecording();
		stream.stop();
	}

	/* ----- Spring injected dependencies ----- */

	public void setApplication(Application app) {
		this.app = app;
	}
}
