package app.netlify.sachin1008.repos;

import org.springframework.data.jpa.repository.JpaRepository;

import app.netlify.sachin1008.entity.VideoMetaData;

public interface VideoMetaDataRepo extends JpaRepository<VideoMetaData, String> {
	
	

}
