package jpabook.jpashop.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.validation.constraints.NotEmpty;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter @Setter
public class Member {

    @Id @GeneratedValue
    @Column(name = "member_id")
    private Long id;
    @NotEmpty
    private String name;

    @Embedded
    private Address address;

    @JsonIgnore //제이슨으로 뿌릴 때 이 어노테이션은 안보여줌
    @OneToMany(mappedBy = "member")
    private List<Order> orders = new ArrayList<>();

    public void addOrders(Order order){
        order.setMember(this);
        orders.add(order);

    }
}
