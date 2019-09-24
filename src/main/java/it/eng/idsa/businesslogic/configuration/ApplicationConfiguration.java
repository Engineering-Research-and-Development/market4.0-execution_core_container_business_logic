package it.eng.idsa.businesslogic.configuration;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * 
 * @author Milan Karajovic and Gabriele De Luca
 *
 */

@Component
@ConfigurationProperties("application")
public class ApplicationConfiguration {
	
	private String targetDirectory;
	private String dapsUrl;
	private String keyStoreName;
	private String keyStorePassword;
	private String keystoreAliasName;
	private String connectorUUID;
	private String proxyUser;
	private String proxyPassword;
	private String proxyHost;
	private String proxyPort;
	private String dapsJWKSUrl;
	
	public String getTargetDirectory() {
		return targetDirectory;
	}
	public void setTargetDirectory(String targetDirectory) {
		this.targetDirectory = targetDirectory;
	}
	public String getDapsUrl() {
		return dapsUrl;
	}
	public void setDapsUrl(String dapsUrl) {
		this.dapsUrl = dapsUrl;
	}
	public String getKeyStoreName() {
		return keyStoreName;
	}
	public void setKeyStoreName(String keyStoreName) {
		this.keyStoreName = keyStoreName;
	}
	public String getKeyStorePassword() {
		return keyStorePassword;
	}
	public void setKeyStorePassword(String keyStorePassword) {
		this.keyStorePassword = keyStorePassword;
	}
	public String getKeystoreAliasName() {
		return keystoreAliasName;
	}
	public void setKeystoreAliasName(String keystoreAliasName) {
		this.keystoreAliasName = keystoreAliasName;
	}
	public String getConnectorUUID() {
		return connectorUUID;
	}
	public void setConnectorUUID(String connectorUUID) {
		this.connectorUUID = connectorUUID;
	}
	public String getProxyUser() {
		return proxyUser;
	}
	public void setProxyUser(String proxyUser) {
		this.proxyUser = proxyUser;
	}
	public String getProxyPassword() {
		return proxyPassword;
	}
	public void setProxyPassword(String proxyPassword) {
		this.proxyPassword = proxyPassword;
	}
	public String getProxyHost() {
		return proxyHost;
	}
	public void setProxyHost(String proxyHost) {
		this.proxyHost = proxyHost;
	}
	public String getProxyPort() {
		return proxyPort;
	}
	public void setProxyPort(String proxyPort) {
		this.proxyPort = proxyPort;
	}
	public String getDapsJWKSUrl() {
		return dapsJWKSUrl;
	}
	public void setDapsJWKSUrl(String dapsJWKSUrl) {
		this.dapsJWKSUrl = dapsJWKSUrl;
	}
	
}