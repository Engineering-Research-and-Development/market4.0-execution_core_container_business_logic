package it.eng.idsa.businesslogic.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;

import it.eng.idsa.businesslogic.processor.producer.websocket.client.FileStreamingBean;
import it.eng.idsa.businesslogic.processor.producer.websocket.client.IdscpClientBean;
import it.eng.idsa.businesslogic.processor.producer.websocket.client.InputStreamSocketListenerClient;
import it.eng.idsa.businesslogic.processor.producer.websocket.client.ResponseMessageBufferClient;
import it.eng.idsa.businesslogic.processor.producer.websocket.client.ResponseMessageReceiverClient;

/**
 * 
 * @author Milan Karajovic and Gabriele De Luca
 *
 */

@Configuration
public class WebSocketClientConfiguration {

	@Bean
	@Scope("singleton")
	public IdscpClientBean idscpClientServiceWebSocket() {
		return new IdscpClientBean();
	}
	
	@Bean
	@Scope("singleton")
	public FileStreamingBean fileStreamingWebSocket() {
		return new FileStreamingBean();
	}
	
	@Bean
	@Scope("singleton")
	public InputStreamSocketListenerClient inputStreamSocketListenerWebSocketClient() {
		return new InputStreamSocketListenerClient();
	}
	
	@Bean
	@Scope("singleton")
	public ResponseMessageBufferClient responseMessageBufferWebSocketClient() {
		return new ResponseMessageBufferClient();
	}
	
	@Bean
	@Scope("singleton")
	public ResponseMessageReceiverClient responseMessageReceiverWebSocketClient() {
		return new ResponseMessageReceiverClient();
	}
	
}
