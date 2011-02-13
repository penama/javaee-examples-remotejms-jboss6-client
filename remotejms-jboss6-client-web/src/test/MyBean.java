package test;

import java.util.Properties;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.jms.Destination;
import javax.jms.MessageProducer;
import javax.jms.Session;
import javax.jms.TextMessage;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;

@ManagedBean
@RequestScoped
public class MyBean {

	
	public String sendTestMessage() throws Exception {
		
		Context context = getRemoteContext();

		ConnectionFactory factory = (ConnectionFactory) context.lookup("/ConnectionFactory");

		Connection connection = null;
		try {
			connection = factory.createConnection();

			Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
			Destination destination = (Destination) context.lookup("/queue/testQueue");

			MessageProducer producer = session.createProducer(destination);
			TextMessage message = session.createTextMessage("Test");

			producer.send(message);
		} finally {
			if (connection != null) {
				connection.close();
			}
		}
		
		return "";
	}

	private Context getRemoteContext() throws NamingException {
		Properties properties = new Properties();
		properties.put(Context.INITIAL_CONTEXT_FACTORY, "org.jnp.interfaces.NamingContextFactory");
		properties.put(Context.URL_PKG_PREFIXES, "jboss.naming:org.jnp.interfaces");
		properties.put(Context.PROVIDER_URL, "jnp://localhost:1199");
		return new InitialContext(properties);
	}

}
