package LSB.web.Mapper;

import LSB.web.Model.Account;
import org.apache.ibatis.annotations.*;
import org.springframework.stereotype.Component;

/**
 * @ClassName: UserMapper
 * @Description: TODO
 * @Author: Nick Lee
 * @Date: Create in 23:05 2023/3/6
 **/
@Component
public interface UserMapper {

    /**
     * @Author: Nick Lee
     * @Description: get user's password
     * @Date: 2023/3/10 20:41
     * @Return:
     **/
    @Select(value = "select u.name,u.password from account u where u.name=#{name}")
    @Results
            ({@Result(property = "name", column = "name"),
                    @Result(property = "password", column = "password")})
    Account findUserByName(@Param("name") String name);

    /**
     * @Author: Nick Lee
     * @Description: get user's data
     * @Date: 2023/3/10 20:41
     * @Return:
     **/
    @Select("select * from account where name=#{name}")
    Account getDetails(String name);

    @Insert("")
    void Logs();
}
