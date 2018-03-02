package com.example.demo;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.ImportAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;
@EnableAutoConfiguration
@SpringBootApplication
public class EnseignantServiceApplication {

	public static void main(String[] args) {
		/* ApplicationContext ctx = */ SpringApplication.run(EnseignantServiceApplication.class, args);
		/*EnseignantRepository EnseignantRepository = ctx.getBean(EnseignantRepository.class);
		
		Enseignant m1 = new Enseignant("test", "simo1", "");
		Enseignant m2 = new Enseignant("test", "simo2", "");
		
		EnseignantRepository.save(m1);
		EnseignantRepository.save(m2);
		
		EnseignantRepository.findAll().forEach(m->System.out.println(m.getPrenom()));*/

	}
//	
//	@Autowired
//	void setMessage(@Value("${message}") String m) {
//		System.out.println("message = "+m);
//	}
	
//	@Bean
//	CommandLineRunner init(EnseignantRepository EnseignantRepository) {
//		return args->Arrays.asList("jlong,rwinch,dsyer,pwebb,sgibb".split(",")).forEach(
//						proxyId->Arrays.asList("Dave,Syer;Phil,Webb;Juergen,Hoeller".split(";"))
//							.stream()
//							.map(n -> n.split(","))
//							.forEach(name -> EnseignantRepository.save(new Enseignant(
//									proxyId, name[0], name[1]))));
//	}
		
}


@RepositoryRestResource
interface EnseignantRepository extends JpaRepository<Enseignant, Long>{
	Collection<Enseignant> findByproxyId(String proxyId);
}


@RestController
class EnseignantRestController{
	@Autowired
	private EnseignantRepository EnseignantRepository;
	
	@RequestMapping("/enseignants/{proxyId}")
	Collection<Enseignant> Enseignants(@PathVariable String proxyId){
		return this.EnseignantRepository.findByproxyId(proxyId);
	}
}

@Entity
class Enseignant{
	@Id
	@GeneratedValue
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