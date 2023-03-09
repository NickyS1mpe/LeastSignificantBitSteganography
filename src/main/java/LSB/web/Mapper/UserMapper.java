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

    @Select(value = "select u.name,u.password from account u where u.name=#{name}")
    @Results
            ({@Result(property = "name", column = "name"),
                    @Result(property = "password", column = "password")})
    Account findUserByName(@Param("name") String name);

    @Select("select * from account where name=#{name}")
    Account getDetails(String name);
}
