package co.yixiang.modules.security.security.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

/**
 * @author hupeng
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class OnlineUser {

    private Long id;

    private String userName;

    private String nickName;

    private String browser;

    private String ip;

    private String address;

    private String key;

    private Date loginTime;


}
