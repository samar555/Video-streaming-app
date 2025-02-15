package app.netlify.sachin1008.ResponseMessage;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Message {
  
	Boolean status;
	String result;
	Object object;
	
}
