package app.netlify.sachin1008.controller;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.ResourceRegion;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import app.netlify.sachin1008.ResponseMessage.Message;
import app.netlify.sachin1008.services.VideoMetaDataService;

@RestController
@RequestMapping("api/v1/")
public class FilesController {
	
	@Autowired
	private VideoMetaDataService videoMetaDataService;
	
	
	
	@PostMapping("video/save")
	public ResponseEntity<Message> saveFile(@RequestBody MultipartFile file,@RequestParam String videoDescription){
		Message message=new Message();
		
		if(file!=null) {
			message.setObject(null);
			System.out.println("file is "+file);
			List<?> messages=videoMetaDataService.saveFile(file,videoDescription);
			Boolean status= Boolean.parseBoolean(messages.get(0).toString());
			String response=messages.get(1).toString();
			message.setStatus(status);
			if(message.getStatus()==true) {
				message.setResult("file saved successfully");
				return ResponseEntity.ok(message);
			}else {
				message.setResult(response);
				return ResponseEntity.badRequest().body(message);
			}
		
		}else {
			message.setObject(null);
			message.setResult("file can't be empty");
			message.setStatus(false);
			
			return ResponseEntity.badRequest().body(message);
		}
		
	}
	
	
	@PostMapping("streaming/video")
	public ResponseEntity<ResourceRegion> onlineStreaming(@RequestHeader HttpHeaders headers){
		 File file = new File("");
	        FileSystemResource video = new FileSystemResource(file);
	        String contentType=null;
	        long contentLength=0;
			try {
				contentType = Files.probeContentType(file.toPath());
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} // Get dynamic content type
	       
			try {
				contentLength = video.contentLength();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
	        ResourceRegion region =videoMetaDataService.getResourceRegion(video, headers, contentLength);

	        return ResponseEntity.status(HttpStatus.PARTIAL_CONTENT)
	                .contentType(MediaType.parseMediaType(contentType)) // Use detected content type
	                .body(region);
	}

}
