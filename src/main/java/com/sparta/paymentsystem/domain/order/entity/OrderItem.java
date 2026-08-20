package com.sparta.paymentsystem.domain.order.entity;

import com.sparta.paymentsystem.domain.product.entity.Product;
import com.sparta.paymentsystem.global.entity.BaseTimeEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "order_items")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class OrderItem extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_id", nullable = false)
    private Order order;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id", nullable = false)
    private Product product;

    @Column(name = "product_name", nullable = false, length = 200)
    private String productName;

    @Column(name = "order_price", nullable = false, columnDefinition = "int UNSIGNED")
    private int orderPrice;

    @Column(nullable = false, columnDefinition = "int UNSIGNED")
    private int quantity;

    public OrderItem(Product product, int orderPrice, int quantity) {
        this.product = product;
        this.productName = product.getName();
        this.orderPrice = orderPrice;
        this.quantity = quantity;
    }

    void setOrder(Order order) {
        this.order = order;
    }

    public int getSubtotal() {
        return orderPrice * quantity;
    }
}

/*
orderPrice 필드: 주문 시점 가격 스냅샷으로 고정 → 이후 상품 가격 변동과 완전히 독립
productName 별도 저장: 상품이 삭제되어도 주문 이력 보존 가능
getSubtotal(): orderPrice × quantity 계산을 엔티티 내부로 캡슐화 → 서비스 계층 중복 연산 제거
setOrder()를 패키지 프라이빗으로 제한 → Order.addOrderItem()을 통해서만 연관관계 설정 강제
 */
