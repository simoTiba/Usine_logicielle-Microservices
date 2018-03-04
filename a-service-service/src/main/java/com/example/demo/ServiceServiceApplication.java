package com.example.demo;

import java.util.Collection;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.transaction.Transactional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.context.ApplicationContext;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;


@EnableJpaRepositories 
@EnableEurekaClient
@SpringBootApplication
@EnableAutoConfiguration
public class ServiceServiceApplication {
	
	public static void main(String[] args) {
		ApplicationContext ctx=(ApplicationContext) SpringApplication.run(ServiceServiceApplication.class, args);
		ServiceRepository ServiceRepository = ctx.getBean(ServiceRepository.class);
		Service serv1=new Service("jlong", "prov1", "etat1");
		Service serv2=new Service("jlong", "prov2", "etat2");
		ServiceRepository.save(serv1);
		ServiceRepository.save(serv2);
		ServiceRepository.findAll().forEach(x->System.out.println(x.getId()));		
	}	
}

@RepositoryRestResource
interface ServiceRepository extends JpaRepository<Service, Long>{
	Collection<Service> findByueId(String ueId);
	
	@Modifying
    @Transactional
    @Query("delete from Service s where s.ueId = ?1")
    void deleteServiceByUeId(String ueId);
}


@RestController
class ServiceRestController{
	
	@Autowired
	private ServiceRepository serviceRepository;
	
	public static final Logger logger = LoggerFactory.getLogger(ServiceRestController.class);

	@Autowired
	ServiceRestController(ServiceRepository serviceRepository) {
		this.serviceRepository = serviceRepository;
	}
	
	/**************** AFICHAGE ****************************/
	@GetMapping("/services/{ueId}")
	Collection<Service> Service(@PathVariable String ueId){
		return this.serviceRepository.findByueId(ueId);
	}
	
	
	/**************** AJOUTER ****************************/
	@PostMapping("/addService")
    public ResponseEntity <?> addService(@RequestBody Service service) {
		serviceRepository.save(service);
            return ResponseEntity.status(HttpStatus.CREATED).build();
    }
	
	/**************** SUPPRIMER ****************************/
	@DeleteMapping("/deleteServices/{ueId}")
	public void deleteService(@PathVariable String ueId) {
		serviceRepository.deleteServiceByUeId(ueId);
	}
	
	
	/**************** PUT ****************************/
	@PutMapping("/services/info")
	public @ResponseBody String updateDiplome(@RequestBody Service service){
	 logger.info(service.toString());
	 return "ok";
	}
}


@Entity
class Service {
	@Id
	@GeneratedValue
	private	Long id;
	private String ueId;
	private String proxyId;
	private Long enseignantId;
	private boolean realise; 
	private String previsionel;
	private String etat;

	public Service(String ueId, String previsionel, String etat) {
		super();
		this.ueId = ueId;
		this.previsionel = previsionel;
		this.etat = etat;
	}
	
	public String getueId() {
		return ueId;
	}

	public void setueId(String ueId) {
		this.ueId = ueId;
	}

	public Service() {
		super();
	}
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getProxyId() {
		return proxyId;
	}
	public void setProxyId(String proxyId) {
		this.proxyId = proxyId;
	}

	public boolean isRealise() {
		return realise;
	}
	public void setRealise(boolean realise) {
		this.realise = realise;
	}
	public String getPrevisionel() {
		return previsionel;
	}
	public void setPrevisionel(String previsionel) {
		this.previsionel = previsionel;
	}
	public String getEtat() {
		return etat;
	}
	public void setEtat(String etat) {
		this.etat = etat;
	}
	@Override
	public String toString() {
		return "Service [id=" + id + ", proxyId=" + proxyId + ", enseignatId=" + enseignantId + ", realise=" + realise
				+ ", previsionel=" + previsionel + ", etat=" + etat + "]";
	}
	public Long getEnseignantId() {
		return enseignantId;
	}
	public void setEnseignantId(Long enseignantId) {
		this.enseignantId = enseignantId;
	}

	
}



