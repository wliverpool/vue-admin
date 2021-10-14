package wfm.example.common.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 分配用户dto
 * @author 吴福明
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class AssigneeDto {

    private String username;

    private Boolean isExecutor;
}
