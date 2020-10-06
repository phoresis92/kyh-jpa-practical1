package tk.youngdk.jpashop.repository;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import org.springframework.util.StringUtils;
import tk.youngdk.jpashop.domain.Member;
import tk.youngdk.jpashop.domain.Order;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.*;
import java.util.ArrayList;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class OrderRepository {

    private final EntityManager em;

    public void save(Order order){
        em.persist(order);
    }

    public Order findOne(Long orderId) {
        return em.find(Order.class, orderId);
    }

    public List<Order> findAll(OrderSearch orderSearch){
        return em.createQuery("select o from Order o join o.member m" +
                        " where o.status = :status " +
                        " and m.name like :name", Order.class)
                .setParameter("status", orderSearch.getOrderStatus())
                .setParameter("name", orderSearch.getMemberName())
//                .setFirstResult(0)
                .setMaxResults(1000) // 최대 1000건
                .getResultList();
    }

    public List<Order> findAllByCriteria(OrderSearch orderSearch) {

        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<Order> cq = cb.createQuery(Order.class);
        Root<Order> o = cq.from(Order.class);
        Join<Order, Member> m = o.join("member", JoinType.INNER); //회원과 조인

        List<Predicate> criteria = new ArrayList<>();

        //주문 상태 검색
        if (orderSearch.getOrderStatus() != null) {
            Predicate status = cb.equal(o.get("status"),
                    orderSearch.getOrderStatus());
            criteria.add(status);
        }

        //회원 이름 검색
        if (StringUtils.hasText(orderSearch.getMemberName())) {
            Predicate name =
                    cb.like(m.<String>get("name"), "%" +
                            orderSearch.getMemberName() + "%");
            criteria.add(name);
        }

        cq.where(cb.and(criteria.toArray(new Predicate[criteria.size()])));
        TypedQuery<Order> query = em.createQuery(cq).setMaxResults(1000); //최대 1000건

        return query.getResultList();
    }

    public List<Order> findAllWithMemberDelivery() {
        List<Order> resultList = em.createQuery(
                "select o from Order o " +
                        " join fetch o.member m " +
                        " join fetch o.delivery d ", Order.class
        )
                .getResultList();

        return resultList;

    }

    public List<Order> findAllWithItem() {
        return em.createQuery(
                "select distinct o from Order o " +
                " join fetch o.member m " +
                " join fetch o.delivery d " +
                " join fetch o.orderItems oi " +
                " join fetch oi.item i ", Order.class)
//                .setFirstResult(1)
//                .setMaxResults(100)
                .getResultList();


        /*
        HHH000104: firstResult/maxResults specified with collection fetch; applying in memory!
        distinct를 에플리케이션 레벨에서 하기때문에 DB에서 모든 데이터를 메모리에 올려서 소팅 한다!!!

        컬렉션 페치 조인은 무조건 1개만 사용한다!!!
        1 * N 까지 사용하고
        1 * N * M 이상 될경우 하이버네이트가 아무리 distinct 여도 데이터가 부정합하게 조회될 수 있다!
        */
    }
}
