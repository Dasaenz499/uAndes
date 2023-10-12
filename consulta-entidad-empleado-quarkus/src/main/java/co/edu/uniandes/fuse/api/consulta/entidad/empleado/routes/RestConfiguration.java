package co.edu.uniandes.fuse.api.consulta.entidad.empleado.routes;

import static org.apache.camel.model.rest.RestParamType.path;

import java.util.Set;

import org.apache.camel.CamelContext;
import org.apache.camel.Exchange;
import org.apache.camel.LoggingLevel;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.http.base.HttpOperationFailedException;
import org.apache.camel.model.dataformat.JsonLibrary;
import org.apache.camel.model.rest.RestBindingMode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import co.edu.uniandes.fuse.api.consulta.entidad.empleado.configurator.CamelConfig;
import co.edu.uniandes.fuse.api.consulta.entidad.empleado.models.respuesta.EntidadEmpleado;
import co.edu.uniandes.fuse.api.consulta.entidad.empleado.process.DatoEmpleadoAggregationStrategy;
import co.edu.uniandes.fuse.api.consulta.entidad.empleado.util.Constants;

import javax.inject.Inject;
import javax.ws.rs.Path;

/**
 * Configuraci�n REST: definici�n properties, datasources y configuraci�n rest
 *
 * @author CedEx Desarrollo de Software - DSIT - Universidad de los Andes
 * @since 2020-03-25
 */

//@ApplicationScoped

@Path("/api")
public class RestConfiguration extends RouteBuilder {

	public Logger LOG = LoggerFactory.getLogger("consultaEntidadEmpleadoLog");

	@Inject
	CamelConfig restCamelConfig;

	// @Default
	// private Environment env;

	@Override
	public void configure() throws Exception {

		// REST & SWAGGER CONFIGURATION
		restConfiguration().component("servlet").bindingMode(RestBindingMode.json)
				// .dataFormatProperty("prettyPrint", restCamelConfig.getApiPrettyPrint())
				.dataFormatProperty("prettyPrint", "true")
				// .port(env.getProperty("server.port", restCamelConfig.getApiPort()))
				// .contextPath(contextPath.substring(0, contextPath.length() - 2))
				.contextPath("/api")
				// .contextPath(restCamelConfig.getCamelServletMappingContextPath().substring(0,
				// restCamelConfig.getCamelServletMappingContextPath().length() - 2)
				.apiContextPath("/api-doc").apiProperty("api.title", restCamelConfig.apiTitle())
				.apiProperty("api.host", restCamelConfig.apiHost())
				.apiProperty("api.description", restCamelConfig.apiDescription())
				.apiProperty("api.version", restCamelConfig.apiVersion())
				.apiProperty("cors", restCamelConfig.apiCors());

		// REST & SWAGGER COMPONENTS

		rest("/empleado").description("Consulta de informaci&oacute;n de Entidad Empleado").consumes("application/json")
				.produces("application/json")

				.get("/{nro_documento}").description("Obtiene la entidad de un empleado dentro de Oracle Cloud HCM")
				.outType(EntidadEmpleado.class).param().name("nro_documento").type(path)
				.description("Numero de documento del empleado").dataType("String").required(Boolean.TRUE)
				.defaultValue("1111111111").endParam().responseMessage().code(200).message("Consulta Empleado exitosa")
				.endResponseMessage().to("direct:consultaEmpleado");

		/* ************************************************* */

		System.out.println("Valor de mi.propiedad: " + restCamelConfig.uriServiceHcmGender());

		// EXCEPTIONS
		onException(Exception.class).handled(true).log("${exception.stacktrace}")
				.setHeader(Exchange.HTTP_RESPONSE_CODE, constant(400))
				.setHeader(Exchange.HTTP_RESPONSE_TEXT, constant("Caracter(es) no permitidos")).setBody()
				.simple("{\"error\":{\"codigo\":\"${header.CamelHttpResponseCode}\",\"causa\":\"${header.CamelHttpResponseText}\",\"mensaje\":\"${exception.message}\"}}")
				.setHeader("Content-Type").constant("application/json").end();

		onException(HttpOperationFailedException.class).handled(true).log("${exception.stacktrace}")
				.setHeader(Exchange.HTTP_RESPONSE_CODE, constant(404))
				.setHeader(Exchange.HTTP_RESPONSE_TEXT, constant("Documento no encontrado")).setBody()
				.simple("{\"error\":{\"codigo\":\"${header.CamelHttpResponseCode}\",\"causa\":\"${header.CamelHttpResponseText}\",\"mensaje\":\"${exception.message}\"}}")
				.end();

		// REST & SWAGGER COMPONENTS
		// ROUTE EMPLEADOS
		from("direct:consultaEmpleado").log(LoggingLevel.INFO, LOG, "Inicio de la integracion").choice()
				.when(header("nro_documento").not().regex("^\\d{1,12}$")).setProperty("HttpErrorProperty")
				.constant(Constants.CODE_BAD_REQUEST)
				.throwException(new Exception(
						"Valor de nro_documento incorrecto (longitud minima 1, longitud maxima 12, caracteres permitidos: numericos)"))
				.end()

				.setProperty("documento").simple("${header.nro_documento}").setProperty("url")
				.simple(restCamelConfig.uriServiceHcmGUID1()).setProperty("urlHcmemp")
				.simple("${exchangeProperty.url.replace('$nroDocumento','${exchangeProperty.documento}')}")
				.removeHeaders("CamelHttp*").setHeader(Exchange.HTTP_METHOD).constant("GET")
				.setHeader("REST-Framework-Version").simple("2").to("direct:getHeaderAutenticacion")
				.log(LoggingLevel.INFO, LOG,
						"Route: API-ENTIDAD-EMPLEADO ::: PETICION WORKERS ::: ${exchangeProperty.urlHcmemp}")

				.toD("${exchangeProperty.urlHcmemp}").setHeader("count").jsonpath("$.count", true)
				.log("COUNT:${header.count}").choice().when(header("count").isEqualTo(0))
				.setProperty("HttpErrorProperty").constant(Constants.CODE_BAD_REQUEST)
				.throwException(
						new HttpOperationFailedException(null, 404, null, null, null, "Documento no encontrado"))
				.end()

				.toD("${exchangeProperty.urlHcmemp}").setProperty("datosBasicos").jsonpath("$.items[0]", true)
				.setProperty("idD").simple("${exchangeProperty.datosBasicos.get('links').get(0).get('href')}")
				.setProperty("id").simple("${exchangeProperty.idD.toString().split(\"/workers/\")[1]}")
				.setProperty("personNumber").simple("${exchangeProperty.datosBasicos.get('PersonNumber')}")

				.recipientList(constant("direct:getWorkersRelationships")).parallelProcessing()
				.aggregationStrategy(new DatoEmpleadoAggregationStrategy())
				.recipientList(constant("direct:getWorkersAssignments")).parallelProcessing()
				.aggregationStrategy(new DatoEmpleadoAggregationStrategy())
				.recipientList(constant(
						"direct:getWorkersNames,direct:getWorkersNationalId,direct:getWorkersGender,direct:getWorkersPhone,direct:getUser,direct:getCentroCosto,direct:getDepartment,direct:getPosition,direct:getJob,direct:getContract,direct:getSalaries,direct:getAddresses"))
				.parallelProcessing().aggregationStrategy(new DatoEmpleadoAggregationStrategy())

				.setBody(simple("resource:templates/EntidadEmpleadoResponse.vm")).convertBodyTo(String.class)
				.process(exchange -> {
					String jsonBody = exchange.getIn().getBody(String.class);
					String unescapedJsonBody = jsonBody.replaceAll("\\\\", "");
					exchange.getIn().setBody(unescapedJsonBody);
				}).unmarshal().json(JsonLibrary.Jackson)
				.log(LoggingLevel.INFO, LOG, "Route: API-ENTIDAD-EMPLEADO ::: SALIDA ${body}").to("mock:end");

		from("direct:getWorkersNames").setProperty("urlWorkerNames").simple(restCamelConfig.uriServiceHcmNames())
				.setProperty("urlWorkerNames")
				.simple("${exchangeProperty.urlWorkerNames.replace('$id','${exchangeProperty.id}')}")
				.setProperty("urlWorkerNames")
				.simple("${exchangeProperty.urlWorkerNames.replace('$today','${date:now:yyyy-MM-dd}')}")
				.log(LoggingLevel.INFO, LOG,
						"Route: API-ENTIDAD-EMPLEADO ::: PETICION HCM WORKER NAMES ${exchangeProperty.urlWorkerNames}")
				.toD("${exchangeProperty.urlWorkerNames}").setProperty("datosWorkerNames").jsonpath("$.items[0]", true)
				.to("mock:end");

		from("direct:getWorkersNationalId").setProperty("urlWorkerNationalId")
				.simple(restCamelConfig.uriServiceHcmNationalId()).setProperty("urlWorkerNationalId")
				.simple("${exchangeProperty.urlWorkerNationalId.replace('$id','${exchangeProperty.id}')}")
				.setProperty("urlWorkerNationalId")
				.simple("${exchangeProperty.urlWorkerNationalId.replace('$today','${date:now:yyyy-MM-dd}')}")
				.log(LoggingLevel.INFO, LOG,
						"Route: API-ENTIDAD-EMPLEADO ::: PETICION HCM WORKER NATIONAL ID ${exchangeProperty.urlWorkerNationalId}")
				.toD("${exchangeProperty.urlWorkerNationalId}").setProperty("datosWorkerNationalId")
				.jsonpath("$.items[0]", true).to("mock:end");

		from("direct:getAddresses").doTry().setProperty("urlAddresses").simple(restCamelConfig.uriServiceHcmAddresses())
				.setProperty("urlAddresses")
				.simple("${exchangeProperty.urlAddresses.replace('$id','${exchangeProperty.id}')}")
				.log(LoggingLevel.INFO, LOG,
						"Route: API-ENTIDAD-EMPLEADO ::: PETICION ADDRESSES ${exchangeProperty.urlAddresses}")
				.toD("${exchangeProperty.urlAddresses}").setProperty("datosAddresses").jsonpath("$.items[0]", true)
				.to("mock:end").doCatch(Exception.class).setProperty("datosAddresses").constant("null");

		from("direct:getWorkersGender").setProperty("urlWorkerGender").simple(restCamelConfig.uriServiceHcmGender())
				.setProperty("urlWorkerGender")
				.simple("${exchangeProperty.urlWorkerGender.replace('$id','${exchangeProperty.id}')}")
				.setProperty("urlWorkerGender")
				.simple("${exchangeProperty.urlWorkerGender.replace('$today','${date:now:yyyy-MM-dd}')}")
				.log(LoggingLevel.INFO, LOG,
						"Route: API-ENTIDAD-EMPLEADO ::: PETICION HCM WORKER GENDER ${exchangeProperty.urlWorkerGender}")
				.toD("${exchangeProperty.urlWorkerGender}").setProperty("datosWorkerGender")
				.jsonpath("$.items[0]", true).to("mock:end");

		from("direct:getWorkersPhone").setProperty("urlWorkerPhone").simple(restCamelConfig.uriServiceHcmPhone())
				.setProperty("urlWorkerPhone")
				.simple("${exchangeProperty.urlWorkerPhone.replace('$id','${exchangeProperty.id}')}")
				.setProperty("urlWorkerPhone")
				.simple("${exchangeProperty.urlWorkerPhone.replace('$today','${date:now:yyyy-MM-dd}')}")
				.log(LoggingLevel.INFO, LOG,
						"Route: API-ENTIDAD-EMPLEADO ::: PETICION HCM WORKER PHONE ${exchangeProperty.urlWorkerPhone}")
				.toD("${exchangeProperty.urlWorkerPhone}").setProperty("datosWorkerPhone").jsonpath("$.items[0]", true)
				.setProperty("workerPhone").simple("${exchangeProperty.datosWorkerPhone.get('PhoneNumber')}")
				.to("mock:end");

		from("direct:getUser").setProperty("urlWorkerUser").simple(restCamelConfig.uriServiceFscmUser())
				.setProperty("urlWorkerUser")
				.simple("${exchangeProperty.urlWorkerUser.replace('$personNumber','${exchangeProperty.personNumber}')}")
				.log(LoggingLevel.INFO, LOG,
						"Route: API-ENTIDAD-EMPLEADO ::: PETICION HCM WORKER USER ${exchangeProperty.urlWorkerUser}")
				.toD("${exchangeProperty.urlWorkerUser}").setProperty("datosWorkerUser").jsonpath("$.items[0]", true)
				.to("mock:end");

		from("direct:getWorkersRelationships").setProperty("urlWorkerRelationships")
				.simple(restCamelConfig.uriServiceHcmWorkersRelationships()).setProperty("urlWorkerRelationships")
				.simple("${exchangeProperty.urlWorkerRelationships.replace('$id','${exchangeProperty.id}')}")
				.setProperty("urlWorkerRelationships")
				.simple("${exchangeProperty.urlWorkerRelationships.replace('$today','${date:now:yyyy-MM-dd}')}")
				.log(LoggingLevel.INFO, LOG,
						"Route: API-ENTIDAD-EMPLEADO ::: PETICION HCM WORKER RELATIONSHIPS ${exchangeProperty.urlWorkerRelationships}")
				.toD("${exchangeProperty.urlWorkerRelationships}").setProperty("datosWorkerRelationships")
				.jsonpath("$.items[0]", true)
				.log(LoggingLevel.INFO, LOG,
						"Route: API-ENTIDAD-EMPLEADO ::: PETICION HCM WORKER RELATIONSHIPS PeriodOfServiceId ${exchangeProperty.datosWorkerRelationships.get('PeriodOfServiceId')}")
				.setProperty("periodOfServiceId")
				.simple("${exchangeProperty.datosWorkerRelationships.get('PeriodOfServiceId').toString()}")
				.to("mock:end");

		from("direct:getWorkersAssignments").setProperty("urlWorkerAssignments")
				.simple(restCamelConfig.uriServiceHcmWorkersAssignments()).setProperty("urlWorkerAssignments")
				.simple("${exchangeProperty.urlWorkerAssignments.replace('$id','${exchangeProperty.id}')}")
				.setProperty("urlWorkerAssignments")
				.simple("${exchangeProperty.urlWorkerAssignments.replace('$periodOfServiceId','${exchangeProperty.periodOfServiceId}')}")
				.log(LoggingLevel.INFO, LOG,
						"Route: API-ENTIDAD-EMPLEADO ::: PETICION HCM WORKER ASSIGNMENTS ${exchangeProperty.urlWorkerAssignments}")
				.toD("${exchangeProperty.urlWorkerAssignments}").setProperty("datosWorkerAssignments")
				.jsonpath("$.items[0]", true).setProperty("workerAssignmentsId")
				.simple("${exchangeProperty.datosWorkerAssignments.get('AssignmentId').toString()}")
				.setProperty("workerDepartmentId")
				.simple("${exchangeProperty.datosWorkerAssignments.get('DepartmentId').toString()}")
				.setProperty("workerPositionId")
				.simple("${exchangeProperty.datosWorkerAssignments.get('PositionId').toString()}")
				.setProperty("workerJobId").simple("${exchangeProperty.datosWorkerAssignments.get('JobId').toString()}")
				.setProperty("workerContractId")
				.simple("${exchangeProperty.datosWorkerAssignments.get('ContractId').toString()}")
				.setProperty("segmento")
				.simple("${exchangeProperty.datosWorkerAssignments.get('DefaultExpenseAccount')}")
				.setProperty("centroCosto").simple("${exchangeProperty.segmento.toString().split('\\.')[8]}")
				.to("mock:end");

		from("direct:getCentroCosto").doTry().setProperty("urlCentroCosto")
				.simple(restCamelConfig.uriServiceFscmAccount()).setProperty("urlFscmCentroCosto")
				.simple("${exchangeProperty.urlCentroCosto.replace('$account','${exchangeProperty.centroCosto}')}")
				.log(LoggingLevel.INFO, LOG,
						"Route: API-ENTIDAD-EMPLEADO ::: PETICION FINNANCIALS CENTRO COSTO ${exchangeProperty.urlFscmCentroCosto}")
				.toD("${exchangeProperty.urlFscmCentroCosto}").setProperty("datosCentroCosto")
				.jsonpath("$.items[0]", true).to("mock:end").doCatch(Exception.class).setProperty("datosCentroCosto")
				.constant("null");

		from("direct:getDepartment").setProperty("urlDepartment").simple(restCamelConfig.uriServiceHcmDepartment())
				.setProperty("urlhcmDepartment")
				.simple("${exchangeProperty.urlDepartment.replace('$organizationId','${exchangeProperty.workerDepartmentId}')}")
				.log(LoggingLevel.INFO, LOG,
						"Route: API-ENTIDAD-EMPLEADO ::: PETICION DEPARTMENT ${exchangeProperty.urlhcmDepartment}")
				.toD("${exchangeProperty.urlhcmDepartment}").setProperty("datosDepartment").jsonpath("$.items[0]", true)
				.to("mock:end");

		from("direct:getPosition").setProperty("urlPosition").simple(restCamelConfig.uriServiceHcmPosition())
				.setProperty("urlhcmPosition")
				.simple("${exchangeProperty.urlPosition.replace('$positionId','${exchangeProperty.workerPositionId}')}")
				.log(LoggingLevel.INFO, LOG,
						"Route: API-ENTIDAD-EMPLEADO ::: PETICION POSITION ${exchangeProperty.urlhcmPosition}")
				.toD("${exchangeProperty.urlhcmPosition}")
				// .log("datos position BODY ---->>>> ${body}")
				.setProperty("datosPosition").jsonpath("$.items[0]", true).to("mock:end");

		from("direct:getJob").setProperty("urlJob").simple(restCamelConfig.uriServiceHcmJob()).setProperty("urlhcmJob")
				.simple("${exchangeProperty.urlJob.replace('$jobId','${exchangeProperty.workerJobId}')}")
				.log(LoggingLevel.INFO, LOG,
						"Route: API-ENTIDAD-EMPLEADO ::: PETICION JOB ${exchangeProperty.urlhcmJob}")
				.toD("${exchangeProperty.urlhcmJob}").setProperty("datosJob").jsonpath("$.items[0]", true)
				.to("mock:end");

		from("direct:getContract").doTry().setProperty("urlContract").simple(restCamelConfig.uriServiceHcmContract())
				.setProperty("urlhcmContract")
				.simple("${exchangeProperty.urlContract.replace('$contractId','${exchangeProperty.workerContractId}')}")
				.log(LoggingLevel.INFO, LOG,
						"Route: API-ENTIDAD-EMPLEADO ::: PETICION CONTRACT ${exchangeProperty.urlhcmContract}")
				.toD("${exchangeProperty.urlhcmContract}").setProperty("datosContract").jsonpath("$.items[0]", true)
				.to("mock:end").doCatch(Exception.class).setProperty("datosContract").constant("null");

		from("direct:getSalaries").doTry().setProperty("urlSalaries").simple(restCamelConfig.uriServiceHcmSalaries())
				.setProperty("urlSalaries")
				.simple("${exchangeProperty.urlSalaries.replace('$assignmentId','${exchangeProperty.workerAssignmentsId}')}")
				.log(LoggingLevel.INFO, LOG,
						"Route: API-ENTIDAD-EMPLEADO ::: PETICION SALARIES ${exchangeProperty.urlSalaries}")
				.toD("${exchangeProperty.urlSalaries}").setProperty("datosSalaries")
				.jsonpath("$.items[0].salaryComponents[0].ComponentReasonCode", true).to("mock:end")
				.doCatch(Exception.class).setProperty("datosSalaries").constant("null");

		from("direct:getHeaderAutenticacion").setBody()
				.simple(restCamelConfig.ociLoginUser() + ":" + restCamelConfig.ociLoginPassword()).marshal().base64()
				.convertBodyTo(byte[].class, "iso-8859-1").setBody(body().regexReplaceAll("\\r\\n", ""))
				.setHeader("Authorization").simple("Basic ${body}").to("mock:end");

	}

	public void addTemplatedRoutesToCamelContext(CamelContext context) throws Exception {
		// TODO Auto-generated method stub

	}

	public Set<String> updateRoutesToCamelContext(CamelContext context) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}
}
