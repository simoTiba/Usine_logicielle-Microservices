package com.example.demo;

import java.util.Arrays;
import java.util.Collection;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Transient;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;



@EnableEurekaClient
@SpringBootApplication
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
}


@RestController
class EnseignantRestController{
	@Autowired
	private ServiceRepository serviceRepository;
	
	@RequestMapping("/services/{ueId}")
	Collection<Service> Service(@PathVariable String ueId){
		return this.serviceRepository.findByueId(ueId);
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



