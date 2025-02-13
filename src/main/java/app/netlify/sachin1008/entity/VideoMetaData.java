package app.netlify.sachin1008.entity;



import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

@Entity
public class VideoMetaData {
	
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long videoId;
	
	private String videoName;
	
	private String videlFilePath;

}
