package org.red5.core;

import org.red5.server.adapter.ApplicationAdapter;
import org.red5.server.api.IConnection;
import org.red5.server.api.IScope;
import org.red5.server.api.so.ISharedObject;
import org.red5.server.api.so.ISharedObjectListener;

public class Application extends ApplicationAdapter{
	
	//IConnection conn= Red5.getConnectionLocal();
		String message="";

	public boolean appStart (IScope scope) {
		//System.out.println(scope);
		System.out.println("Shared Object application started");
		createSharedObject(scope, "chat", true);
		
		ISharedObject so = getSharedObject(scope, "chat");		
		ISharedObjectListener listener = new MyCustomListener();
		
		so.addSharedObjectListener(listener);
		so.setAttribute(message, "zz");
		
		System.out.println(so.getName());
		System.out.println(so.getPath());
		System.out.println(so.getClass());
		return true;
	}
	
	public void appStop (IScope scope) {
		System.out.println("Shared Object Application stopped");
		
	}
	public boolean connect (IConnection conn, IScope scope, Object []params) {
		System.out.println("Connected to server application - Client : "+ 
							conn.getClient()+","+conn.getScope());
		return true;
	}
	public void disconnect (IConnection conn, IScope scope) {
		System.out.println("Disconnected");
		super.disconnect(conn, scope);
	}
	
	public boolean roomStart(IScope scope) {
		  if (!super.roomStart(scope))
		      return false;

		  createSharedObject(scope, "chat", true);
		  ISharedObject so = getSharedObject(scope, "chat");
		  
		  System.out.println(so.getName());
		  System.out.println("shared object");

		  // Şimdi paylaşılan nesneyle birşeyler yapabilirsiniz...

		  return true;
		}

}
