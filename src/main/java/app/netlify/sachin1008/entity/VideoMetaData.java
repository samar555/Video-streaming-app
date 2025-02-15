package app.netlify.sachin1008.entity;



import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class VideoMetaData {
	
	
	@Id
	private String videoId;
	
	private String videoName;
	
	private String videoDescription;
	
	private String contentType;
	
	private String videoFilePath;

}
