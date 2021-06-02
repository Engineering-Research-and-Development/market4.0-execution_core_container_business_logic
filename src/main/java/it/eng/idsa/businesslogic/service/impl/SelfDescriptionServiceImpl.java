
package it.eng.idsa.businesslogic.service.impl;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;

import javax.annotation.PostConstruct;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import de.fraunhofer.iais.eis.BaseConnectorBuilder;
import de.fraunhofer.iais.eis.Connector;
import de.fraunhofer.iais.eis.ConnectorEndpointBuilder;
import de.fraunhofer.iais.eis.ConnectorUnavailableMessageBuilder;
import de.fraunhofer.iais.eis.ConnectorUpdateMessageBuilder;
import de.fraunhofer.iais.eis.Message;
import de.fraunhofer.iais.eis.QueryLanguage;
import de.fraunhofer.iais.eis.QueryMessageBuilder;
import de.fraunhofer.iais.eis.QueryScope;
import de.fraunhofer.iais.eis.Resource;
import de.fraunhofer.iais.eis.ResourceCatalog;
import de.fraunhofer.iais.eis.ResourceCatalogBuilder;
import de.fraunhofer.iais.eis.SecurityProfile;
import de.fraunhofer.iais.eis.util.ConstraintViolationException;
import de.fraunhofer.iais.eis.util.TypedLiteral;
import de.fraunhofer.iais.eis.util.Util;
import it.eng.idsa.businesslogic.configuration.SelfDescriptionConfiguration;
import it.eng.idsa.businesslogic.service.DapsTokenProviderService;
import it.eng.idsa.businesslogic.service.ResourceDataAppService;
import it.eng.idsa.businesslogic.service.SelfDescriptionService;
import it.eng.idsa.multipart.processor.MultipartMessageProcessor;
import it.eng.idsa.multipart.util.DateUtil;

/**
 * @author Antonio Scatoloni and Gabriele De Luca
 */

@Service
public class SelfDescriptionServiceImpl implements SelfDescriptionService {
	
	private static final Logger logger = LoggerFactory.getLogger(SelfDescriptionServiceImpl.class);
	
	private DapsTokenProviderService dapsProvider;
	private SelfDescriptionConfiguration selfDescriptionConfiguration;
	private Connector connector;
	private URI issuerConnectorURI;
	private ResourceDataAppService dataAppService;

	@Autowired
	public SelfDescriptionServiceImpl(
			SelfDescriptionConfiguration selfDescriptionConfiguration,
			ResourceDataAppService dataAppService,
			DapsTokenProviderService dapsProvider) {
		this.selfDescriptionConfiguration = selfDescriptionConfiguration;
		this.dataAppService = dataAppService;
		this.dapsProvider = dapsProvider;
	}

	@PostConstruct
	public void initConnector() throws ConstraintViolationException {
		issuerConnectorURI = selfDescriptionConfiguration.getConnectorURI();
		this.connector = new BaseConnectorBuilder(issuerConnectorURI)
				._maintainer_(selfDescriptionConfiguration.getMaintainer())
				._curator_(selfDescriptionConfiguration.getCurator())
				._resourceCatalog_(this.getCatalog())
				._securityProfile_(SecurityProfile.BASE_SECURITY_PROFILE)
				._inboundModelVersion_(Util.asList(new String[] { selfDescriptionConfiguration.getInformationModelVersion() }))
				._title_(Util.asList(new TypedLiteral(selfDescriptionConfiguration.getTitle())))
				._description_(Util.asList(new TypedLiteral(selfDescriptionConfiguration.getDescription())))
				._outboundModelVersion_(selfDescriptionConfiguration.getInformationModelVersion())
				._hasDefaultEndpoint_(new ConnectorEndpointBuilder(selfDescriptionConfiguration.getDefaultEndpoint())
						._accessURL_(selfDescriptionConfiguration.getDefaultEndpoint())
						.build())
//				._hasEndpoint_(Util.asList(new ConnectorEndpointBuilder(new URI("https://someURL/incoming-data-channel/receivedMessage")).build()))
				.build();
	}

	public Connector getConnector() throws ConstraintViolationException, URISyntaxException {
		if (null == this.connector) {
			this.initConnector();
		}
		return this.connector;
	}

	@Override
	public String getConnectorSelfDescription() {
		String result = null;
		try {
			result = MultipartMessageProcessor.serializeToJsonLD(this.connector);
		} catch (IOException e) {
			logger.error("Error while serializing", e);
		}
		return result;
	}

//	private Resource getResource() throws ConstraintViolationException, URISyntaxException {
//		Resource offeredResource = (new ResourceBuilder())
//				._title_(Util.asList(
//						new TypedLiteral(selfDescriptionConfiguration.getSelfDescription().getResource().getTitle())))
//				._description_(Util.asList(
//						new TypedLiteral(selfDescriptionConfiguration.getSelfDescription().getResource().getDescription())))
////				._contractOffer_(getContractOffers())
//				._contentType_(ContentType.SCHEMA_DEFINITION)
//				._keyword_(Util.asList(new TypedLiteral("Engineering Ingegneria Informatica SpA"), 
//						new TypedLiteral("broker"), new TypedLiteral("trueConnector")))
//				._version_("1.0.0")
//				._language_(Util.asList(Language.EN, Language.IT))
//				.build();
//		return offeredResource;
//	}

//	private ArrayList<ContractOffer> getContractOffers() throws ConstraintViolationException, URISyntaxException {
//		Permission permission = new PermissionBuilder(
//				new URI(selfDescriptionConfiguration.getSelfDescription().getContractOffer().getPermission())).build();
//		ContractOffer contractOffer = new ContractOfferBuilder(new URI("http://example.com/ids-profile/1234"))
//				._provider_(new URI(selfDescriptionConfiguration.getSelfDescription().getContractOffer().getProvider()))
//				._permission_(Util.asList(permission))
//				.build();
//		
//			ArrayList<ContractOffer> contractOfferList = new ArrayList<>();
//			contractOfferList.add(contractOffer);
//			return contractOfferList;
//	}

	private java.util.List<ResourceCatalog> getCatalog() {
		java.util.List<ResourceCatalog> catalogList = null;
		try {
			Resource resource = dataAppService.getResourceFromDataApp();
			ResourceCatalog catalog = null;
			catalog = (new ResourceCatalogBuilder()
					._offeredResource_(Util.asList(new Resource[] { resource }))
					.build());
			catalogList = new ArrayList<>();
			catalogList.add(catalog);
		} catch (ConstraintViolationException e) {
			e.printStackTrace();
		}
		return catalogList;
	}

	@Override
	public Message getConnectorAvailbilityMessage() {
		return new ConnectorUpdateMessageBuilder()
			._senderAgent_(selfDescriptionConfiguration.getSenderAgent())
			._issued_(DateUtil.now())
			._issuerConnector_(issuerConnectorURI)
			._modelVersion_(selfDescriptionConfiguration.getInformationModelVersion())
			._securityToken_(dapsProvider.getDynamicAtributeToken())
			._affectedConnector_(connector.getId())
			.build();
	}


	@Override
	public Message getConnectorUpdateMessage() {
		return new ConnectorUpdateMessageBuilder()
				._senderAgent_(selfDescriptionConfiguration.getSenderAgent())
				._modelVersion_(selfDescriptionConfiguration.getInformationModelVersion())
				._issuerConnector_(issuerConnectorURI)
				._issued_(DateUtil.now())
				._securityToken_(dapsProvider.getDynamicAtributeToken())
				._affectedConnector_(connector.getId())
				.build();
	}

	@Override
	public Message getConnectorUnavailableMessage() {

		// Mandatory fields are: affectedConnector, securityToken, issuerConnector, senderAgent, modelVersion, issued
		
		return new ConnectorUnavailableMessageBuilder()
				._issued_(DateUtil.now())
				._modelVersion_(selfDescriptionConfiguration.getInformationModelVersion())
				._issuerConnector_(issuerConnectorURI)
				._senderAgent_(selfDescriptionConfiguration.getSenderAgent())
				._securityToken_(dapsProvider.getDynamicAtributeToken())
				._affectedConnector_(connector.getId())
				.build();
	}

	@Override
	public Message getConnectorInactiveMessage() {
		return new ConnectorUnavailableMessageBuilder()
				._issued_(DateUtil.now())
				._modelVersion_(selfDescriptionConfiguration.getInformationModelVersion())
				._issuerConnector_(issuerConnectorURI)
				._senderAgent_(selfDescriptionConfiguration.getSenderAgent())
				._securityToken_(dapsProvider.getDynamicAtributeToken())
				._affectedConnector_(connector.getId())
				.build();
	}

	@Override
	public Message getConnectorQueryMessage() {
		return new QueryMessageBuilder()
				._senderAgent_(selfDescriptionConfiguration.getSenderAgent())
				._modelVersion_(selfDescriptionConfiguration.getInformationModelVersion())
				._issuerConnector_(issuerConnectorURI)
				._issued_(DateUtil.now())
				._securityToken_(dapsProvider.getDynamicAtributeToken())
				._queryLanguage_(QueryLanguage.SPARQL)
				._queryScope_(QueryScope.ALL)
				.build();
	}

}
