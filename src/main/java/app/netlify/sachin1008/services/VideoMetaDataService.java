package app.netlify.sachin1008.services;

import java.io.InputStream;
import java.nio.file.CopyOption;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import app.netlify.sachin1008.entity.VideoMetaData;
import app.netlify.sachin1008.repos.VideoMetaDataRepo;

import org.springframework.util.StringUtils;

@Service
public class VideoMetaDataService {

	@Value("${UPLOAD.VIDEO.DIR}")
	private String dir;
	@Autowired
	private VideoMetaDataRepo dataRepo;

	public List<?> saveFile(MultipartFile file,String description) {

		try {
			VideoMetaData videodata = new VideoMetaData();
			if (file != null&&file.getContentType()!=null) {
				String filename = file.getOriginalFilename();
				String contentType = file.getContentType();
				InputStream Stram = file.getInputStream();
				String cleanFilename = org.springframework.util.StringUtils.cleanPath(filename);
				String cleanFolerDir = StringUtils.cleanPath(dir);
				System.out.println(contentType);

				String[] type = contentType.split("/");
				if (type[0].toString().toLowerCase().equalsIgnoreCase("video")) {
					Path path = Paths.get(cleanFolerDir, cleanFilename);
					if (!Files.exists(path)) {
						Path created = Files.createDirectories(path);
						System.out.println("folder created " + created);
					}
					Files.copy(Stram, path, StandardCopyOption.REPLACE_EXISTING);
					System.out.println(path);
					videodata.setContentType(contentType);
					videodata.setVideoDescription(description);
					videodata.setVideoFilePath(path.toString());
					videodata.setVideoName(filename);
					videodata.setVideoId(UUID.randomUUID().toString());
					dataRepo.save(videodata);
					return List.of("true","file uploaded");
				} else {
					return List.of("false","file is not video type");
				}

			} else {
				return List.of("false","file is not empty");
			}

		} catch (Exception e) {
			e.printStackTrace();
			return List.of("false",e.toString());
		}

	}

}
