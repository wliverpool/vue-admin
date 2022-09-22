package wfm.example.back.common.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * 字典DTO
 * @author 吴福明
 */

@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@JsonIgnoreProperties(ignoreUnknown = true)
public class DictDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    public DictDTO() {
    }

    public DictDTO(String value, String text) {
        this.value = value;
        this.text = text;
    }

    /**
     * 字典value
     */
    private String value;
    /**
     * 字典文本
     */
    private String text;

    public String getTitle() {
        return this.text;
    }

}
