package co.edu.uniandes.fuse.api.consulta.entidad.empleado.process;

import org.apache.camel.AggregationStrategy;
import org.apache.camel.Exchange;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Esta clase contiene el metodo aggregate que permite mantener las propiedades
 * de los exchanges de las rutas direct:getCentroCosto direct:getDepartment,
 * direct:getPosition, direct:getJob, direct:getContract, datosSalaries y  datosAddresses de manera que no se
 * pierdan durante el enrutamiento ya que algunas de sus propiedades son
 * requeridas en los siguientes servicios.
 *
 *
 */
public class DatoEmpleadoAggregationStrategy implements AggregationStrategy {
	
	Logger logger = LoggerFactory.getLogger(DatoEmpleadoAggregationStrategy.class);

    public Exchange aggregate(Exchange oldExchange, Exchange newExchange) {
    	
        if (oldExchange == null) {
            return newExchange;
        }
        if (newExchange.getProperties().containsKey("datosCentroCosto")) {
            oldExchange.setProperty("datosCentroCosto", newExchange.getProperty("datosCentroCosto"));
        }
        if (newExchange.getProperties().containsKey("datosDepartment")) {
            oldExchange.setProperty("datosDepartment", newExchange.getProperty("datosDepartment"));
        }
        if (newExchange.getProperties().containsKey("datosPosition")) {
            oldExchange.setProperty("datosPosition", newExchange.getProperty("datosPosition"));
        }
        if (newExchange.getProperties().containsKey("datosJob")) {
            oldExchange.setProperty("datosJob", newExchange.getProperty("datosJob"));
        }
        if (newExchange.getProperties().containsKey("datosContract")) {
            oldExchange.setProperty("datosContract", newExchange.getProperty("datosContract"));
        }
        if (newExchange.getProperties().containsKey("datosSalaries")) {
            oldExchange.setProperty("datosSalaries", newExchange.getProperty("datosSalaries"));
        }
        if (newExchange.getProperties().containsKey("datosAddresses")) {
            oldExchange.setProperty("datosAddresses", newExchange.getProperty("datosAddresses"));
        }
        if (newExchange.getProperties().containsKey("datosWorkerUser")) {
            oldExchange.setProperty("datosWorkerUser", newExchange.getProperty("datosWorkerUser"));
        }
        if(newExchange.getProperties().containsKey("datosWorkerAssignments")) {       	       	
            oldExchange.setProperty("datosWorkerAssignments", newExchange.getProperty("datosWorkerAssignments"));
        }
        if(newExchange.getProperties().containsKey("periodOfServiceId")) {    	
            oldExchange.setProperty("periodOfServiceId", newExchange.getProperty("periodOfServiceId"));
        }
        
        if (newExchange.getProperties().containsKey("workerAssignmentsId")) {
            oldExchange.setProperty("workerAssignmentsId", newExchange.getProperty("workerAssignmentsId"));
        }
        if (newExchange.getProperties().containsKey("workerDepartmentId")) {
            oldExchange.setProperty("workerDepartmentId", newExchange.getProperty("workerDepartmentId"));
        }
        if (newExchange.getProperties().containsKey("workerPositionId")) {
            oldExchange.setProperty("workerPositionId", newExchange.getProperty("workerPositionId"));
        }
        if (newExchange.getProperties().containsKey("workerJobId")) {
            oldExchange.setProperty("workerJobId", newExchange.getProperty("workerJobId"));
        }
        if (newExchange.getProperties().containsKey("workerContractId")) {
            oldExchange.setProperty("workerContractId", newExchange.getProperty("workerContractId"));
        }
        
        if(newExchange.getProperties().containsKey("datosWorkerNames")) {
            oldExchange.setProperty("datosWorkerNames", newExchange.getProperty("datosWorkerNames"));
        }
        if(newExchange.getProperties().containsKey("datosWorkerNationalId")) {
            oldExchange.setProperty("datosWorkerNationalId", newExchange.getProperty("datosWorkerNationalId"));
        }
        if(newExchange.getProperties().containsKey("datosWorkerGender")) {
            oldExchange.setProperty("datosWorkerGender", newExchange.getProperty("datosWorkerGender"));
        }
        if(newExchange.getProperties().containsKey("datosWorkerPhone")) {
            oldExchange.setProperty("datosWorkerPhone", newExchange.getProperty("datosWorkerPhone"));
        }
        if(newExchange.getProperties().containsKey("datosWorkerRelationships")) {
            oldExchange.setProperty("datosWorkerRelationships", newExchange.getProperty("datosWorkerRelationships"));
        }
        
        return oldExchange;
    }

}