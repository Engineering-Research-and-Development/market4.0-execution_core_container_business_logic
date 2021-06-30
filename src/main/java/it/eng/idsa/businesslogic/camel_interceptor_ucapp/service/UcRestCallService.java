package it.eng.idsa.businesslogic.camel_interceptor_ucapp.service;

import it.eng.idsa.businesslogic.camel_interceptor_ucapp.model.IdsUseObject;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface UcRestCallService {
	
    @POST("/enforce/usage/use")
    public Call<Object>  enforceUsageControl(@Body IdsUseObject idsUseObject);
}
