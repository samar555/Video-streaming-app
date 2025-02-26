package app.netlify.sachin1008.controller;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.ResourceRegion;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import app.netlify.sachin1008.ResponseMessage.Message;
import app.netlify.sachin1008.entity.VideoMetaData;
import app.netlify.sachin1008.services.VideoMetaDataService;

@RestController
@RequestMapping("api/v1/")
@CrossOrigin("http://localhost:4200")
public class FilesController {

	@Autowired
	private VideoMetaDataService videoMetaDataService;

	@Value("${VIDEO.CHUNK.FILE}")
	private Long chunk;

	@PostMapping("video/save")
	public ResponseEntity<Message> saveFile(@RequestBody MultipartFile file, @RequestParam String videoDescription) {
		Message message = new Message();

		if (file != null) {
			message.setObject(null);
			System.out.println("file is " + file);
			List<?> messages = videoMetaDataService.saveFile(file, videoDescription);
			Boolean status = Boolean.parseBoolean(messages.get(0).toString());
			String response = messages.get(1).toString();
			message.setStatus(status);
			if (message.getStatus() == true) {
				message.setResult("file saved successfully");
				return ResponseEntity.ok(message);
			} else {
				message.setResult(response);
				return ResponseEntity.badRequest().body(message);
			}

		} else {
			message.setObject(null);
			message.setResult("file can't be empty");
			message.setStatus(false);

			return ResponseEntity.badRequest().body(message);
		}

	}

	@GetMapping("streaming/video")
	public ResponseEntity<Resource> streamVideoRange(@RequestHeader(value = "Range", required = false) String range) {
		System.out.println(range);
		//

		VideoMetaData video = videoMetaDataService.getVideo();
		Path path = Paths.get(video.getVideoFilePath());

		Resource resource = new FileSystemResource(path);

		String contentType = video.getContentType();

		if (contentType == null) {
			contentType = "application/octet-stream";

		}

		// file ki length
		long fileLength = path.toFile().length();

		InputStream inputStream;
		// pahle jaisa hi code hai kyuki range header null

//            return ResponseEntity.ok().contentType(MediaType.parseMediaType(contentType)).body(resource);

		// calculating start and end range

		long rangeStart;

		long rangeEnd;

		if (range == null) {
			rangeStart = 0;
		} else {
			String[] ranges = range.replace("bytes=", "").split("-");
			rangeStart = Long.parseLong(ranges[0]);
		}
		rangeEnd = rangeStart + chunk - 1;

		if (rangeEnd >= fileLength) {
			rangeEnd = fileLength - 1;
		}

//        if (ranges.length > 1) {
//            rangeEnd = Long.parseLong(ranges[1]);
//        } else {
//            rangeEnd = fileLength - 1;
//        }
//
//        if (rangeEnd > fileLength - 1) {
//            rangeEnd = fileLength - 1;
//        }

		System.out.println("range start : " + rangeStart);
		System.out.println("range end : " + rangeEnd);

		try {

			inputStream = Files.newInputStream(path);
			inputStream.skip(rangeStart);
			long contentLength = rangeEnd - rangeStart + 1;

			byte[] data = new byte[(int) contentLength];
			int read = inputStream.read(data, 0, data.length);
			System.out.println("read(number of bytes) : " + read);

			HttpHeaders headers = new HttpHeaders();
			headers.add("Content-Range", "bytes " + rangeStart + "-" + rangeEnd + "/" + fileLength);
			headers.add("Cache-Control", "no-cache, no-store, must-revalidate");
			headers.add("Pragma", "no-cache");
			headers.add("Expires", "0");
			headers.add("X-Content-Type-Options", "nosniff");
			headers.setContentLength(contentLength);

			return ResponseEntity.status(HttpStatus.PARTIAL_CONTENT).headers(headers)
					.contentType(MediaType.parseMediaType(contentType)).body(new ByteArrayResource(data));

		} catch (IOException ex) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
		}

	}

}
