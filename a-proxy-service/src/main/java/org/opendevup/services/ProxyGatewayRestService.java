package org.opendevup.services;

import java.util.Arrays;
import java.util.Collection;


import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;



@Configuration
class Myconfiguration{
	@LoadBalanced
	@Bean
	public RestTemplate restTemplate() {
	    return new RestTemplate();
	}
}

// @Configuration
// class Myconfiguration_2{
// 	@LoadBalanced
// 	@Bean
// 	diplomeClient diplomeClient() {
// 	return new diplomeClient() {
		
// 		private diplomeClient diplomeClient;

// 		@Override
// 		public
// 		Collection<Diplome> getDips(String userId) {
// 			return this.diplomeClient.getDips(userId);
// 		}
// 	};
// }
// 	@LoadBalanced
// 	@Bean
// 	serviceClient serviceClient() {
// 	return new serviceClient() {
		
// 		private serviceClient serviceClient;

// 		@Override
// 		public Collection<Service> getService(String ueId) {
// 			return this.serviceClient.getService(ueId);
// 		}
// 	};
// }
	
// }




@RestController
public class ProxyGatewayRestService {

	@RequestMapping(value = "/")
	public String available() {
		return "Welcome To Proxy Service!";
	}
	
//@Autowired
//private RestTemplate restTemplate;

//@RequestMapping(value="/services") 
//public Collection<Service> listSocietes(){
//
//	ParameterizedTypeReference<Resources<Service>> responseType= new ParameterizedTypeReference<Resources<Service>>() { };
//	return restTemplate.exchange("http://services-service/services", HttpMethod.GET, null,responseType).getBody().getContent();
//}


//@RequestMapping(value="/enseignants")
//public Collection<Enseignant> listEnseignant(){
//	
//	ParameterizedTypeReference<Resources<Enseignant>> responseType= new ParameterizedTypeReference<Resources<Enseignant>>() { };
//	return restTemplate.exchange("http://enseignant-service/enseignants", HttpMethod.GET, null,responseType).getBody().getContent();
//}

//@RequestMapping(value="/diplomes")
//public Collection<Diplome> listDiplomes(){
//	
//	ParameterizedTypeReference<Resources<Diplome>> responseType= new ParameterizedTypeReference<Resources<Diplome>>() { };
//	return restTemplate.exchange("http://diplome-service/diplomes", HttpMethod.GET, null,responseType).getBody().getContent();
//}

@Autowired
private IntegrationClient integrationClient;

@RequestMapping("/ServiceByDiplome/{ueId}")
proxy proxy(@PathVariable String ueId) { 
   return new proxy(ueId, this.integrationClient.getService(ueId),this.integrationClient.getDips(ueId));
	}

}


@FeignClient("diplome-service")
interface diplomeClient{
	@RequestMapping(method = RequestMethod.GET, value = "/diplomes/{ueId}")
	Collection<Diplome> getDips(@PathVariable("ueId") String ueId);
}

@FeignClient("services-service")
interface serviceClient{
	@RequestMapping(method = RequestMethod.GET, value = "/services/{ueId}")
	Collection<Service> getService(@PathVariable("ueId") String ueId);
}

@Component
class FeignExample implements CommandLineRunner {

    private Log log = LogFactory.getLog(this.getClass().getName());
    
    @Override
    public void run(String... strings) throws Exception {

        log.info("------------------------------");
        log.info("Feign Example");

    }
    
 }

@Component
class IntegrationClient {

    @Autowired
    private diplomeClient diplomeClient;
    
    @Autowired
    private serviceClient serviceClient;
    

    public Collection<Diplome> getDiplomesFallback(String ueId) {
        System.out.println("getDiplomesFallback");
        return Arrays.asList();
    }

    @HystrixCommand(fallbackMethod = "getDiplomesFallback")
    public Collection<Diplome> getDips(String ueId) {
        return this.diplomeClient.getDips(ueId);
    }
    
    public Collection<Service> getServiceFallback(String ueId) {
        System.out.println("getServiceFallback");
        return Arrays.asList();
    }

    @HystrixCommand(fallbackMethod = "getServiceFallback")
    public Collection<Service> getService(String ueId) {
        return this.serviceClient.getService(ueId);
    }

}


 class Enseignant{
	
	private Long id;
	private String proxyId, nom, prenom;
	public Enseignant() {

	}
	public Enseignant(String proxyId, String nom, String prenom) {
		super();
		this.proxyId = proxyId;
		this.nom = nom;
		this.prenom = prenom;
	}
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getproxyId() {
		return proxyId;
	}
	public void setproxyId(String proxyId) {
		this.proxyId = proxyId;
	}
	public String getNom() {
		return nom;
	}
	public void setNom(String nom) {
		this.nom = nom;
	}
	public String getPrenom() {
		return prenom;
	}
	public void setPrenom(String prenom) {
		this.prenom = prenom;
	}
	@Override
	public String toString() {
		return "Enseignant [id=" + id + ", proxyId=" + proxyId + ", nom=" + nom + ", prenom=" + prenom + "]";
	}
	
}
 
  class Service {
	private String ueId;
	private	Long id;
	private String proxyId;
	private Long enseignantId;
	private boolean realise; 
	private String previsionel;
	private String etat;
	private Collection<Diplome> dips;
	
	public Service(String ueId, String previsionel, String etat) {
		super();
		this.ueId = ueId;
		this.previsionel = previsionel;
		this.etat = etat;
	}
	
	public Service(String ueId, Collection<Diplome> dips) {
		super();
		this.ueId = ueId;
		this.dips = dips;
	}



	public Service() {
		super();
		// TODO Auto-generated constructor stub
	}
	
	public Long getEnseignantId() {
		return enseignantId;
	}

	public void setEnseignantId(Long enseignantId) {
		this.enseignantId = enseignantId;
	}

	public Collection<Diplome> getDips() {
		return dips;
	}

	public void setDips(Collection<Diplome> dips) {
		this.dips = dips;
	}

	public String getUeId() {
		return ueId;
	}
	public void setUeId(String ueId) {
		this.ueId = ueId;
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
}

class Diplome{
	private Long id;
	private String ueId;
	private String semestre;
	private String volumehoraire;
	public Diplome(String ueId, String semestre) {
		super();
		this.ueId = ueId;
		this.semestre = semestre;
	}
	public Diplome(String semestre) {
		super();
		this.semestre = semestre;
	}
	public Diplome() {
		super();
		// TODO Auto-generated constructor stub
	}
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getUeId() {
		return ueId;
	}
	public void setUeId(String ueId) {
		this.ueId = ueId;
	}
	public String getSemestre() {
		return semestre;
	}
	public void setSemestre(String semestre) {
		this.semestre = semestre;
	}
	public String getVolumehoraire() {
		return volumehoraire;
	}
	public void setVolumehoraire(String volumehoraire) {
		this.volumehoraire = volumehoraire;
	}
	@Override
	public String toString() {
		return "Diplome [id=" + id + ", UeId=" + ueId + ", semestre=" + semestre + ", volumehoraire=" + volumehoraire
				+ "]";
	}	
}

class proxy{
	private String ueId;
	private String proxyId;
	private Collection<Enseignant> enseignants;
	private Collection<Diplome> dips;
	private Collection<Service> Service;
	public proxy(String ueId, Collection<Service> Service, Collection<Diplome> diplomes) {
		super();
		this.ueId = ueId;
		this.dips = diplomes;
		this.Service = Service;
	}
	
	public String getUeId() {
		return ueId;
	}
	@JsonIgnore
	public String getProxyId() {
		return proxyId;
	}
	@JsonIgnore
	public Collection<Enseignant> getEnseignants() {
		return enseignants;
	}
	public Collection<Diplome> getDips() {
		return dips;
	}
	public Collection<Service> getService() {
		return Service;
	}

	
}


	

