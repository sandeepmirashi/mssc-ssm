package mirashi.tech.msscssm.services;

import mirashi.tech.msscssm.domain.Payment;
import mirashi.tech.msscssm.domain.PaymentEvent;
import mirashi.tech.msscssm.domain.PaymentState;
import mirashi.tech.msscssm.repository.PaymentRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.RepeatedTest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.statemachine.StateMachine;

import javax.transaction.Transactional;
import java.math.BigDecimal;

@SpringBootTest
class PaymentServiceImplTest {
    @Autowired
    PaymentService paymentService;

    @Autowired
    PaymentRepository paymentRepository;
    Payment payment;

    @BeforeEach
    void setUp() {
        payment = Payment.builder().amount(new BigDecimal("12.99"))
                .build();
    }

    @Transactional
    @Test
    void preAuth() {
        Payment savedPayment = paymentService.newPayment(payment);
        System.out.println("Should be New");
        System.out.println(savedPayment.getState());

        StateMachine<PaymentState, PaymentEvent> sm = paymentService.preAuth(savedPayment.getId());

        paymentService.preAuth(savedPayment.getId());
        Payment preAuthPayment = paymentRepository.getOne(savedPayment.getId());


        System.out.println("Should be PRE_AUTH/PRE_AUTH_ERROR and it is %s" + sm.getState().getId());
        System.out.println(preAuthPayment);
    }

    @Transactional
    @RepeatedTest(10)
//    @Test
    void testAuth(){
        Payment savedPayment = paymentService.newPayment(payment);
        StateMachine<PaymentState, PaymentEvent> sm = paymentService.preAuth(savedPayment.getId());
        if (sm.getState().getId() == PaymentState.PRE_AUTH){
            System.out.println("Payment is Pre Authorized");
            StateMachine<PaymentState, PaymentEvent> authSM = paymentService.authorizePayment(savedPayment.getId());
            System.out.println("Result of Auth: " + authSM.getState().getId());

        }else{
            System.out.println("Payment failed in pre-auth");
        }
    }
}