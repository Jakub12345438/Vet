package przychodnia.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MailDto {

    private String from;
    private String to;
    private String subject;
    private Map<String, Object> model;

}
