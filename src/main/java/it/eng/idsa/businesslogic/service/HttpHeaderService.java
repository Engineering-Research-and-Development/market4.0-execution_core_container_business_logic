package it.eng.idsa.businesslogic.service;

import java.io.IOException;
import java.util.Map;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;

public interface HttpHeaderService {
	
	public String getHeaderMessagePartFromHttpHeadersWithoutToken(Map<String, Object> headers) throws JsonProcessingException;

	public Map<String, Object> prepareMessageForSendingAsHttpHeadersWithToken(String header) throws JsonParseException, JsonMappingException, IOException;

	public String getHeaderMessagePartFromHttpHeadersWithToken(Map<String, Object> headers) throws JsonProcessingException;

	public Map<String, Object> prepareMessageForSendingAsHttpHeadersWithoutToken(String header) throws JsonParseException, JsonMappingException, IOException;
}