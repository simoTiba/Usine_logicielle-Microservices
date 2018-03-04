package org.opendevup;


import java.util.Arrays;
import java.util.Collection;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.transaction.Transactional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.context.annotation.Bean;
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
public class DiplomeServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(DiplomeServiceApplication.class, args);
		
		}
	@Bean
	CommandLineRunner init(DiplomeRepository diplomeRepository) {
		return args->Arrays.asList("jlong,rwinch,dsyer,pwebb,sgibb".split(","))
				.forEach(ueId->Arrays.asList("Dave,Syer;Phil,Webb;Juergen,Hoeller".split(";"))
						.stream().map(n -> n.split(",")).forEach(name -> diplomeRepository.save(new Diplome(ueId,name[0],name[1]))));
	}

}

@RepositoryRestResource
interface DiplomeRepository extends JpaRepository<Diplome, Long>{
	Collection<Diplome> findByUeId(String proxyId);

	@Modifying
    @Transactional
    @Query("delete from Diplome d where d.ueId = ?1")
    void deleteDiplomesByUeId(String ueId);
}

@RestController
class DiplomeRestController{
	@Autowired
	private DiplomeRepository diplomeRepository;	
	
	public static final Logger logger = LoggerFactory.getLogger(DiplomeRestController.class);
	
	@Autowired
	DiplomeRestController(DiplomeRepository diplomeRepository) {
		this.diplomeRepository = diplomeRepository;
	}
	
	/**************** AFICHAGE ****************************/
	@GetMapping("/diplomes/{ueId}")
	Collection<Diplome> Diplomes(@PathVariable String ueId){
		return this.diplomeRepository.findByUeId(ueId);
	}
	
	
	/**************** AJOUTER ****************************/
	@PostMapping("/addDiplome")
    public ResponseEntity <?> addDiplome(@RequestBody Diplome dip) {
          diplomeRepository.save(dip);
            return ResponseEntity.status(HttpStatus.CREATED).build();
    }
	
	
	/**************** SUPPRIMER ****************************/
	@DeleteMapping("/deleteDiplomes/{ueId}")
	public void deleteDiplome(@PathVariable String ueId) {
		diplomeRepository.deleteDiplomesByUeId(ueId);
	}
	
	
	/**************** PUT ****************************/
	@PutMapping("/diplomes/info")
	public @ResponseBody String updateDiplome(@RequestBody Diplome diplome){
	 logger.info(diplome.toString());
	 return "ok";
	}
	
	
}

@Entity
class Diplome{
	@Id
	@GeneratedValue
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
	public Diplome(String ueId2, String string, String string2) {
		this.ueId=ueId2;
		this.semestre=string;
		this.volumehoraire=string2;
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
	
	
	

	

