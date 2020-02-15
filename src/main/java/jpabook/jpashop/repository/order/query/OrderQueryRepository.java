package jpabook.jpashop.repository.order.query;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Repository
@RequiredArgsConstructor
public class OrderQueryRepository {

    private final EntityManager em;

    public List<OrderQueryDto> findOrderQueryDtos() {

        List<OrderQueryDto> result = findOrders();

        result.forEach(o -> {
            List<OrderItemQueryDto> orderItems = findOrderItems(o.getOrderId());
            o.setOrderItems(orderItems);
        });
        return result;
    }

    public List<OrderQueryDto> findAllByDto_optimization() {
        List<OrderQueryDto> reslut = findOrders();
        //orderId를 빼서 list에 넣음
        List<Long> orderIds = reslut.stream()
                .map(orderQueryDto -> orderQueryDto.getOrderId())
                .collect(Collectors.toList());
        //그걸 파라미터로 오더아이디에 바로 넣어준다
        List<OrderItemQueryDto> orderItems = em.createQuery(
                "select new jpabook.jpashop.repository.order.query.OrderItemQueryDto(oi.order.id, i.name, oi.orderPrice, oi.count)" +
                        " from OrderItem oi" +
                        " join oi.item i" +
                        " where oi.order.id in :orderIds", OrderItemQueryDto.class) //in 절로 한번에 가져오기
                .setParameter("orderIds", orderIds)
                .getResultList();

        //orderItems 를 mpa으로 바꿔서 최적화를 시킨다
        //key는 orderItemQueryDto.getOrderId 값은 OrderItemQueryDto
        Map<Long, List<OrderItemQueryDto>> orderItemMap = orderItems.stream()
                .collect(Collectors.groupingBy(orderItemQueryDto -> orderItemQueryDto.getOrderId()));
        //사용법 orderItemMap.get(key값)
        //order에 루프를 돌리면서 orderItems에 set해서 메모리에 맵을 올려둠
        reslut.forEach(orderQueryDto ->
                orderQueryDto.setOrderItems(orderItemMap.get(orderQueryDto.getOrderId())));
        return reslut;

    }
//==오더아이템 제이피큐엘 DTO==
    private List<OrderItemQueryDto> findOrderItems(Long orderId) {
        return em.createQuery(
                "select new jpabook.jpashop.repository.order.query.OrderItemQueryDto(oi.order.id, i.name, oi.orderPrice, oi.count)" +
                        " from OrderItem oi" +
                        " join oi.item i" +
                        " where oi.order.id = :orderId", OrderItemQueryDto.class)
                .setParameter("orderId", orderId)
                .getResultList();
    }
//==오더찾는 JQPL DTO==
    private List<OrderQueryDto> findOrders() {
        return em.createQuery(
                "select new jpabook.jpashop.repository.order.query.OrderQueryDto(o.id, m.name, o.orderDate, o.status, d.address)" +
                        " from Order o" +
                        " join o.member m" +
                        " join o.delivery d", OrderQueryDto.class)
                .getResultList();
    }

}
