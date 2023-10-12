package co.edu.uniandes.fuse.api.consulta.entidad.empleado.configurator;

import javax.enterprise.inject.Default;

import io.smallrye.config.ConfigMapping;

@Default
@ConfigMapping(prefix = "empleado")
public interface CamelConfig {
			
	String apiHost();
	String apiPort();
	String apiName();
	String apiTitle();
	String apiDescription();
	String apiCors();
	String apiVersion();
	String apiContactName();
	String apiContactEmail();
	String apiPrettyPrint();

	String ociLoginUser();
	String ociLoginPassword();
	
	String uriServiceHcmGUID1();
	String uriServiceHcmNames();
	String uriServiceHcmNationalId();
	String uriServiceHcmGender();
	String uriServiceHcmPhone();
	String uriServiceFscmUser();

	String uriPathHcmUsers();
	String uriHostHcm();
	String uriPathHcmWorkers();
	String uriPathFscmAccount();
	String uriPathHcmDepartment();
	String uriPathHcmPosition();
	String uriPathHcmJob();
	String uriPathHcmContract();
	String uriPathHcmSalaries();
	String uriServiceHcmWorkersAssignments();
	String uriServiceHcmWorkersRelationships();
	String uriServiceFscmAccount();
	String uriServiceHcmDepartment();
	String uriServiceHcmPosition();
	String uriServiceHcmJob();
	String uriServiceHcmContract();
	String uriServiceHcmSalaries();
	String uriServiceHcmAddresses();

	String auditApi();
	String authApiEnable();
	String authApiRealm();
	String authApiRole();
	
	String camelServletMappingContextPath();
	String camelContextName();
		
}
