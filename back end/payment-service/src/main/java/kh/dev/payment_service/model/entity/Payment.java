package kh.dev.payment_service.model.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import java.math.BigDecimal;
import kh.dev.common_util.config.Auditable;
import kh.dev.common_util.constant.CurrencyType;
import kh.dev.common_util.constant.PaymentStatus;
import kh.dev.common_util.constant.PaymentType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
public class Payment extends Auditable {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(unique = true, nullable = false)
  private String uuid;

  @Column(nullable = false)
  private String userEmail;

  @Column(nullable = false)
  private Long bookingId;

  @Column(nullable = false)
  private BigDecimal amount;

  @Enumerated(EnumType.STRING)
  @Column(nullable = false)
  private CurrencyType currencyType;

  @Enumerated(EnumType.STRING)
  @Column(nullable = false)
  private PaymentType paymentType;

  @Column(unique = true, nullable = false)
  private String transactionUuid;

  @Enumerated(EnumType.STRING)
  @Column(nullable = false)
  private PaymentStatus status;
}
