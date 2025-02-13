package app.netlify.sachin1008.services;

import java.io.InputStream;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import app.netlify.sachin1008.entity.VideoMetaData;
import org.springframework.util.StringUtils;

@Service
public class VideoMetaDataService {

	@Value("${UPLOAD.VIDEO.DIR}")
	private String dir;

	public boolean saveFile(MultipartFile file) {
		VideoMetaData videodata = new VideoMetaData();
		try {
			if (file != null) {
				String filename = file.getOriginalFilename();
				String contentType = file.getContentType();
				InputStream Stram = file.getInputStream();
				String cleanFilename = org.springframework.util.StringUtils.cleanPath(filename);
				String cleanFolerDir = StringUtils.cleanPath(dir);
				Path path=Paths.get(cleanFolerDir,cleanFilename);
				System.out.println(path);
				

			}

		} catch (Exception e) {
			e.printStackTrace();
		}

		return false;
	}

}
