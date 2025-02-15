package app.netlify.sachin1008.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
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

}
