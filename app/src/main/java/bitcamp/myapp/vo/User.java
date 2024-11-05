package bitcamp.myapp.vo;

import java.io.Serializable;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@EqualsAndHashCode(of = {"no"})
public class User implements Serializable {

    private static final long serialVersionUID = 1L;

    private int no;
    private String name;
    private String email;
    private String password;
    private String tel;
    private String photo;

    public User(int no) {
        this.no = no;
    }
}
